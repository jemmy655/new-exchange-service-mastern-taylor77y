package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.model.*;
import cn.xa87.rabbit.mapper.BalanceMapper;
import cn.xa87.rabbit.mapper.BalanceRecordMapper;
import cn.xa87.rabbit.mapper.EntrustHistoryMapper;
import cn.xa87.rabbit.mapper.EntrustMapper;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class EntrustProjectBusiness {
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private EntrustMapper entrustMapper;
    @Autowired
    private EntrustHistoryMapper entrustHistoryMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;

    public void execute(String msg) {
        Entrust entrust = JSONObject.parseObject(msg, Entrust.class);
        if (entrust.getUld().equals("UP")) {
            Set<String> set = null;
            EntrustConstant.Entrust_Type entrust_Type = null;
            if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                entrust_Type = EntrustConstant.Entrust_Type.SELL;
                Set<String> set_buy = redisRepository.zsetRangByScore(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        entrust.getPrice().doubleValue(), entrust.getPrice().doubleValue());
                if (!set_buy.isEmpty()) {
                    Entrust entrust_buy = JSONObject.parseObject(set_buy.iterator().next(), Entrust.class);
                    entrust.setSort(entrust_buy.getSort() - 1);
                } else {
                    entrust.setSort((long) 9999999999f);
                }
                set = redisRepository
                        .zsetRangByScore(
                                CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL
                                        + CacheConstants.SPLIT + entrust.getPairsName(),
                                0, entrust.getPrice().doubleValue());
            } else {
                entrust_Type = EntrustConstant.Entrust_Type.BUY;
                entrust.setSort(System.currentTimeMillis());
                set = redisRepository.zsetRevRangByScore(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        entrust.getPrice().doubleValue(), Long.parseLong("99999999999"));
            }
            if (set.isEmpty()) {
                redisRepository.zsetAdd(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType() + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        JSONObject.toJSONString(entrust), entrust.getPrice().doubleValue());
            } else {
                // 进入撮合阶段
                matchOrder(set, entrust, entrust_Type);
            }
        }
        if (entrust.getUld().equals("DOWN")) {
            this.closeEntrust(entrust);
        }
        if (entrust.getUld().equals("MARKET")) {
            Set<String> set = null;
            EntrustConstant.Entrust_Type entrust_Type = null;
            if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                entrust_Type = EntrustConstant.Entrust_Type.SELL;
                set = redisRepository.zsetRang(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                        + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + entrust.getPairsName(), 0, -1);
                if (set.isEmpty()) {
                    Set<String> set_buy = redisRepository.zsetRangByScore(
                            CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY
                                    + CacheConstants.SPLIT + entrust.getPairsName(),
                            entrust.getPrice().doubleValue(), entrust.getPrice().doubleValue());
                    if (!set_buy.isEmpty()) {
                        Entrust entrust_buy = JSONObject.parseObject(set_buy.iterator().next(), Entrust.class);
                        entrust.setSort(entrust_buy.getSort() - 1);
                    } else {
                        entrust.setSort((long) 9999999999f);
                    }
                    redisRepository.zsetAdd(
                            CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType() + CacheConstants.SPLIT
                                    + entrust.getPairsName(),
                            JSONObject.toJSONString(entrust), entrust.getPrice().doubleValue());
                } else {
                    matchOrderMarketBuy(set, entrust, entrust_Type);
                }
            } else {
                entrust_Type = EntrustConstant.Entrust_Type.BUY;
                entrust.setSort(System.currentTimeMillis());
                set = redisRepository.zsetRevRang(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        0, -1);
                if (set.isEmpty()) {
                    redisRepository.zsetAdd(
                            CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType() + CacheConstants.SPLIT
                                    + entrust.getPairsName(),
                            JSONObject.toJSONString(entrust), entrust.getPrice().doubleValue());
                    entrust.setSurplusCount(entrust.getCount());
                    entrust.setState(EntrustConstant.Order_State.CREATE);
                    entrustMapper.insert(entrust);
                } else {
                    // 进入撮合阶段
                    matchOrderMarketSell(set, entrust, entrust_Type);
                }
            }

        }
    }

    public void matchOrderMarketBuy(Set<String> set, Entrust entrust, EntrustConstant.Entrust_Type entrust_Type) {
        BigDecimal matchFee = entrust.getMatchFee();
        for (String str : set) {
            Entrust entrustSetInfo = JSONObject.parseObject(str, Entrust.class);
            BigDecimal count = entrustSetInfo.getCount();
            BigDecimal price = entrustSetInfo.getPrice();
            BigDecimal matchFeeInfo = count.multiply(price);
            int result = matchFeeInfo.compareTo(matchFee);
            if (result == 1) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal surplusMatchFee = matchFeeInfo.subtract(matchFee);
                BigDecimal surplusCount = surplusMatchFee.divide(price, 8, BigDecimal.ROUND_HALF_UP);
                entrustSetInfo.setCount(surplusCount);
                redisRepository.zsetAdd(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        JSONObject.toJSONString(entrustSetInfo), entrustSetInfo.getPrice().doubleValue());
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrust.getPairsName(), count.subtract(surplusCount), matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, count.subtract(surplusCount), entrust.getPairsName(), entrust,
                        entrustSetInfo.getId());
                matchFee = new BigDecimal("0");
                break;
            }
            if (result == 0) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrust.getPairsName(), count, matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, count, entrust.getPairsName(), entrust, entrustSetInfo.getId());
                matchFee = new BigDecimal("0");
                break;
            }
            if (result == -1) {
                matchFee = matchFee.subtract(matchFeeInfo);
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrustSetInfo.getPairsName(), count, matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, count, entrust.getPairsName(), entrust, entrustSetInfo.getId());
            }
        }
        if (matchFee.doubleValue() > 0f) {
            String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + entrust.getPairsName());
            JSONObject jsonInfo = JSONObject.parseObject(result);
            BigDecimal price = jsonInfo.getBigDecimal("nowPrice");
            entrust.setCount(matchFee.divide(price, 8, BigDecimal.ROUND_HALF_UP));
            entrust.setSurplusCount(matchFee.divide(price, 8, BigDecimal.ROUND_HALF_UP));
            entrust.setState(EntrustConstant.Order_State.CREATE);
            entrustMapper.insert(entrust);

            Set<String> set_buy = redisRepository.zsetRangByScore(
                    CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY
                            + CacheConstants.SPLIT + entrust.getPairsName(),
                    price.doubleValue(), price.doubleValue());
            if (!set_buy.isEmpty()) {
                Entrust entrust_buy = JSONObject.parseObject(set_buy.iterator().next(), Entrust.class);
                entrust.setSort(entrust_buy.getSort() - 1);
            } else {
                entrust.setSort((long) 9999999999f);
            }
            redisRepository.zsetAdd(
                    CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType() + CacheConstants.SPLIT
                            + entrust.getPairsName(),
                    JSONObject.toJSONString(entrust), entrust.getPrice().doubleValue());
        }
    }

    public void matchOrderMarketSell(Set<String> set, Entrust entrust, EntrustConstant.Entrust_Type entrust_Type) {
        BigDecimal subTotal = entrust.getCount();
        for (String str : set) {
            if (subTotal.doubleValue() <= 0) {
                break;
            }
            Entrust entrustSetInfo = JSONObject.parseObject(str, Entrust.class);
            BigDecimal count = entrustSetInfo.getCount();
            BigDecimal price = entrustSetInfo.getPrice();
            int result = count.compareTo(subTotal);
            if (result == 1) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                entrustSetInfo.setCount(count.subtract(subTotal));
                redisRepository.zsetAdd(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        JSONObject.toJSONString(entrustSetInfo), entrustSetInfo.getPrice().doubleValue());
                BigDecimal matchPrice = price;
                setRedis(entrust.getPairsName(), subTotal, matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, subTotal, entrust.getPairsName(), entrust, entrustSetInfo.getId());
                subTotal = new BigDecimal("0");
                break;
            }
            if (result == 0) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                setRedis(entrust.getPairsName(), subTotal, matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, subTotal, entrust.getPairsName(), entrust, entrustSetInfo.getId());
                subTotal = new BigDecimal("0");
                break;
            }
            if (result == -1) {
                subTotal = subTotal.subtract(count);
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                setRedis(entrustSetInfo.getPairsName(), count, matchPrice, entrust.getEntrustType());
                matchMarKetResult(matchPrice, count, entrust.getPairsName(), entrust, entrustSetInfo.getId());
            }
        }
        if (subTotal.doubleValue() > 0f) {
            redisRepository.zsetAdd(
                    CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType() + CacheConstants.SPLIT
                            + entrust.getPairsName(),
                    JSONObject.toJSONString(entrust), entrust.getPrice().doubleValue());
            entrust.setCount(subTotal);
            entrust.setSurplusCount(subTotal);
            entrust.setState(EntrustConstant.Order_State.CREATE);
            entrustMapper.insert(entrust);
        }
    }

    public void matchOrder(Set<String> set, Entrust entrust, EntrustConstant.Entrust_Type entrust_Type) {
        BigDecimal subTotal = entrust.getCount();
        for (String str : set) {
            if (subTotal.doubleValue() <= 0) {
                break;
            }
            Entrust entrustSetInfo = JSONObject.parseObject(str, Entrust.class);
            BigDecimal count = entrustSetInfo.getCount();
            BigDecimal price = entrustSetInfo.getPrice();
            int result = count.compareTo(subTotal);
            if (result == 1) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                entrustSetInfo.setCount(count.subtract(subTotal));
                redisRepository.zsetAdd(
                        CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                                + entrust.getPairsName(),
                        JSONObject.toJSONString(entrustSetInfo), entrustSetInfo.getPrice().doubleValue());
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrust.getPairsName(), subTotal, matchPrice, entrust.getEntrustType());
                matchResult(matchPrice, subTotal, entrust.getPairsName(), entrust.getId(), entrustSetInfo.getId());
                subTotal = new BigDecimal("0");
                break;
            }
            if (result == 0) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrust.getPairsName(), subTotal, matchPrice, entrust.getEntrustType());
                matchResult(matchPrice, subTotal, entrust.getPairsName(), entrust.getId(), entrustSetInfo.getId());
                subTotal = new BigDecimal("0");
                break;
            }
            if (result == -1) {
                subTotal = subTotal.subtract(count);
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust_Type + CacheConstants.SPLIT
                        + entrust.getPairsName(), str);
                BigDecimal matchPrice = price;
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                    matchPrice = entrust.getPrice();
                }
                setRedis(entrustSetInfo.getPairsName(), count, matchPrice, entrust.getEntrustType());
                matchResult(matchPrice, count, entrust.getPairsName(), entrust.getId(), entrustSetInfo.getId());
            }
        }
        if (subTotal.doubleValue() > 0f) {
            entrust.setCount(subTotal);
            this.execute(JSONObject.toJSONString(entrust));
        }
    }

    // 撮合结果进行资产更新，扣除手续费
    private void matchResult(BigDecimal price, BigDecimal count, String pairsName, String activeOrder,
                             String passiveOrder) {
        EntrustBalance entrustBalance = new EntrustBalance(price, count, pairsName, activeOrder, passiveOrder);
        rabbitMqProducer.putEntrustBalance(JSONObject.toJSONString(entrustBalance));
    }

    // 撮合结果进行资产更新，扣除手续费
    private void matchMarKetResult(BigDecimal price, BigDecimal count, String pairsName, Entrust activeOrder,
                                   String passiveOrder) {
        EntrustMarketBalance entrustBalance = new EntrustMarketBalance(price, count, pairsName, activeOrder, passiveOrder);
        rabbitMqProducer.putEntrustMarketBalance(JSONObject.toJSONString(entrustBalance));
    }

    private void setRedis(String pairsName, BigDecimal count, BigDecimal price,
                          EntrustConstant.Entrust_Type entrust_Type) {
        EntrustResult entrustResult = new EntrustResult(pairsName, count, price, entrust_Type);
        redisRepository.zsetAdd(CacheConstants.ENTRUST_ORDER_END_KEY + pairsName,
                JSONObject.toJSONString(entrustResult), System.currentTimeMillis());
    }

    private void closeEntrust(Entrust entrust) {
        List<String> list = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                + entrust.getEntrustType() + CacheConstants.SPLIT + entrust.getPairsName(), entrust.getId());
        if (!list.isEmpty()) {
            for (String str : list) {
                redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrust.getEntrustType()
                        + CacheConstants.SPLIT + entrust.getPairsName(), str);
                entrust.setState(EntrustConstant.Order_State.CLOSE);
                BigDecimal count = entrust.getCount().subtract(entrust.getSurplusCount());
                if (entrust.getMatchPrice() != null) {
                    entrust.setMatchFee(count.multiply(entrust.getMatchPrice()));
                } else {
                    entrust.setMatchFee(new BigDecimal("0"));
                }
                EntrustHistory entrustHistory = new EntrustHistory();
                BeanUtils.copyProperties(entrust, entrustHistory);
                entrustHistoryMapper.insert(entrustHistory);
                entrustMapper.deleteById(entrust.getId());
                closeBalance(entrust);
            }
        }
    }

    private void closeBalance(Entrust entrust) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember(),
                5000, 50, 100);
        if (lock_coin) {
            // 回归用户资产
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
            switch (entrust.getEntrustType().getType()) {
                case "BUY":
                    wrapperActive.eq("currency", entrust.getMainCur());
                    break;
                case "SELL":
                    wrapperActive.eq("currency", entrust.getTokenCur());
                    break;
            }
            wrapperActive.eq("user_id", entrust.getMember());
            Balance balance = balanceMapper.selectOne(wrapperActive);
            BigDecimal blockBalanceActive = new BigDecimal("0");
            BigDecimal assetsBalance = balance.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balance.getAssetsBlockedBalance();
            if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                blockBalanceActive = entrust.getSurplusCount();
            } else {
                blockBalanceActive = entrust.getSurplusCount().multiply(entrust.getPrice());
            }
            BigDecimal blockBalance = balance.getAssetsBlockedBalance().subtract(blockBalanceActive);
            balance.setAssetsBalance(balance.getAssetsBalance().add(blockBalanceActive));
            if (blockBalance.compareTo(new BigDecimal("0")) == -1) {
                balance.setAssetsBlockedBalance(new BigDecimal("0"));
            } else {
                balance.setAssetsBlockedBalance(blockBalance);
            }

            balanceMapper.updateById(balance);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());

            int balanceType = 0;
            int balanceType2 = 0;
            String currency = "";
            // 卖出
            if(entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)){
                balanceType = 35;
                balanceType2 = 36;
                currency = entrust.getTokenCur();
            }else {
                balanceType = 32;
                balanceType2 = 33;
                currency = entrust.getMainCur();
            }
            // 保存资金流水记录（增加）
            saveBalanceRecord(entrust.getMember(),currency,balanceType,2,assetsBalance,balance.getAssetsBalance(),blockBalanceActive);
            // 保存资金流水记录(减少)
            saveBalanceRecord(entrust.getMember(),currency,balanceType2,1,assetsBlockedBalance,balance.getAssetsBlockedBalance(),blockBalance);

        } else {
            closeBalance(entrust);
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
