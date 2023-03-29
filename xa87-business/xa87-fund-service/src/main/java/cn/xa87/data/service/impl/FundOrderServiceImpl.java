package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DataUtils;
import cn.xa87.common.utils.OrderUtils;
import cn.xa87.data.mapper.BalanceMapper;
import cn.xa87.data.mapper.BalanceRecordMapper;
import cn.xa87.data.mapper.FundOrderMapper;
import cn.xa87.data.mapper.FundProductMapper;
import cn.xa87.data.service.FundOrderService;
import cn.xa87.model.Balance;
import cn.xa87.model.BalanceRecord;
import cn.xa87.model.FundOrder;
import cn.xa87.model.FundProduct;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.FundOrderVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FundOrderServiceImpl extends ServiceImpl<FundOrderMapper, FundOrder> implements FundOrderService {
    @Autowired
    private FundProductMapper fundProductMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public List<Map<String,Object>> getFundOrderByUserId(String userId, String status) {
        DecimalFormat df = new DecimalFormat("#.##");
        List<Map<String,Object>> list=new ArrayList<>();
        QueryWrapper<FundOrder> wrapperMain = new QueryWrapper<FundOrder>();
        wrapperMain.eq("enabled", status);
        wrapperMain.eq("member_id", userId);
        wrapperMain.orderByDesc("create_time");
        List<FundOrder> fundOrders = this.baseMapper.selectList(wrapperMain);
        for (FundOrder f:fundOrders){
            Map<String,Object> map=JSON.parseObject(JSON.toJSONString(f), Map.class);
            FundProduct fundProduct = fundProductMapper.selectById(f.getFundProductId());
            map.put("fund_image",fundProduct.getFundImage());
            map.put("name",fundProduct.getZhName());
            map.put("todayMoney",f.getPrice().multiply(fundProduct.getTodayRate()));
            map.put("rate",df.format(fundProduct.getDayRateFront())+"%~"+df.format(fundProduct.getDayRateBehind())+"%");
            map.put("money",df.format(f.getPrice().multiply(fundProduct.getDayRateFront()))+"~"+df.format(f.getPrice().multiply(fundProduct.getDayRateBehind()))+"(USDT)");
            list.add(map);
        }
        return list;
    }

    @Override
    public Boolean setFundOrderPurchase(FundOrderVo fundOrderVo) {
        //判断基金产品是否存在
        FundProduct fundProduct = fundProductMapper.selectById(fundOrderVo.getFundProductId());
        if (fundProduct == null) {
            throw new BusinessException("基金产品不存在，请检查id是否正确");
        }

        FundOrder fundOrder = new FundOrder(
                OrderUtils.getCode(),
                fundOrderVo.getFundProductId(),
                fundOrderVo.getMemberId(),
                fundOrderVo.getValueDate(),
                fundOrderVo.getFinishValueDate(),
                fundOrderVo.getPeriodDay(),
                fundOrderVo.getResidueDay(),
                fundOrderVo.getPrice(),
                new BigDecimal(0.00),
                fundProduct.getDefaultRatio().multiply(new BigDecimal(fundOrderVo.getResidueDay())).multiply(fundOrderVo.getPrice()),
                0,
                new Date(),
                null
        );
        this.baseMapper.insert(fundOrder);
        //今日购买，第二天产生收益  扣除用户账号资金，记录
        //改变账号余额
        openBalance(fundOrderVo.getMemberId(),fundOrderVo.getPrice());
        return true;
    }

    @Override
    public Boolean setFundOrderRedeem(FundOrderVo fundOrderVo) {
        //违约金计算公式=基金产品 违约结算比列*剩余天*投资金额
        //		    正常赎回=累计收益
        //判断基金产品是否存在
        FundOrder fundOrder=this.baseMapper.selectById(fundOrderVo.getId());
        if (fundOrder == null) {
            throw new BusinessException("订单不存在，请检查id是否正确");
        }
        FundProduct fundProduct = fundProductMapper.selectById(fundOrder.getFundProductId());
        if (fundProduct == null) {
            throw new BusinessException("基金产品不存在，请检查id是否正确");
        }
        //计算违约金
        //改变状态
        //改变结束时间
        //改变收益率 扣除 违约金
        //剩余天数归零
        BigDecimal penalPrice=fundProduct.getDefaultRatio().multiply(new BigDecimal(fundOrder.getResidueDay())).multiply(fundOrder.getPrice());
        fundOrder.setEnabled(2);
        fundOrder.setFinishValueDate(new Date());
        fundOrder.setAccumulatedIncome(fundOrder.getAccumulatedIncome().subtract(penalPrice));
        fundOrder.setResidueDay(0);
        this.baseMapper.updateById(fundOrder);
        //改变账号余额
        updateBalance(fundOrder.getMemberId(),fundOrder.getPrice().add(fundOrder.getAccumulatedIncome()));
        return true;
    }

    @Override
    public Boolean setRedeem(FundOrderVo fundOrderVo) {

        FundOrder fundOrder=this.baseMapper.selectById(fundOrderVo.getId());
        if (fundOrder == null) {
            throw new BusinessException("订单不存在，请检查id是否正确");
        }
        FundProduct fundProduct = fundProductMapper.selectById(fundOrderVo.getFundProductId());
        if (fundProduct == null) {
            throw new BusinessException("基金产品不存在，请检查id是否正确");
        }
        fundOrder.setEnabled(1);
        fundOrder.setFinishValueDate(new Date());
        fundOrder.setResidueDay(0);
        fundOrder.setAccumulatedIncome(fundOrderVo.getAccumulatedIncome());
        this.baseMapper.updateById(fundOrder);
        //改变账号余额
        updateBalance(fundOrder.getMemberId(),fundOrder.getPrice().add(fundOrder.getAccumulatedIncome()));
        return true;
    }

    @Override
    public OrderCheck getCheckFundOrder(String productId) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        FundProduct fundProduct = fundProductMapper.selectById(productId);
        if (fundProduct == null) {
            throw new BusinessException("基金产品不存在，请检查id是否正确");
        }
        OrderCheck fundOrderCheck=new OrderCheck();
        fundOrderCheck.setName(fundProduct.getZhName());
        fundOrderCheck.setMinx(fundProduct.getInvestmentAmountBehind());
        fundOrderCheck.setMin(fundProduct.getInvestmentAmountFront());
        fundOrderCheck.setBuyDate(new Date());
        fundOrderCheck.setStartDate(DataUtils.addDays(sdf1.format(new Date()),"yyyy-MM-dd",1));
        fundOrderCheck.setDistribute("每天");
        fundOrderCheck.setEndDate(DataUtils.addDays(sdf1.format(fundOrderCheck.getStartDate()),"yyyy-MM-dd",fundProduct.getPeriodDay()));
        fundOrderCheck.setPredictRate(fundProduct.getDayRateFront()+"~"+fundProduct.getDayRateBehind());
        fundOrderCheck.setRansomRate(fundProduct.getDefaultRatio()+"%");
        fundOrderCheck.setTodayRate(fundProduct.getTodayRate()+"%");
        fundOrderCheck.setPeriodDay(fundProduct.getPeriodDay().toString());
        return fundOrderCheck;
    }

    @Override
    public Map<String, Object> getCountFundOrderByUserId(String userId) {

        Map<String,Object> map=new HashMap<>();
        QueryWrapper<FundOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("member_id",userId);
        wrapper.eq("enabled",0);
        List<FundOrder> fundOrder=this.baseMapper.selectList(wrapper);
        BigDecimal toPrice=new BigDecimal(0);
        BigDecimal dayPrice=new BigDecimal(0);
        BigDecimal addUpPrice=new BigDecimal(0);
        for (FundOrder f: fundOrder) {
            BigDecimal a=new BigDecimal(0);
            FundProduct fundProduct = fundProductMapper.selectById(f.getFundProductId());
            if (fundProduct == null) {

            }else {
                if (!f.getValueDate().equals(new Date())) { //新建订单没有收益
                    a = f.getPrice().multiply(fundProduct.getTodayRate());
                }
            }
            toPrice=toPrice.add(f.getPrice());
            dayPrice = dayPrice.add(a);
            addUpPrice.add(f.getAccumulatedIncome());
        }
        map.put("toPrice",toPrice);
        map.put("dayPrice",dayPrice);
        map.put("addUpPrice",addUpPrice);
        map.put("size",fundOrder.size());
        return map;
    }

    /**
     * 扣除金额
     * @param userId
     * @param price 要增加的金额
     * @return
     */
    private Boolean openBalance(String userId,BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", "USDT");
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain==null){
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.subtract(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock( CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金减少记录
            saveBalanceRecord(userId,"USDT",10,1,
                    assetsBalance,balanceMain.getAssetsBalance(),price);
        } else {
            openBalance(userId,price);
        }
        //不使用
        return true;
    }

    /**
     * 增加金额
     * @param userId
     * @param price
     * @return
     */
    private BigDecimal updateBalance(String userId,BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", "USDT");
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain==null){
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.add(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock( CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金增加记录
            saveBalanceRecord(userId,"USDT",11,1,
                    assetsBalance,balanceMain.getAssetsBalance(),price);
        } else {
            updateBalance(userId,price);
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
