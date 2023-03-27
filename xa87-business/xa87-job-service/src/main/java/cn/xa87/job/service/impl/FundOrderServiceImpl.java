package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.job.mapper.*;
import cn.xa87.job.service.FundOrderService;
import cn.xa87.model.*;
import cn.xa87.vo.FundOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class FundOrderServiceImpl implements FundOrderService {
    @Autowired
    private FundOrderMapper fundOrderMapper;
    @Autowired
    private FundProductMapper fundProductMapper;

    @Autowired
    private SmartPoolOrderMapper smartPoolOrderMapper;

    @Autowired
    private SmartPoolProductMapper smartPoolProductMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Override
    public void countFundYield() {
        //查询所以 为赎回的订单
        QueryWrapper<FundOrder> wrapperMain = new QueryWrapper<FundOrder>();
        wrapperMain.eq("enabled", "0");
        wrapperMain.le("value_date", new Date());
        wrapperMain.orderByAsc("create_time");
        List<FundOrder> fundOrders=fundOrderMapper.selectList(wrapperMain);
        for (FundOrder f:fundOrders) {
            FundProduct fundProduct = fundProductMapper.selectById(f.getFundProductId());
            //今日收益计算公式 =金额*今日利率
            BigDecimal price=f.getPrice().multiply(fundProduct.getTodayRate());
           if (f.getResidueDay()==0){
               f.setEnabled(1);
               fundOrderMapper.updateById(f);
               //改变账号余额
               updateBalance("USDT",f.getMemberId(),f.getPrice().add(f.getAccumulatedIncome()));
           }else {
                f.setAccumulatedIncome(f.getAccumulatedIncome().add(price)); //修改收益率
                f.setResidueDay(f.getResidueDay()-1);
                fundOrderMapper.updateById(f);
           }
        }
    }
    private void FundOrder(FundOrder f){
        FundProduct fundProduct = fundProductMapper.selectById(f.getFundProductId());
        //今日收益计算公式 =金额*今日利率
        BigDecimal price=f.getPrice().multiply(fundProduct.getTodayRate());
        if (f.getResidueDay()==0){
            f.setEnabled(1);
            fundOrderMapper.updateById(f);
            //改变账号余额
            updateBalance("USDT",f.getMemberId(),f.getPrice().add(f.getAccumulatedIncome()));
        }else {
            f.setAccumulatedIncome(f.getAccumulatedIncome().add(price)); //修改收益率
            f.setResidueDay(f.getResidueDay()-1);
            fundOrderMapper.updateById(f);
        }
    }

    private void SmartPoolOrder(SmartPoolOrder f){
        SmartPoolProduct smartPoolProduct = smartPoolProductMapper.selectById(f.getProductId());
        //今日收益计算公式 =金额*今日利率
        BigDecimal price=f.getPrice().multiply(smartPoolProduct.getTodayRate());
        f.setAccumulatedIncome(f.getAccumulatedIncome().add(price)); //修改收益率
        f.setResidueDay(f.getResidueDay()-1);
        smartPoolOrderMapper.updateById(f);
        //改变账号余额
    }

    /**
     * 增加金额
     * @param userId
     * @param price
     * @return
     */
    private BigDecimal updateBalance(String buyPairName,String userId,BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", buyPairName);
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
            updateBalance(buyPairName,userId,price);
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
