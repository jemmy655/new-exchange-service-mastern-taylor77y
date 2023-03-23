package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DateUtil;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.model.*;
import cn.xa87.rabbit.mapper.BalanceMapper;
import cn.xa87.rabbit.mapper.BalanceRecordMapper;
import cn.xa87.rabbit.mapper.EntrustHistoryMapper;
import cn.xa87.rabbit.mapper.EntrustMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class EntrustMarketBalanceBusiness {

    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private EntrustMapper entrustMapper;
    @Resource
    private Xa87RedisRepository redisRepository;
    @Resource
    private EntrustHistoryMapper entrustHistoryMapper;
    @Resource
    private BalanceRecordMapper balanceRecordMapper;

    public void execute(String msg) throws ParseException {
        EntrustMarketBalance entrustBalance = JSONObject.parseObject(msg, EntrustMarketBalance.class);
        String pairsName = entrustBalance.getPairsName();
        BigDecimal price = entrustBalance.getPrice();
        BigDecimal count = entrustBalance.getCount();
        Entrust activeEntrust = entrustBalance.getActiveOrder();
        // 修改资产if (!matchBalance.getPassiveWarehouse().contains("robot")) {
        activeEntrust.setSurplusCount(new BigDecimal("0"));
        // 被动方
        if (!entrustBalance.getPassiveOrder().contains("robot")) {
            Entrust passiveEntrust = entrustMapper.selectById(entrustBalance.getPassiveOrder());
            if (passiveEntrust == null) {
                EntrustHistory entrustHistory = entrustHistoryMapper.selectById(entrustBalance.getPassiveOrder());
                if(null == entrustHistory){
                    log.error("未查询到数据，条件====》"+entrustBalance.getPassiveOrder());
                    return;
                }
                activeEntrust.setMatchMember(entrustHistory.getMember());
            } else {
                activeEntrust.setMatchMember(passiveEntrust.getMember());
            }
        } else {
            activeEntrust.setMatchMember("robot");
        }
        activeEntrust.setMatchCount(entrustBalance.getCount());
        activeEntrust.setMatchPrice(entrustBalance.getPrice());
        // 主动的
        activeEntrust.setMethodType(EntrustConstant.Method_Type.ACTIVE);
        activeEntrust.setMatchFee(price.multiply(count));
        activeEntrust.setSurplusCount(new BigDecimal("0"));
        activeEntrust.setCount(count);
        activeEntrust.setPrice(price);
        activeEntrust.setState(EntrustConstant.Order_State.FINAL);
        // 手续费
        BigDecimal tradeFeeActive = new BigDecimal("0");
        if (activeEntrust.getTradeRate() != null) {
            tradeFeeActive = price.multiply(count).multiply(activeEntrust.getTradeRate());
        } else {
            tradeFeeActive = price.multiply(count).multiply(new BigDecimal("0.003"));
        }
        if (activeEntrust.getTradeFee() != null) {
            activeEntrust.setTradeFee(activeEntrust.getTradeFee().add(tradeFeeActive));
        } else {
            activeEntrust.setTradeFee(tradeFeeActive);
        }
        EntrustHistory entrustHistory = new EntrustHistory();
        BeanUtils.copyProperties(activeEntrust, entrustHistory);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        entrustHistory.setId(uuid);
        entrustHistoryMapper.insert(entrustHistory);

        updateOrderBalanceA(activeEntrust, entrustBalance);


        if (!entrustBalance.getPassiveOrder().contains("robot")) {
            Entrust passiveEntrust = entrustMapper.selectById(entrustBalance.getPassiveOrder());
            if(null == passiveEntrust){
                log.error("未查询到数据，条件====》"+entrustBalance.getPassiveOrder());
                return;
            }
            // 被动方
            passiveEntrust.setSurplusCount(passiveEntrust.getSurplusCount().subtract(entrustBalance.getCount()));
            // 主动方
            passiveEntrust.setMatchMember(activeEntrust.getMember());
            passiveEntrust.setMatchCount(passiveEntrust.getMatchCount().add(entrustBalance.getCount()));
            // 被动的
            passiveEntrust.setMethodType(EntrustConstant.Method_Type.PASSIVE);
            passiveEntrust.setMatchPrice(entrustBalance.getPrice());
            passiveEntrust.setMatchFee(passiveEntrust.getMatchCount().multiply(passiveEntrust.getMatchPrice()));
            updateOrderBalanceP(passiveEntrust, entrustBalance);
        }

        /// 添加Kline----
        // redis
        long timestamp = new Date().getTime();
        // 一分钟
        Long oneMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 1);
        String oneMinute = "1m";
        redisKline(pairsName, price, count, oneMinuteTimestamp, oneMinute);
        // 五分钟
        Long fiveMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 5);
        String fiveMinute = "5m";
        redisKline(pairsName, price, count, fiveMinuteTimestamp, fiveMinute);
        // 15分钟
        Long fifteenMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 15);
        String fifteenMinute = "15m";
        redisKline(pairsName, price, count, fifteenMinuteTimestamp, fifteenMinute);
        // 30分钟
        Long thirtyMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 30);
        String thirtyMinute = "30m";
        redisKline(pairsName, price, count, thirtyMinuteTimestamp, thirtyMinute);
        // 1小时
        Long oneHourTimestamp = DateUtil.hourTimestamp(timestamp);
        String oneHour = "1h";
        redisKline(pairsName, price, count, oneHourTimestamp, oneHour);
        // 一天
        Long oneDayTimestamp = DateUtil.dayTimestamp(timestamp);
        String oneDay = "1d";
        redisKline(pairsName, price, count, oneDayTimestamp, oneDay);
        // 一星期
        Long oneWeekTimestamp = DateUtil.weekTimestamp(timestamp, 1);
        String oneWeek = "1w";
        redisKline(pairsName, price, count, oneWeekTimestamp, oneWeek);
        /////////////////////
    }


    private void updateOrderBalanceA(Entrust activeEntrust, EntrustMarketBalance entrustBalance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + activeEntrust.getMember(), 5000, 50, 100);
        if (lock_coin) {
            BigDecimal price = entrustBalance.getPrice();
            BigDecimal count = entrustBalance.getCount();
            BigDecimal mulSumFee = price.multiply(count);
            // 卖
            if (activeEntrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                // 手续费
                BigDecimal tradeFeeActive = new BigDecimal("0");
                if (activeEntrust.getTradeRate() != null) {
                    tradeFeeActive = mulSumFee.multiply(activeEntrust.getTradeRate());
                } else {
                    tradeFeeActive = mulSumFee.multiply(new BigDecimal("0.003"));
                }
                if (activeEntrust.getTradeFee() != null) {
                    activeEntrust.setTradeFee(activeEntrust.getTradeFee().add(tradeFeeActive));
                } else {
                    activeEntrust.setTradeFee(tradeFeeActive);
                }
                QueryWrapper<Balance> wrapperActiveToken = new QueryWrapper<Balance>();
                wrapperActiveToken.eq("currency", activeEntrust.getTokenCur());
                wrapperActiveToken.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenCur = balanceMapper.selectOne(wrapperActiveToken);
                BigDecimal assetsBlockedBalance = balanceTokenCur.getAssetsBlockedBalance();

                BigDecimal subtract = balanceTokenCur.getAssetsBlockedBalance().subtract(count);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenCur.setAssetsBlockedBalance(subtract);

                log.info("观察点1==》"+balanceTokenCur);

                balanceMapper.updateById(balanceTokenCur);

                // 资金流水记录
                BigDecimal subtract1 = balanceTokenCur.getAssetsBlockedBalance().subtract(assetsBlockedBalance);
                if(subtract1.doubleValue() < 0){
                    subtract1 = assetsBlockedBalance;
                }
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getTokenCur(),39,1,assetsBlockedBalance,balanceTokenCur.getAssetsBlockedBalance(),subtract1);

                QueryWrapper<Balance> wrapperActiveMain = new QueryWrapper<Balance>();
                wrapperActiveMain.eq("currency", activeEntrust.getMainCur());
                wrapperActiveMain.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperActiveMain);
                //补齐差额
                BigDecimal marginPrice = activeEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();
                BigDecimal add1 = balanceTokenMain.getAssetsBalance().add(mulSumFee).subtract(tradeFeeActive).add(marginFee);
                balanceTokenMain.setAssetsBalance(add1);
                balanceMapper.updateById(balanceTokenMain);

                // 资金流水记录
                QueryWrapper<Balance> wrapperPassiveMain2 = new QueryWrapper<Balance>();
                wrapperPassiveMain2.eq("currency", activeEntrust.getMainCur());
                wrapperPassiveMain2.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenMain2 = balanceMapper.selectOne(wrapperPassiveMain2);
                BigDecimal subtract2 = balanceTokenMain2.getAssetsBalance().subtract(assetsBalance);
                if(subtract2.doubleValue() < 0){
                    subtract2 = balanceTokenMain.getAssetsBalance();
                }
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),40,2,assetsBalance,balanceTokenCur.getAssetsBalance(),subtract2);
            } else {
                // 手续费
                BigDecimal tradeFeeActive = new BigDecimal("0");
                if (activeEntrust.getTradeRate() != null) {
                    tradeFeeActive = count.multiply(activeEntrust.getTradeRate());
                } else {
                    tradeFeeActive = count.multiply(new BigDecimal("0.003"));
                }
                if (activeEntrust.getTradeFee() != null) {
                    activeEntrust.setTradeFee(activeEntrust.getTradeFee().add(tradeFeeActive));
                } else {
                    activeEntrust.setTradeFee(tradeFeeActive);
                }
                QueryWrapper<Balance> wrapperActiveMain = new QueryWrapper<Balance>();
                wrapperActiveMain.eq("currency", activeEntrust.getMainCur());
                wrapperActiveMain.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperActiveMain);
                if(null == balanceTokenMain){
                    log.info("居然查询不到数据，用户ID==>"+activeEntrust.getMember()+"币==》"+activeEntrust.getMainCur());
                    return;
                }
                BigDecimal assetsBlockedBalance = balanceTokenMain.getAssetsBlockedBalance();
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();

                BigDecimal blockBalanceActive = entrustBalance.getCount().multiply(activeEntrust.getPrice());
                balanceTokenMain.setAssetsBlockedBalance(balanceTokenMain.getAssetsBlockedBalance().subtract(blockBalanceActive));
                //补齐差额
                BigDecimal marginPrice = activeEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                balanceTokenMain.setAssetsBalance(balanceTokenMain.getAssetsBalance().add(marginFee));
                BigDecimal bigDecimal = balanceTokenMain.getAssetsBlockedBalance();

                if(bigDecimal.doubleValue() < 0 ){
                    bigDecimal = new BigDecimal("0");
                }
                balanceTokenMain.setAssetsBlockedBalance(bigDecimal);

                log.info("观测点6==》"+balanceTokenMain);
                balanceMapper.updateById(balanceTokenMain);

                // 资金流水记录
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),37,1,assetsBlockedBalance,balanceTokenMain.getAssetsBlockedBalance(),blockBalanceActive);
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),38,2,assetsBalance,balanceTokenMain.getAssetsBalance(),marginFee);

                QueryWrapper<Balance> wrapperActiveToken = new QueryWrapper<Balance>();
                wrapperActiveToken.eq("currency", activeEntrust.getTokenCur());
                wrapperActiveToken.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenToken = balanceMapper.selectOne(wrapperActiveToken);
                BigDecimal assetsBalance1 = balanceTokenToken.getAssetsBalance();

                BigDecimal subtract = balanceTokenToken.getAssetsBalance().add(count).subtract(tradeFeeActive);
                balanceTokenToken.setAssetsBalance(subtract);
                balanceMapper.updateById(balanceTokenToken);

                // 资金流水记录
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getTokenCur(),38,2,assetsBalance1,balanceTokenToken.getAssetsBalance(),subtract);

            }
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + activeEntrust.getMember());
        } else {
            updateOrderBalanceA(activeEntrust, entrustBalance);
        }

    }


    private void updateOrderBalanceP(Entrust passiveEntrust, EntrustMarketBalance entrustBalance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + passiveEntrust.getMember(), 5000, 50, 100);
        if (lock_coin) {
            BigDecimal price = entrustBalance.getPrice();
            BigDecimal count = entrustBalance.getCount();
            BigDecimal mulSumFee = price.multiply(count);
            // 卖
            if (passiveEntrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                // 手续费
                BigDecimal tradeFeeActive = new BigDecimal("0");
                if (passiveEntrust.getTradeRate() != null) {
                    tradeFeeActive = mulSumFee.multiply(passiveEntrust.getTradeRate());
                } else {
                    tradeFeeActive = mulSumFee.multiply(new BigDecimal("0.003"));
                }
                if (passiveEntrust.getTradeFee() != null) {
                    passiveEntrust.setTradeFee(passiveEntrust.getTradeFee().add(tradeFeeActive));
                } else {
                    passiveEntrust.setTradeFee(tradeFeeActive);
                }
                QueryWrapper<Balance> wrapperPassiveToken = new QueryWrapper<Balance>();
                wrapperPassiveToken.eq("currency", passiveEntrust.getTokenCur());
                wrapperPassiveToken.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenCur = balanceMapper.selectOne(wrapperPassiveToken);
                BigDecimal assetsBlockedBalance = balanceTokenCur.getAssetsBlockedBalance();

                balanceTokenCur.setAssetsBlockedBalance(balanceTokenCur.getAssetsBlockedBalance().subtract(count));

                balanceMapper.updateById(balanceTokenCur);

                // 资金流水记录
                BigDecimal subtract1 = balanceTokenCur.getAssetsBlockedBalance().subtract(assetsBlockedBalance);
                if(subtract1.doubleValue() < 0){
                    subtract1 = assetsBlockedBalance;
                }
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getTokenCur(),39,1,assetsBlockedBalance,balanceTokenCur.getAssetsBlockedBalance(),subtract1);

                QueryWrapper<Balance> wrapperPassiveMain = new QueryWrapper<Balance>();
                wrapperPassiveMain.eq("currency", passiveEntrust.getMainCur());
                wrapperPassiveMain.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperPassiveMain);
                // 补齐差额
                BigDecimal marginPrice = passiveEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                balanceTokenMain.setAssetsBalance(
                        balanceTokenMain.getAssetsBalance().add(mulSumFee).subtract(tradeFeeActive).add(marginFee));
                balanceMapper.updateById(balanceTokenMain);
                // 资金流水记录
                BigDecimal subtract2 = balanceTokenCur.getAssetsBlockedBalance().subtract(assetsBlockedBalance);
                if(subtract2.doubleValue() < 0){
                    subtract2 = assetsBlockedBalance;
                }
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getTokenCur(),39,1,assetsBlockedBalance,balanceTokenCur.getAssetsBlockedBalance(),subtract2);

            } else {
                // 手续费
                BigDecimal tradeFeeActive = new BigDecimal("0");
                if (passiveEntrust.getTradeRate() != null) {
                    tradeFeeActive = count.multiply(passiveEntrust.getTradeRate());
                } else {
                    tradeFeeActive = count.multiply(new BigDecimal("0.003"));
                }
                if (passiveEntrust.getTradeFee() != null) {
                    passiveEntrust.setTradeFee(passiveEntrust.getTradeFee().add(tradeFeeActive));
                } else {
                    passiveEntrust.setTradeFee(tradeFeeActive);
                }
                QueryWrapper<Balance> wrapperPassiveMain = new QueryWrapper<Balance>();
                wrapperPassiveMain.eq("currency", passiveEntrust.getMainCur());
                wrapperPassiveMain.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperPassiveMain);
                BigDecimal assetsBlockedBalance = balanceTokenMain.getAssetsBlockedBalance();

                BigDecimal blockBalanceActive = entrustBalance.getCount().multiply(passiveEntrust.getPrice());
                BigDecimal subtract = balanceTokenMain.getAssetsBlockedBalance().subtract(blockBalanceActive);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenMain.setAssetsBlockedBalance(subtract);
                // 补齐差额
                BigDecimal marginPrice = passiveEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();

                balanceTokenMain.setAssetsBalance(balanceTokenMain.getAssetsBalance().add(marginFee));
                balanceMapper.updateById(balanceTokenMain);

                // 资金流水记录
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getMainCur(),37,1,assetsBlockedBalance,balanceTokenMain.getAssetsBlockedBalance(),blockBalanceActive);
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getMainCur(),38,2,assetsBalance,balanceTokenMain.getAssetsBalance(),marginFee);


                QueryWrapper<Balance> wrapperActiveToken = new QueryWrapper<Balance>();
                wrapperActiveToken.eq("currency", passiveEntrust.getTokenCur());
                wrapperActiveToken.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenToken = balanceMapper.selectOne(wrapperActiveToken);

                BigDecimal assetsBalance1 = balanceTokenToken.getAssetsBalance();

                BigDecimal subtract1 = balanceTokenToken.getAssetsBalance().add(count).subtract(tradeFeeActive);
                balanceTokenToken.setAssetsBalance(subtract1);
                balanceMapper.updateById(balanceTokenToken);
                // 资金流水记录
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getTokenCur(),38,2,assetsBalance1,balanceTokenToken.getAssetsBalance(),subtract1);

            }
            if (passiveEntrust.getSurplusCount().compareTo(new BigDecimal("0")) == 0) {
                passiveEntrust.setState(EntrustConstant.Order_State.FINAL);
                EntrustHistory entrustHistory = new EntrustHistory();
                BeanUtils.copyProperties(passiveEntrust, entrustHistory);
                entrustHistoryMapper.insert(entrustHistory);
                entrustMapper.deleteById(passiveEntrust.getId());
            } else {
                passiveEntrust.setState(EntrustConstant.Order_State.MATCH);
                entrustMapper.updateById(passiveEntrust);
            }
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + passiveEntrust.getMember());
        } else {
            updateOrderBalanceP(passiveEntrust, entrustBalance);
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

    @Async
    public void redisKline(String pairsName, BigDecimal price, BigDecimal count, long timestamp, String time)
            throws ParseException {
        String key = CacheConstants.KLINE_KEY + time + CacheConstants.SPLIT + pairsName;
        Set<String> oneMinute = redisRepository.zsetRevRang(key, 0, 0);
        Iterator<String> oneMinuteIt = oneMinute.iterator();
        Kline kline = new Kline();
        if (oneMinuteIt.hasNext()) {
            String next = oneMinuteIt.next();
            JSONObject jsonObject = JSONObject.parseObject(next);
            Kline redisKline = JSONObject.toJavaObject(jsonObject, Kline.class);
            if (timestamp == redisKline.getTime()) {
                if (price.compareTo(redisKline.getHigh()) > 0) {
                    kline.setHigh(price);
                } else {
                    kline.setHigh(redisKline.getHigh());
                }
                if (price.compareTo(redisKline.getLow()) < 0) {
                    kline.setLow(price);
                } else {
                    kline.setLow(redisKline.getLow());
                }
                kline.setOpen(redisKline.getOpen());
                kline.setClose(price);
                kline.setTime(redisKline.getTime());
                kline.setVolume(redisKline.getVolume().add(count));
            } else {
                kline.setTime(timestamp);
                kline.setVolume(count);
                kline.setClose(price);
                kline.setLow(price);
                kline.setHigh(price);
                kline.setOpen(price);
            }
        } else {
            kline.setVolume(count);
            kline.setClose(price);
            kline.setLow(price);
            kline.setHigh(price);
            kline.setOpen(price);
            kline.setTime(timestamp);
        }
        redisRepository.zsetRmByScore(key, timestamp);
        Boolean bool = redisRepository.zsetAdd(key, JSONObject.toJSONString(kline), timestamp);
        if (!bool) {
            log.error("redis存储失败");
        }
    }
}
