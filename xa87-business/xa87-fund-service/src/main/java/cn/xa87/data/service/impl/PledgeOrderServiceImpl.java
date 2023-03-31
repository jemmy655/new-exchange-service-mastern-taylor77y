package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DataUtils;
import cn.xa87.common.utils.OrderUtils;
import cn.xa87.data.mapper.BalanceMapper;
import cn.xa87.data.mapper.BalanceRecordMapper;
import cn.xa87.data.mapper.PledgeOrderDetailMapper;
import cn.xa87.data.mapper.PledgeOrderMapper;
import cn.xa87.data.service.PledgeOrderService;
import cn.xa87.model.*;
import cn.xa87.vo.PledgeOrderDetailVo;
import cn.xa87.vo.PledgeOrderVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.time.DateUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PledgeOrderServiceImpl extends ServiceImpl<PledgeOrderMapper, PledgeOrder> implements PledgeOrderService {
    @Autowired
    private PledgeOrderDetailMapper pledgeOrderDetailMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Override
    public List<Map<String, Object>> getMemberCionName(String userId) {
        List<Map<String,Object>> list=new LinkedList<>();
        QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
        wrapperMain.eq("user_id", userId);
        List<Balance> balanceMain = balanceMapper.selectList(wrapperMain);
        balanceMain.forEach(item->{
            Map<String,Object> map=new HashMap<>();
            map.put("name",item.getCurrency());
            map.put("price",item.getAssetsBalance());
            list.add(map);
        });
        return list;
    }

    /**
     * 币价值转换
     * @param currencyTarget  币名称
     * @param quantity  USDT 金额
     * @return
     */
    public BigDecimal currency(String currencyTarget,BigDecimal quantity){
        BigDecimal priceTarget = new BigDecimal("1"); //btc 最新价
        // 目标币价格
        if(!currencyTarget.equals("USDT")){
            String resultTarget = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + currencyTarget.toUpperCase() + "/USDT");
            if(null != resultTarget){
                JSONObject jsonInfo = JSONObject.parseObject(resultTarget);
                priceTarget = jsonInfo.getBigDecimal("nowPrice");
            }
        }
        // ustd换算成btc可得多少数量  usdt / btc
        BigDecimal afterExchangeQuantity = quantity.divide(priceTarget,5, BigDecimal.ROUND_HALF_UP);

        return afterExchangeQuantity;
    }

    public BigDecimal nowPrice(String currencyTarget){
        BigDecimal nowPrice = new BigDecimal("1");
        // 目标币价格
        if(!currencyTarget.equals("USDT")){
            String resultTarget = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + currencyTarget.toUpperCase() + "/USDT");
            if(null != resultTarget){
                JSONObject jsonInfo = JSONObject.parseObject(resultTarget);
                nowPrice = jsonInfo.getBigDecimal("nowPrice");
            }
        }
        return nowPrice;
    }

    @Override
    public List<Map<String,Object>> getPledgeOrderList(String userId) {
        List<Map<String,Object>> list=new LinkedList<>();
        QueryWrapper<PledgeOrder> wrapperMain = new QueryWrapper<PledgeOrder>();
        wrapperMain.eq("member_id", userId);
        wrapperMain.orderByDesc("creation_time");
        List<PledgeOrder> fundOrders = this.baseMapper.selectList(wrapperMain);
        for (PledgeOrder f:fundOrders){
            Map<String,Object> map= JSON.parseObject(JSON.toJSONString(f), Map.class);
            QueryWrapper<PledgeOrderDetail> queryWrapper = new QueryWrapper<PledgeOrderDetail>();
            queryWrapper.eq("order_id", f.getId());
            queryWrapper.orderByDesc("creation_time");
            List<PledgeOrderDetail> details=pledgeOrderDetailMapper.selectList(queryWrapper);
            map.put("details",details);
            list.add(map);
        }
        return list;
    }

    @Override
    public Boolean setPledgeOrderBorrow(PledgeOrderVo pledgeOrderVo) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PledgeOrder order=new PledgeOrder(
          pledgeOrderVo.getMemberId(), //用户id
          OrderUtils.getCode(),
          pledgeOrderVo.getBorrowMoney(),// 借还金额
          pledgeOrderVo.getBorrowName(),//借还币名称
          pledgeOrderVo.getPledgeMoney(),//质押金额
          pledgeOrderVo.getPledgeName(),//质押名称
          pledgeOrderVo.getDeadline(),//周期
          pledgeOrderVo.getForcePrice(),//强平金额
          pledgeOrderVo.getPledgeRate(),//质押率
          pledgeOrderVo.getHrRate(),//小时利率
          pledgeOrderVo.getDayRate(),//一天利率
          new BigDecimal(0.002),//
          pledgeOrderVo.getTotalMoney(),//总利息
          pledgeOrderVo.getBorrowMoney(),//总负债
          pledgeOrderVo.getPredictRefundMoney(),//预计还款
          new BigDecimal(0),//
          new Date(),
          DataUtils.addDays(sdf1.format(new Date()),"yyyy-MM-dd HH:mm:ss",pledgeOrderVo.getDeadline()),
          0
        );
        this.baseMapper.insert(order);
        saveDetail(order.getId(),order.getBorrowMoney(),order.getPledgeMoney(),order.getPledgeName(),"BORROW");
        //账户资金增加
        updateBalance(pledgeOrderVo.getBorrowName(),pledgeOrderVo.getMemberId(),pledgeOrderVo.getBorrowMoney());
        //账户 质押资金减少
        openBalance(pledgeOrderVo.getPledgeName(),pledgeOrderVo.getMemberId(),pledgeOrderVo.getPledgeMoney());
        return true;
    }

    public boolean saveDetail(String orderId,BigDecimal BMoney,BigDecimal PMoney,String PName,String type){
        PledgeOrderDetail detail=new PledgeOrderDetail();
        detail.setOrderId(orderId);
        detail.setCreationTime(new Date());
        detail.setMoney(BMoney);
        detail.setPledgeType("币");
        detail.setPledgePrice(PMoney);
        detail.setPledgeName(PName);
        detail.setType(type);
        pledgeOrderDetailMapper.insert(detail);
        return true;
    }

    @Override
    public Boolean setPledgeOrderRepayment(PledgeOrderVo pledgeOrderVo) {
        PledgeOrder  p= this.baseMapper.selectById(pledgeOrderVo.getId());
        p.setTotalIncurDebts(p.getTotalIncurDebts().subtract(pledgeOrderVo.getBorrowMoney()));
        //（订单借贷 + 累计利息）/ 质押资产价值 * 100%  >= 75%  提醒补充质押资产
        BigDecimal rate=new BigDecimal(0.00);
        try {
            rate=p.getBorrowMoney()
                    .subtract(pledgeOrderVo.getBorrowMoney())
                    .add(p.getTotalMoney())
                    .divide(pledgeOrderVo.getPledgeMoney().multiply(nowPrice(p.getPledgeName())),5,BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(1));
        }catch(Exception e){
            System.out.println("计算出错了");
        }
        p.setPledgeRate(rate);
        p.setRefundPrice(p.getRefundPrice().add(pledgeOrderVo.getBorrowMoney()));
        saveDetail(p.getId(),pledgeOrderVo.getBorrowMoney(),p.getPledgeMoney(),p.getPledgeName(),"REFUND");
        if (p.getTotalIncurDebts().compareTo(new BigDecimal(0))==0){
            p.setStatus(1); //结清 btc 增加
            updateBalance(p.getPledgeName(),p.getMemberId(),p.getPledgeMoney());
        }
        this.baseMapper.updateById(p);
        openBalance(p.getBorrowName(),p.getMemberId(),pledgeOrderVo.getBorrowMoney());
        return true;
    }

    @Override
    public PledgeOrderVo getLoanMoney(String userId, BigDecimal loanCycle, String pledge_name, BigDecimal borrow_price, BigDecimal pledge_price) {
        PledgeOrderVo vo=new PledgeOrderVo();
        BigDecimal rate=new BigDecimal(0.00);
        BigDecimal totalMoney=new BigDecimal(0.00);
        try {
            rate=borrow_price.divide(pledge_price.multiply(nowPrice(pledge_name)),5,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1));
            if (rate .compareTo(new BigDecimal(0.70))>=0){
                BigDecimal quantity=borrow_price.multiply(new BigDecimal(0.75));
                throw new BusinessException("质押率过高，质押金额不得低于"+pledge_price.add(currency(pledge_name,quantity)));
            }
        }catch(Exception e){
            System.out.println("计算出错了");
        }
        totalMoney=borrow_price.multiply(loanCycle.multiply(new BigDecimal(0.048)));
        vo.setForcePrice(borrow_price);//强平价格
        vo.setPledgeRate(rate); //质押率
        vo.setHrRate(new BigDecimal(0.002));
        vo.setDayRate(new BigDecimal(0.048));
        vo.setTotalMoney(totalMoney); //总利息
        vo.setPredictRefundMoney(borrow_price.add(totalMoney)); //预计还款
        vo.setFeeMoney(new BigDecimal(0.002)); //强平手续费率
        return vo;
    }



    /**
     * 减少金额
     *
     * @param userId
     * @param price  要增加的金额
     * @return
     */
    private Boolean openBalance(String buyPairName,String userId, BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", buyPairName);
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain == null) {
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.subtract(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金减少记录
            saveBalanceRecord(userId, buyPairName, 10, 1,
                    assetsBalance, balanceMain.getAssetsBalance(), price);
        } else {
            openBalance(buyPairName,userId, price);
        }
        //不使用
        return true;
    }

    /**
     * 增加金额
     *
     * @param userId
     * @param price
     * @return
     */
    private BigDecimal updateBalance(String buyPairName,String userId, BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", buyPairName);
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain == null) {
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.add(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金减少记录
            saveBalanceRecord(userId, buyPairName, 11, 1,
                    assetsBalance, balanceMain.getAssetsBalance(), price);
        } else {
            updateBalance(buyPairName,userId, price);
        }
        //不使用
        return new BigDecimal(0.00);
    }

    private void saveBalanceRecord(String memberId,String currency,Integer balanceType,Integer fundsType,BigDecimal balanceBefore, BigDecimal balanceBack,BigDecimal balanceDifference){
        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setMemberId(memberId);
        balanceRecord.setCurrency(currency);
        balanceRecord.setBalanceType(balanceType);
        balanceRecord.setFundsType(fundsType);
        balanceRecord.setBalanceBefore(balanceBefore);
        balanceRecord.setBalanceBack(balanceBack);
        balanceRecord.setBalanceDifference(balanceDifference);
        balanceRecord.setCreateTime(new Date());
        balanceRecord.setDataClassification(1);
        balanceRecordMapper.insert(balanceRecord);
    }
}
