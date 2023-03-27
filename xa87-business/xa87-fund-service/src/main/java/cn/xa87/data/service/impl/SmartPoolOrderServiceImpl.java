package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DataUtils;
import cn.xa87.common.utils.OrderUtils;
import cn.xa87.data.mapper.*;
import cn.xa87.data.service.SmartPoolOrderService;
import cn.xa87.data.service.SmartPoolProductService;
import cn.xa87.model.*;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.SmartPoolOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmartPoolOrderServiceImpl extends ServiceImpl<SmartPoolOrderMapper, SmartPoolOrder> implements SmartPoolOrderService {

    @Autowired
    private SmartPoolProductMapper smartPoolProductMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public List<SmartPoolOrder> getSmartPoolOrderByUserId(String userId, String status) {
        QueryWrapper<SmartPoolOrder> wrapperMain = new QueryWrapper<SmartPoolOrder>();
        wrapperMain.eq("enabled", status);
        wrapperMain.eq("member_id", userId);
        wrapperMain.orderByDesc("create_time");
        List<SmartPoolOrder> fundOrders = this.baseMapper.selectList(wrapperMain);
        return fundOrders;
    }

    @Override
    public OrderCheck getCheckSmartPoolOrder(String productId) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SmartPoolProduct fundProduct = smartPoolProductMapper.selectById(productId);
        if (fundProduct == null) {
            throw new BusinessException("基金产品不存在，请检查id是否正确");
        }
        OrderCheck fundOrderCheck = new OrderCheck();
        fundOrderCheck.setName(fundProduct.getZhName());
        fundOrderCheck.setMinx(fundProduct.getInvestmentAmountBehind());
        fundOrderCheck.setMin(fundProduct.getInvestmentAmountFront());
        fundOrderCheck.setBuyDate(new Date());
        fundOrderCheck.setStartDate(DataUtils.addDays(sdf1.format(new Date()), "yyyy-MM-dd", 1));
        fundOrderCheck.setDistribute("每天");
        fundOrderCheck.setTodayRate(fundProduct.getTodayRate() + "%");
        return fundOrderCheck;
    }

    @Override
    public Map<String, Object> getCountSmartPoolOrderByUserId(String userId) {

        Map<String, Object> map = new HashMap<>();
        QueryWrapper<SmartPoolOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", userId);
        wrapper.eq("enabled", 0);
        List<SmartPoolOrder> fundOrder = this.baseMapper.selectList(wrapper);
        BigDecimal toPrice = new BigDecimal(0);
        BigDecimal dayPrice = new BigDecimal(0);
        BigDecimal addUpPrice = new BigDecimal(0);
        for (SmartPoolOrder f : fundOrder) {
            BigDecimal a = new BigDecimal(0);
            SmartPoolProduct fundProduct = smartPoolProductMapper.selectById(f.getProductId());
            if (fundProduct == null) {
            }else {
                if (!f.getStartTime().equals(new Date())) { //新建订单没有收益
                    a = f.getPrice().multiply(fundProduct.getTodayRate());
                }
            }
            toPrice = toPrice.add(f.getPrice());
            dayPrice = dayPrice.add(a);
            addUpPrice.add(f.getAccumulatedIncome());
        }
        map.put("toPrice", toPrice);
        map.put("dayPrice", dayPrice);
        map.put("addUpPrice", addUpPrice);
        map.put("size", fundOrder.size());
        return map;
    }

    @Override
    public Boolean setSmartPoolOrderPurchase(SmartPoolOrderVo fundOrderVo) {
        //判断基金产品是否存在
        SmartPoolProduct fundProduct = smartPoolProductMapper.selectById(fundOrderVo.getProductId());
        if (fundProduct == null) {
            throw new BusinessException("产品不存在，请检查id是否正确");
        }
        SmartPoolOrder fundOrder = new SmartPoolOrder(
                fundOrderVo.getOrderNumber(),
                fundOrderVo.getProductId(),
                fundOrderVo.getMemberId(),
                fundOrderVo.getValueDate(),
                fundOrderVo.getFinishValueDate(),
                fundOrderVo.getPeriodDay(),
                fundOrderVo.getResidueDay(),
                fundOrderVo.getPrice(),
                fundOrderVo.getAccumulatedIncome(),
                fundOrderVo.getPenalPrice(),
                fundOrderVo.getValueDate(),
                fundOrderVo.getFinishValueDate(),
                0,
                fundProduct.getZhName(),
                fundProduct.getEnName(),
                "",
                "",
                new Date(),
                null
        );
        this.baseMapper.insert(fundOrder);
        //改变账号余额
        openBalance(fundProduct.getBuyPairsName(),fundOrderVo.getMemberId(), fundOrderVo.getPrice());
        return true;
    }

    @Override
    public Boolean setSmartPoolOrderRedeem(SmartPoolOrderVo fundOrderVo) {

        SmartPoolProduct fundProduct = smartPoolProductMapper.selectById(fundOrderVo.getProductId());
        if (fundProduct == null) {
            throw new BusinessException("产品不存在，请检查id是否正确");
        }
        SmartPoolOrder fundOrder=this.baseMapper.selectById(fundOrderVo.getId());
        if (fundOrder == null) {
            throw new BusinessException("订单不存在，请检查id是否正确");
        }
        //改变状态
        //改变结束时间
        //剩余天数归零
        fundOrder.setEnabled(1);
        fundOrder.setFinishValueDate(new Date());
        fundOrder.setEndTime(new Date());
        fundOrder.setResidueDay(0);
        this.baseMapper.updateById(fundOrder);
        //改变账号余额
        updateBalance(fundProduct.getOutPairsName(),fundOrder.getMemberId(),fundOrder.getPrice().add(fundOrder.getAccumulatedIncome()));
        return true;
    }


    /**
     * 扣除金额
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


    private void saveBalanceRecord(String memberId, String currency, Integer balanceType, Integer fundsType, BigDecimal balanceBefore, BigDecimal balanceBack, BigDecimal balanceDifference) {
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
