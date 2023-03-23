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
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@Component
public class EntrustBalanceBusiness {

    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private EntrustMapper entrustMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private EntrustHistoryMapper entrustHistoryMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;

    public void execute(String msg) throws ParseException {
        try {
            EntrustBalance entrustBalance = JSONObject.parseObject(msg, EntrustBalance.class);
            String pairsName = entrustBalance.getPairsName();
            BigDecimal price = entrustBalance.getPrice();
            BigDecimal count = entrustBalance.getCount();
            // 修改资产if (!matchBalance.getPassiveWarehouse().contains("robot")) {
            if (!entrustBalance.getActiveOrder().contains("robot")) {
                log.info("成交信息:" + msg);
                // 主动方
                Entrust activeEntrust = entrustMapper.selectById(entrustBalance.getActiveOrder());
                if (activeEntrust == null) {
                    activeEntrust = new Entrust();
                    EntrustHistory entrustHistory = entrustHistoryMapper.selectById(entrustBalance.getActiveOrder());
                    BeanUtils.copyProperties(entrustHistory, activeEntrust);
                } else {
                    activeEntrust.setSurplusCount(activeEntrust.getSurplusCount().subtract(entrustBalance.getCount()));

                    if (!entrustBalance.getPassiveOrder().contains("robot")) {
                        Entrust passiveEntrust = entrustMapper.selectById(entrustBalance.getPassiveOrder());
                        if (passiveEntrust == null) {
                            EntrustHistory entrustHistory = entrustHistoryMapper.selectById(entrustBalance.getPassiveOrder());
                            if (entrustHistory != null) {
                                activeEntrust.setMatchMember(entrustHistory.getMember());
                            }
                        } else {
                            activeEntrust.setMatchMember(passiveEntrust.getMember());
                        }
                    } else {
                        activeEntrust.setMatchMember("robot");
                    }
                    activeEntrust.setMatchCount(activeEntrust.getMatchCount().add(entrustBalance.getCount()));
                    activeEntrust.setMatchPrice(entrustBalance.getPrice());
                    activeEntrust.setMethodType(EntrustConstant.Method_Type.ACTIVE);
                    activeEntrust.setMatchFee(activeEntrust.getMatchCount().multiply(activeEntrust.getMatchPrice()));

                    updateOrderBalanceA(activeEntrust, entrustBalance);
                }
            }
            if (!entrustBalance.getPassiveOrder().contains("robot")) {
                log.info("成交信息:" + msg);
                Entrust passiveEntrust = entrustMapper.selectById(entrustBalance.getPassiveOrder());
                if (passiveEntrust == null) {
                    passiveEntrust = new Entrust();
                    EntrustHistory entrustHistory = entrustHistoryMapper.selectById(entrustBalance.getPassiveOrder());
                    BeanUtils.copyProperties(entrustHistory, passiveEntrust);
                }
                // 被动方
                if (passiveEntrust != null) {
                    passiveEntrust.setSurplusCount(passiveEntrust.getSurplusCount().subtract(entrustBalance.getCount()));
                    // 主动方
                    if (!entrustBalance.getActiveOrder().contains("robot")) {
                        Entrust activeEntrust = entrustMapper.selectById(entrustBalance.getActiveOrder());
                        if (activeEntrust == null) {
                            EntrustHistory entrustHistory = entrustHistoryMapper.selectById(entrustBalance.getActiveOrder());
                            passiveEntrust.setMatchMember(entrustHistory.getMember());
                        } else {
                            passiveEntrust.setMatchMember(activeEntrust.getMember());
                        }

                    } else {
                        passiveEntrust.setMatchMember("robot");
                    }
                    passiveEntrust.setMatchCount(passiveEntrust.getMatchCount().add(entrustBalance.getCount()));
                    passiveEntrust.setMethodType(EntrustConstant.Method_Type.PASSIVE);
                    passiveEntrust.setMatchPrice(entrustBalance.getPrice());
                    passiveEntrust.setMatchFee(passiveEntrust.getMatchCount().multiply(passiveEntrust.getMatchPrice()));
                    updateOrderBalanceP(passiveEntrust, entrustBalance);
                }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        /////////////////////
    }

    private void updateOrderBalanceA(Entrust activeEntrust, EntrustBalance entrustBalance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + activeEntrust.getMember(), 10000, 50, 100);
        if (lock_coin) {
            BigDecimal price = entrustBalance.getPrice();
            BigDecimal count = entrustBalance.getCount();
            BigDecimal mulSumFee = price.multiply(count);
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
//                log.info("主动方代币原资产SELL:冻结:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBlockedBalance());
//                log.info("主动方代币原资产SELL:可用:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBalance());
                BigDecimal assetsBlockedBalance = balanceTokenCur.getAssetsBlockedBalance();
                BigDecimal subtract = balanceTokenCur.getAssetsBlockedBalance().subtract(count);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenCur.setAssetsBlockedBalance(subtract);
                log.info("观察点2==》"+balanceTokenCur);
                balanceMapper.updateById(balanceTokenCur);

                // 增加冻结记录
                BigDecimal subtract1 = balanceTokenCur.getAssetsBlockedBalance().subtract(assetsBlockedBalance);
                if(subtract1.doubleValue() < 0){
                    subtract1 = assetsBlockedBalance;
                }

                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getTokenCur(),39,1,assetsBlockedBalance,balanceTokenCur.getAssetsBlockedBalance(),subtract1);
//                log.info("主动方代币原资产SELL:更改后冻结:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBlockedBalance());
//                log.info("主动方代币原资产SELL:更改后可用:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBalance());
                QueryWrapper<Balance> wrapperActiveMain = new QueryWrapper<Balance>();
                wrapperActiveMain.eq("currency", activeEntrust.getMainCur());
                wrapperActiveMain.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperActiveMain);
//                log.info("主动方主币原资产SELL:冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
//                log.info("主动方主币原资产SELL:可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());

                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();
                //补齐差额
                BigDecimal marginPrice = activeEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                log.info("主动方主币差额SELL:原价格" + activeEntrust.getPrice() + "成交价格:" + price + "数量:" + count + "差额:" + marginFee + "手续费:" + tradeFeeActive + "更改额:" + mulSumFee);
                BigDecimal add = balanceTokenMain.getAssetsBalance().add(mulSumFee).subtract(tradeFeeActive).add(marginFee);
                balanceTokenMain.setAssetsBalance(add);
                balanceMapper.updateById(balanceTokenMain);

                // 增加收益记录
                QueryWrapper<Balance> wrapperPassiveMain2 = new QueryWrapper<Balance>();
                wrapperPassiveMain2.eq("currency", activeEntrust.getMainCur());
                wrapperPassiveMain2.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenMain2 = balanceMapper.selectOne(wrapperPassiveMain2);
                BigDecimal subtract2 = balanceTokenMain2.getAssetsBalance().subtract(assetsBalance);
                if(subtract2.doubleValue() < 0){
                    subtract2 = balanceTokenMain.getAssetsBalance();
                }
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),40,2,assetsBalance,balanceTokenMain.getAssetsBalance(),subtract2);
//                log.info("主动方主币原资产SELL:更改后冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
//                log.info("主动方主币原资产SELL:更改后可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());
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
//                log.info("主动方主币原资产BUY:冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
//                log.info("主动方主币原资产BUY:可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());
                BigDecimal blockBalanceActive = entrustBalance.getCount().multiply(activeEntrust.getPrice());
                log.info("主动方主币原资产BUY:解锁主币:数量" + entrustBalance.getCount() + "价格:" + activeEntrust.getPrice() + "计算结果:" + blockBalanceActive);

                BigDecimal assetsBlockedBalance = balanceTokenMain.getAssetsBlockedBalance();
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();

                BigDecimal subtract = balanceTokenMain.getAssetsBlockedBalance().subtract(blockBalanceActive);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenMain.setAssetsBlockedBalance(subtract);
                log.info("观察点3==》"+balanceTokenMain);
                //补齐差额
                BigDecimal marginPrice = activeEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                log.info("主动方主币差额BUY:原价格" + activeEntrust.getPrice() + "成交价格:" + price + "数量:" + count + "差额:" + marginFee + "手续费:" + tradeFeeActive);
                balanceTokenMain.setAssetsBalance(balanceTokenMain.getAssetsBalance().add(marginFee));
                balanceMapper.updateById(balanceTokenMain);
                // 减少冻结
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),37,1,assetsBlockedBalance,balanceTokenMain.getAssetsBlockedBalance(),blockBalanceActive);
                // 增加收益
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getMainCur(),38,2,assetsBalance,balanceTokenMain.getAssetsBalance(),marginFee);

//                log.info("主动方主币原资产BUY:更改后冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
//                log.info("主动方主币原资产BUY:更改后可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());

                QueryWrapper<Balance> wrapperActiveToken = new QueryWrapper<Balance>();
                wrapperActiveToken.eq("currency", activeEntrust.getTokenCur());
                wrapperActiveToken.eq("user_id", activeEntrust.getMember());
                Balance balanceTokenToken = balanceMapper.selectOne(wrapperActiveToken);
//                log.info("主动方代币原资产BUY:冻结:" + activeEntrust.getTokenCur() + "-" + balanceTokenToken.getAssetsBlockedBalance());
//                log.info("主动方代币原资产BUY:可用:" + activeEntrust.getTokenCur() + "-" + balanceTokenToken.getAssetsBalance());
//                log.info("主动方代币计算结果BUY:" + count + "-手续费:" + tradeFeeActive);

                BigDecimal assetsBalance1 = balanceTokenToken.getAssetsBalance();


                BigDecimal subtract1 = balanceTokenToken.getAssetsBalance().add(count).subtract(tradeFeeActive);
                balanceTokenToken.setAssetsBalance(subtract1);
                balanceMapper.updateById(balanceTokenToken);

                // 增加收益
                saveBalanceRecord(activeEntrust.getMember(),activeEntrust.getTokenCur(),38,2,assetsBalance1,balanceTokenToken.getAssetsBalance(),subtract1);

                log.info("主动方代币原资产BUY:更改后冻结:" + balanceTokenToken.getCurrency() + "-" + balanceTokenToken.getAssetsBlockedBalance());
                log.info("主动方代币原资产BUY:更改后可用:" + balanceTokenToken.getCurrency() + "-" + balanceTokenToken.getAssetsBalance());
            }
            if (activeEntrust.getSurplusCount().compareTo(new BigDecimal("0")) == 0) {
                activeEntrust.setState(EntrustConstant.Order_State.FINAL);
                EntrustHistory entrustHistory = new EntrustHistory();
                BeanUtils.copyProperties(activeEntrust, entrustHistory);
                entrustHistoryMapper.insert(entrustHistory);
                entrustMapper.deleteById(activeEntrust.getId());
            } else {
                activeEntrust.setState(EntrustConstant.Order_State.MATCH);
                entrustMapper.updateById(activeEntrust);
            }
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + activeEntrust.getMember());
        } else {
            updateOrderBalanceA(activeEntrust, entrustBalance);
        }

    }

    private void updateOrderBalanceP(Entrust passiveEntrust, EntrustBalance entrustBalance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + passiveEntrust.getMember(), 10000, 50, 100);
        if (lock_coin) {
            BigDecimal price = entrustBalance.getPrice();
            BigDecimal count = entrustBalance.getCount();
            BigDecimal mulSumFee = price.multiply(count);
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
                log.info("被动方代币原资产SELL:冻结:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBlockedBalance());
                log.info("被动方代币原资产SELL:可用:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBalance());

                BigDecimal assetsBlockedBalance = balanceTokenCur.getAssetsBlockedBalance();

                BigDecimal subtract = balanceTokenCur.getAssetsBlockedBalance().subtract(count);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenCur.setAssetsBlockedBalance(subtract);
                log.info("观察点4==》"+balanceTokenCur);
                balanceMapper.updateById(balanceTokenCur);

                // 减少冻结
                // 增加冻结记录
                BigDecimal subtract1 = balanceTokenCur.getAssetsBlockedBalance().subtract(assetsBlockedBalance);
                if(subtract1.doubleValue() < 0){
                    subtract1 = assetsBlockedBalance;
                }
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getTokenCur(),39,1,assetsBlockedBalance,balanceTokenCur.getAssetsBlockedBalance(),subtract1);

                log.info("被动方代币原资产SELL:更改后冻结:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBlockedBalance());
                log.info("被动方代币原资产SELL:更改后可用:" + balanceTokenCur.getCurrency() + "-" + balanceTokenCur.getAssetsBalance());
                QueryWrapper<Balance> wrapperPassiveMain = new QueryWrapper<Balance>();
                wrapperPassiveMain.eq("currency", passiveEntrust.getMainCur());
                wrapperPassiveMain.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenMain = balanceMapper.selectOne(wrapperPassiveMain);
                log.info("被动方主币原资产SELL:冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
                log.info("被动方主币原资产SELL:可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();
                //补齐差额
                BigDecimal marginPrice = passiveEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                log.info("被被动方主币差额BUY:原价格" + passiveEntrust.getPrice() + "成交价格:" + price + "数量:" + count + "差额:" + marginFee + "手续费:" + tradeFeeActive);
                BigDecimal add2 = balanceTokenMain.getAssetsBalance().add(mulSumFee).subtract(tradeFeeActive).add(marginFee);
                balanceTokenMain.setAssetsBalance(add2);
                balanceMapper.updateById(balanceTokenMain);

                // 增加收益
                QueryWrapper<Balance> wrapperPassiveMain2 = new QueryWrapper<Balance>();
                wrapperPassiveMain2.eq("currency", passiveEntrust.getMainCur());
                wrapperPassiveMain2.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenMain2 = balanceMapper.selectOne(wrapperPassiveMain2);
                BigDecimal subtract2 = balanceTokenMain2.getAssetsBalance().subtract(assetsBalance);
                if(subtract2.doubleValue() < 0){
                    subtract2 = balanceTokenMain.getAssetsBalance();
                }
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getMainCur(),40,2,assetsBalance,balanceTokenMain.getAssetsBalance(),subtract2);

                log.info("被动方主币原资产SELL:更改后冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
                log.info("被动方主币原资产SELL:更改后可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());
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
                log.info("被动方主币原资产BUY:冻结:" + passiveEntrust.getMainCur() + "-" + balanceTokenMain.getAssetsBlockedBalance());
                log.info("被动方主币原资产BUY:可用:" + passiveEntrust.getMainCur() + "-" + balanceTokenMain.getAssetsBalance());
                BigDecimal blockBalanceActive = entrustBalance.getCount().multiply(passiveEntrust.getPrice());

                BigDecimal assetsBlockedBalance = balanceTokenMain.getAssetsBlockedBalance();
                BigDecimal assetsBalance = balanceTokenMain.getAssetsBalance();

                BigDecimal subtract = balanceTokenMain.getAssetsBlockedBalance().subtract(blockBalanceActive);
                if(subtract.doubleValue() < 0){
                    subtract = new BigDecimal("0");
                }
                balanceTokenMain.setAssetsBlockedBalance(subtract);
                log.info("观察点5==》"+balanceTokenMain);
                //补齐差额
                BigDecimal marginPrice = passiveEntrust.getPrice().subtract(price);
                BigDecimal marginFee = marginPrice.multiply(count);
                log.info("被动方主币差额BUY:原价格" + passiveEntrust.getPrice() + "成交价格:" + price + "数量:" + count + "差额:" + marginFee + "手续费:" + tradeFeeActive);
                balanceTokenMain.setAssetsBalance(balanceTokenMain.getAssetsBalance().add(marginFee));
                balanceMapper.updateById(balanceTokenMain);

                // 减少冻结
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getMainCur(),37,1,assetsBlockedBalance,balanceTokenMain.getAssetsBlockedBalance(),subtract);
                // 增加收益
                saveBalanceRecord(passiveEntrust.getMember(),passiveEntrust.getMainCur(),38,2,assetsBalance,balanceTokenMain.getAssetsBalance(),marginFee);

                log.info("被动方主币原资产BUY:更改后冻结:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBlockedBalance());
                log.info("被动方主币原资产BUY:更改后可用:" + balanceTokenMain.getCurrency() + "-" + balanceTokenMain.getAssetsBalance());

                QueryWrapper<Balance> wrapperActiveToken = new QueryWrapper<Balance>();
                wrapperActiveToken.eq("currency", passiveEntrust.getTokenCur());
                wrapperActiveToken.eq("user_id", passiveEntrust.getMember());
                Balance balanceTokenToken = balanceMapper.selectOne(wrapperActiveToken);
                log.info("被动方代币计算结果BUY:" + count + "-手续费:" + tradeFeeActive);

                BigDecimal assetsBalance1 = balanceTokenToken.getAssetsBalance();

                BigDecimal subtract1 = balanceTokenToken.getAssetsBalance().add(count).subtract(tradeFeeActive);
                balanceTokenToken.setAssetsBalance(subtract1);
                balanceMapper.updateById(balanceTokenToken);
                log.info("被动方代币原资产BUY:更改后冻结:" + balanceTokenToken.getCurrency() + "-" + balanceTokenToken.getAssetsBlockedBalance());
                log.info("被动方代币原资产BUY:更改后可用:" + balanceTokenToken.getCurrency() + "-" + balanceTokenToken.getAssetsBalance());

                // 增加收益
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
