package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.job.mapper.*;
import cn.xa87.job.service.MatchingTransactionService;
import cn.xa87.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MatchingTransactionServiceImpl implements MatchingTransactionService {

    @Autowired
    private Xa87RedisRepository redisRepository;
    @Resource
    private PairsMapper pairsMapper;
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private BalanceRecordMapper balanceRecordMapper;
    @Resource
    private EntrustMapper entrustMapper;
    @Resource
    private EntrustHistoryMapper entrustHistoryMapper;

    @Override
    public void currencyMatchingTransaction() {
        log.info("币币撮合交易定时任务已经启动");
        QueryWrapper<Entrust> wrapper = new QueryWrapper<>();
        wrapper.eq("execution_status", 1);
        List<Entrust> entrusts = entrustMapper.selectList(wrapper);
        for (Entrust entrust : entrusts) {
            entrust.setExecutionStatus(2);
            entrustMapper.updateById(entrust);

            Entrust entrust1 = new Entrust();
            entrust1.setId(entrust.getId());
            try{
                // 限价
                if(entrust.getPriceType().equals(ContractConstant.Price_Type.CUSTOM_PRICE)){
                    customPriceSettlement(entrust);
                }else {
                    marketPriceSettlement(entrust);
                }
            }catch (Exception e){
                entrust1.setExecutionStatus(1);
                log.error("币币交易结算异常 异常信息[{}] 交易信息[{}]",e,entrust);
            }finally {
                entrust1.setExecutionStatus(1);
                entrustMapper.updateById(entrust1);
            }

        }
    }

    // 限价
    private void customPriceSettlement(Entrust entrust) {
        // 获取当前虚拟币价格
        BigDecimal currentPrice = pairsMapper.getCurrentPrice(entrust.getTokenCur());

        boolean flag = false;

        // 卖出
        if(entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)){
            flag = currentPrice.doubleValue() >= entrust.getPrice().doubleValue();

        }else if(entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)){
            flag = currentPrice.doubleValue() <= entrust.getPrice().doubleValue();
        }

        if(flag){
            marketPriceSettlement(entrust);
        }
    }

    // 市价
    private void marketPriceSettlement(Entrust entrust) {
        // 手续费，费率
        BigDecimal tradeFeeActive;
        if (entrust.getTradeRate() != null) {
            tradeFeeActive = entrust.getCount().multiply(entrust.getTradeRate());
        } else {
            tradeFeeActive = entrust.getCount().multiply(new BigDecimal("0.003"));
        }
        if (entrust.getTradeFee() != null) {
            entrust.setTradeFee(entrust.getTradeFee().add(tradeFeeActive));
        } else {
            entrust.setTradeFee(tradeFeeActive);
        }

        // 金额
        BigDecimal matchFee = entrust.getMatchFee();

        // 卖出
        if(entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)){
            // 扣除冻结
            extractCurrencyExchange(entrust.getMember(),entrust.getTokenCur(),entrust.getCount(),1,39,1);

            // 扣除手续费
            BigDecimal subtract = matchFee.subtract(tradeFeeActive);
            // 增加可用
            extractCurrencyExchange(entrust.getMember(),entrust.getMainCur(),subtract,2,40,2);
        }else if(entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)){
            // 扣除冻结
            extractCurrencyExchange(entrust.getMember(),entrust.getMainCur(),matchFee,1,37,1);
            // 扣除手续费
            BigDecimal subtract = entrust.getCount().subtract(tradeFeeActive);
            // 增加可用
            extractCurrencyExchange(entrust.getMember(),entrust.getTokenCur(),subtract ,2,38,2);
        }

        entrust.setMatchMember("robot");
        entrust.setMethodType(EntrustConstant.Method_Type.ACTIVE);
        entrust.setMatchPrice(entrust.getPrice());
        entrust.setMatchCount(entrust.getCount());
        entrust.setSurplusCount(BigDecimal.valueOf(0));
        entrust.setState(EntrustConstant.Order_State.FINAL);
        EntrustHistory entrustHistory = new EntrustHistory();
        BeanUtils.copyProperties(entrust, entrustHistory);
        entrustHistoryMapper.insert(entrustHistory);
        entrustMapper.deleteById(entrust.getId());

    }

    private void extractCurrencyExchange(String member, String currencyName, BigDecimal balance, Integer type,Integer balanceType,Integer fundsType) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member,
                5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            wrapper.eq("user_id", member);
            wrapper.eq("currency", currencyName);
            Balance userBalance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = userBalance.getAssetsBalance();
            BigDecimal assetsBlockedBalance = userBalance.getAssetsBlockedBalance();
            if(type == 1){
                userBalance.setAssetsBlockedBalance(userBalance.getAssetsBlockedBalance().subtract(balance));
            }else {
                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
            }

            balanceMapper.updateById(userBalance);

            if(type == 1){
                saveBalanceRecord(member,currencyName,balanceType,fundsType,assetsBlockedBalance,userBalance.getAssetsBlockedBalance(),balance);
            }else {
                saveBalanceRecord(member,currencyName,balanceType,fundsType,assetsBalance,userBalance.getAssetsBalance(),balance);
            }

            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
        } else {
        }
    }


    // 保存资金记录
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
        balanceRecord.setDataClassification(3);
        balanceRecordMapper.insert(balanceRecord);
    }
}
