package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.*;
import cn.xa87.rabbit.mapper.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class ProfitlossPriceBusiness {

    // 请求地址
    public static final String url = "http://intapi.253.com/send/json";
    // API账号，50位以内。必填
    public static final String account = "I7127166";
    // API账号对应密钥，联系客服获取。必填
    public static final String password = "Uq8xGKubr46bfb";
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private ContractMulMapper contractMulMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private MemberMapper memberMapper;

    // 计算盈亏
    public void execute(String msg) {
        try {
            String[] splitResult = msg.split("-");
            BigDecimal price = new BigDecimal(splitResult[0]);
            String pairsName = splitResult[1];
            // 根据交易对获取所有得持仓
//			QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
//			wrapperWarehouse.eq("pairs_name", pairsName);
//			List<Warehouse> warehouses = warehouseMapper.selectList(wrapperWarehouse);

            QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
            wrapperOrder.eq("pairs_name", pairsName);
            wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
            wrapperOrder.eq("order_type", TokenOrderConstant.Order_Type.POSITIONS);
            List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
            // 根据交易对获取合约乘数
            QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
            wrapper.eq("pairs_name", pairsName);
            ContractMul contractMul = contractMulMapper.selectOne(wrapper);
            // 循环所有单子构架map,多线程处理map，提交处理效率
            Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
            for (ContractOrder contractOrder : contractOrders) {
                BigDecimal unProfitLoss = new BigDecimal("0");
                if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                    // 未实现盈亏
                    BigDecimal subPrice = price.subtract(contractOrder.getPrice());
                    unProfitLoss = subPrice.multiply(contractOrder.getIsContractHands()).multiply(contractMul.getContractMul());
                } else {
                    // 未实现盈亏
                    BigDecimal subPrice = contractOrder.getPrice().subtract(price);
                    unProfitLoss = subPrice.multiply(contractOrder.getIsContractHands()).multiply(contractMul.getContractMul());
                }
                if (map.containsKey(contractOrder.getMember())) {
                    map.put(contractOrder.getMember(), map.get(contractOrder.getMember()).add(unProfitLoss));
                } else {
                    map.put(contractOrder.getMember(), unProfitLoss);
                }
                contractOrder.setMatchFee(unProfitLoss);
                contractOrder.setMatchPrice(price);
                contractOrderMapper.updateById(contractOrder);
            }
            for (String key : map.keySet()) {
                try {
                    String memberId = key;
                    BigDecimal balance = map.get(key);
                    updateBalance(memberId, balance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    private void burst(String member) {
        contractOrderMapper.updateOrderState(member);
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("member", member);
        warehouseMapper.delete(wrapperWarehouse);
        updateBalance(member);
    }

    @Async
    private void updateBalance(String memberId, BigDecimal balance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY
                + CacheConstants.SPLIT + memberId, 5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
            wrapperActive.eq("currency", "USDT");
            wrapperActive.eq("user_id", memberId);
            Balance balanceActive = balanceMapper.selectOne(wrapperActive);
            BigDecimal result = balanceActive.getAssetsBalance().add(balance);
            if (result.compareTo(new BigDecimal("0")) == 1) {
                String rdsBalancestr = redisRepository.get(CacheConstants.MEMBER_PROFIT_KEY + memberId);
                BigDecimal rdsBalance = new BigDecimal("0");
                if (rdsBalancestr != null) {
                    rdsBalance = new BigDecimal(rdsBalancestr);
                }
                balanceActive
                        .setAssetsBalance(balanceActive.getAssetsBalance().add(balance).subtract(rdsBalance));
                balanceMapper.updateById(balanceActive);
                redisRepository.set(CacheConstants.MEMBER_PROFIT_KEY + memberId, balance.toPlainString());
            } else {
                burst(memberId);
                redisRepository.set(CacheConstants.MEMBER_RISK_KEY + memberId, "0");
                String isSms = redisRepository.get(CacheConstants.MEMBER_RISK_SMS_KEY_100 + memberId);
                if (isSms == null) {
                    Member member = memberMapper.selectById(memberId);
                    String message = "【BitWorld】尊敬的BitWorld 用户，您的账户风险度已经达到100%，由于您超时未付款，您的订单已被取消。";
                    sendSms(member.getPhone(), message);
                    redisRepository.setExpire(CacheConstants.MEMBER_RISK_SMS_KEY_100 + memberId, "0", 86400);
                }
            }
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY
                    + CacheConstants.SPLIT + memberId);
        } else {
            updateBalance(memberId, balance);
        }
    }

    private void updateBalance(String memberId) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY
                + CacheConstants.SPLIT + memberId, 5000, 50, 100);
        if (lock_coin) {
            log.error("合约余额置空方法进入了!!!");
//            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
//            wrapperMain.eq("currency", "USDT");
//            wrapperMain.eq("user_id", memberId);
//            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
//            balanceMain.setTokenBalance(new BigDecimal("0"));
//            balanceMain.setTokenBlockedBalance(new BigDecimal("0"));
//            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY
                    + CacheConstants.SPLIT + memberId);
        } else {
            updateBalance(memberId);
        }
    }

    @Async
    public boolean sendSms(String phone, String message) {
        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;
        // String msg="【BitWorld】您本次操作验证码为"+code+",有效期5分钟，如非本人操作请忽略！";
        String mobile = "86" + phone;
        JSONObject map = new JSONObject();
        map.put("account", account);
        map.put("password", password);
        map.put("msg", message);
        map.put("mobile", mobile);
        String params = map.toString();
        try {
            HttpUtil.postSms(url, params);
        } catch (Exception e) {
            throw new BusinessException("发送短信异常");
        }
        return Boolean.TRUE;
    }
}
