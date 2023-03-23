package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.SYSconfig;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.job.mapper.*;
import cn.xa87.job.service.BurstService;
import cn.xa87.model.*;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class BurstServiceImpl implements BurstService {
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private ContractMulMapper contractMulMapper;
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public void isBurst() {
        QueryWrapper<ContractMul> wrapperContract = new QueryWrapper<ContractMul>();
        List<ContractMul> contractMuls = contractMulMapper.selectList(wrapperContract);
        // 维持保证金率
        Map<String, BigDecimal> mapEnsure = new HashMap<String, BigDecimal>();
        // 合约乘数
        Map<String, BigDecimal> mapMul = new HashMap<String, BigDecimal>();
        for (ContractMul contractMul : contractMuls) {
            mapEnsure.put(contractMul.getPairsName(), contractMul.getEnsure());
            mapMul.put(contractMul.getPairsName(), contractMul.getContractMul());
        }
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        List<Warehouse> warehouses = warehouseMapper.selectList(wrapperWarehouse);
        Map<String, BigDecimal> userRiskmap = new HashMap<String, BigDecimal>();
        for (Warehouse warehouse : warehouses) {
            try {
                BigDecimal ensure = mapEnsure.get(warehouse.getPairsName());
//				BigDecimal mul=mapMul.get(warehouse.getPairsName());
                QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
                wrapperMain.eq("currency", warehouse.getMainCur());
                wrapperMain.eq("user_id", warehouse.getMember());
                Balance balanceMain = balanceMapper.selectOne(wrapperMain);

//				BigDecimal one = warehouse.getAvePrice().multiply(warehouse.getHands()).multiply(mul)
//						.multiply(ensure);
//				BigDecimal two = one.subtract(balanceMain.getTokenBalance());
//				BigDecimal three = warehouse.getHands().multiply(mul);
//				BigDecimal four = two.divide(three, 8, BigDecimal.ROUND_HALF_UP);
//				if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
//					// 预估强平价格
//					BigDecimal forcePrice=new BigDecimal(Math.abs(four.add(warehouse.getAvePrice()).doubleValue()));
//					warehouse.setForcePrice(forcePrice);
//				} else {
//					warehouse.setForcePrice(warehouse.getAvePrice().subtract(four));
//				}
                // 风险度计算
                if (null != balanceMain) {
                    // 用户可用余额 + 冻结
                    BigDecimal sumBalance = balanceMain.getAssetsBalance().add(balanceMain.getAssetsBlockedBalance());
                    if (null != sumBalance) {
                        // 余额是否等于0或者小于0
                        if (sumBalance.compareTo(new BigDecimal("0")) == 0 || sumBalance.compareTo(new BigDecimal("0")) == -1) {
                            userRiskmap.put(warehouse.getMember(), new BigDecimal("1"));
                        } else {
                            // 保证金率 * （）
                            BigDecimal result = ensure.multiply(warehouse.getTokenPrice()).divide(sumBalance, 8,
                                    BigDecimal.ROUND_HALF_UP);
                            if (userRiskmap.containsKey(warehouse.getMember())) {
                                BigDecimal risk = userRiskmap.get(warehouse.getMember());
                                result = result.add(risk);
                            }
                            userRiskmap.put(warehouse.getMember(), result);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//			warehouseMapper.updateById(warehouse);
        }
        for (String str : userRiskmap.keySet()) {
            if (userRiskmap.get(str).compareTo(new BigDecimal("1")) == 0) {
                burst(str);
                redisRepository.set(CacheConstants.MEMBER_RISK_KEY + str, "0");
                String isSms = redisRepository.get(CacheConstants.MEMBER_RISK_SMS_KEY_100 + str);
                if (isSms == null) {
                    Member member = memberMapper.selectById(str);
                    String message = "【量子科技】尊敬的用户，您的账户风险度已经达到100%，由于您超时未付款，您的订单已被取消。";
                    //sendSms(member.getPhone(), 100);
                    //HttpUtil.sendSms(member.getAreaCode()+member.getPhone(),message);
                    redisRepository.setExpire(CacheConstants.MEMBER_RISK_SMS_KEY_100 + str, "0", 86400);
                }
            }
            if (userRiskmap.get(str).compareTo(new BigDecimal("1")) == 1) {
                burst(str);
                redisRepository.set(CacheConstants.MEMBER_RISK_KEY + str, "0");
                String isSms = redisRepository.get(CacheConstants.MEMBER_RISK_SMS_KEY_100 + str);
                if (isSms == null) {
                    Member member = memberMapper.selectById(str);
                    //String message = "【"+ServiceNameConst.COINNAME+"】尊敬的用户，您的账户风险度已经达到100%，由于您超时未付款，您的订单已被取消。";
                    String message = "【量子科技】尊敬的用户，您的账户风险度已经达到100%，由于您超时未付款，您的订单已被取消。";
                    //sendSms(member.getPhone(), 100);
                    //HttpUtil.sendSms(member.getAreaCode()+member.getPhone(),message);
                    redisRepository.setExpire(CacheConstants.MEMBER_RISK_SMS_KEY_100 + str, "0", 86400);
                }
            }
            if (userRiskmap.get(str).compareTo(new BigDecimal("1")) == -1) {
                if (userRiskmap.get(str).compareTo(new BigDecimal("0.9")) == 1) {
                    String isSms = redisRepository.get(CacheConstants.MEMBER_RISK_SMS_KEY_90 + str);
                    if (isSms == null) {
                        Member member = memberMapper.selectById(str);
                        String message = "【量子科技】尊敬的用户，您的账户风险度已经达到90%(达到100%将进入爆仓流程)，请注意及时补充资金以避免发生不必要的强制平仓。";
                        //sendSms(member.getPhone(), 99);
                        //HttpUtil.sendSms(member.getAreaCode()+member.getPhone(),message);
                        redisRepository.setExpire(CacheConstants.MEMBER_RISK_SMS_KEY_90 + str, "0", 86400);
                    }
                }
                redisRepository.setExpire(CacheConstants.MEMBER_RISK_KEY + str, userRiskmap.get(str).toPlainString(),
                        10);
            }
        }
    }

    @Async
    public void burst(String member) {
        // 持仓均价
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        wrapperOrder.eq("member", member);
        wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        for (ContractOrder contractOrder : contractOrders) {
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_UP);
            } else {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_DOWN);
            }
            contractOrder.setContractHands(contractOrder.getIsContractHands());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            contractOrder.setId(uuid);
            contractOrder.setPrice(contractOrder.getPrice());
            contractOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
            contractOrder.setCreateTime(null);
            contractOrder.setTakeFee(new BigDecimal("0"));
            contractOrder.setCloseType(TokenOrderConstant.Close_Type.BURST);
            contractOrderMapper.insert(contractOrder);
        }
        contractOrderMapper.updateOrderState(member);
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("member", member);
        warehouseMapper.delete(wrapperWarehouse);
        updateBalance(member);
    }

    private void updateBalance(String member) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + member, 5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", "USDT");
            wrapperMain.eq("user_id", member);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            balanceMain.setAssetsBalance(new BigDecimal("0"));
            balanceMain.setAssetsBlockedBalance(new BigDecimal("0"));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + member);
        } else {
            updateBalance(member);
        }
    }

    public boolean sendSms(String phone, Integer num) {

        final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String NoticeSignName = sysConfigMapper.selectByParamKey(SYSconfig.NoticeSignName);
        final String BurstTemplateCode = sysConfigMapper.selectByParamKey(SYSconfig.BurstTemplateCode);

        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;
        DefaultProfile profile = DefaultProfile.getProfile(regionid, access_key_id, access_key_secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionid);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", NoticeSignName);
        request.putQueryParameter("TemplateCode", BurstTemplateCode);
        request.putQueryParameter("TemplateParam", "{\"name\":" + num + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }


    @Override
    @Async
    public void clearProfit() {
        List<String> list = warehouseMapper.getDisMember();
        Map<String, String> map = redisRepository.getKeysValues(CacheConstants.MEMBER_PROFIT_KEY);
        for (String key : map.keySet()) {
            if (!list.contains(key.replaceAll(CacheConstants.MEMBER_PROFIT_KEY, ""))) {
                redisRepository.del(key);
            }
        }
    }


}
