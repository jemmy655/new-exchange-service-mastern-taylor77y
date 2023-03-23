package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DateUtil;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.*;
import cn.xa87.rabbit.mapper.BalanceMapper;
import cn.xa87.rabbit.mapper.ContractMulMapper;
import cn.xa87.rabbit.mapper.ContractOrderMapper;
import cn.xa87.rabbit.mapper.WarehouseMapper;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class BalanceBusiness {

    @Autowired
    private ContractMulMapper contractMulMapper;
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    public void execute(String msg) throws ParseException {
        try {
            MatchBalance matchBalance = JSONObject.parseObject(msg, MatchBalance.class);
            QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
            wrapper.eq("pairs_name", matchBalance.getPairsName());
            ContractMul contractMul = contractMulMapper.selectOne(wrapper);
            BigDecimal price = matchBalance.getPrice();
            BigDecimal count = matchBalance.getCount();
            BigDecimal coinNum = contractMul.getContractMul().multiply(count);
            if (!matchBalance.getActiveWarehouse().contains("robot")) {
                ContractOrder activeOrder = contractOrderMapper.selectById(matchBalance.getActiveOrder());
                // 修改可用手数
                activeOrder.setIsContractHands(activeOrder.getContractHands().subtract(matchBalance.getCount()));
                if (activeOrder.getIsContractHands().doubleValue() == 0) {
                    activeOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
                }
                activeOrder.setCoinNum(activeOrder.getCoinNum().subtract(coinNum));
                contractOrderMapper.updateById(activeOrder);
                // 保证金退还
                BigDecimal passiveSumPrice = contractMul.getContractMul()
                        .multiply(matchBalance.getCount().multiply(activeOrder.getPrice()));
                BigDecimal activeMargin = passiveSumPrice.divide(activeOrder.getLeverNum(), 8, BigDecimal.ROUND_HALF_UP);
                // 手续费
                BigDecimal takeFee = new BigDecimal("0");
                // 手续费
                if (activeOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {

                    takeFee = matchBalance.getCount().multiply(contractMul.getContractMul())
                            .multiply(activeOrder.getPrice()).multiply(contractMul.getMakerFee());
                } else {
                    takeFee = matchBalance.getCount().multiply(contractMul.getContractMul())
                            .multiply(activeOrder.getPrice()).multiply(contractMul.getTakerFee());
                }
                BigDecimal priceResultUp = price.subtract(activeOrder.getPrice());
                BigDecimal priceResultDown = activeOrder.getPrice().subtract(price);
                BigDecimal matchFee = new BigDecimal("0");
                // 已实现盈亏
                if (activeOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {

                    matchFee = priceResultUp.multiply(matchBalance.getCount()).multiply(contractMul.getContractMul());
                } else {
                    matchFee = priceResultDown.multiply(matchBalance.getCount()).multiply(contractMul.getContractMul());
                }
                QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
                wrapperActive.eq("currency", activeOrder.getMainCur());
                wrapperActive.eq("user_id", activeOrder.getMember());
                Balance balanceActive = balanceMapper.selectOne(wrapperActive);
                balanceActive
                        .setAssetsBalance(balanceActive.getAssetsBalance().add(activeMargin).add(matchFee).subtract(takeFee));
                balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(activeMargin));
                updateBalance(balanceActive);

                // 删除组合订单
                Warehouse aWarehouse = warehouseMapper.selectById(matchBalance.getActiveWarehouse());
                BigDecimal ahands = aWarehouse.getHands().subtract(count);
                if (ahands.compareTo(new BigDecimal("0")) == 1) {
                    aWarehouse.setHands(ahands);
                    warehouseMapper.updateById(aWarehouse);
                    if (aWarehouse.getOrdPrice() != null) {
                        refOrdTigger(aWarehouse.getId(), aWarehouse.getOrdPrice(), TokenOrderConstant.Match_Type.ORD);
                    }
                    if (aWarehouse.getTriggerPrice() != null) {
                        refOrdTigger(aWarehouse.getId(), aWarehouse.getTriggerPrice(),
                                TokenOrderConstant.Match_Type.TRIGGER);
                    }
                } else {
                    warehouseMapper.deleteById(aWarehouse.getId());
                    remOrdTigger(aWarehouse.getPairsName(), aWarehouse.getId());
                }
            }
            if (!matchBalance.getPassiveWarehouse().contains("robot")) {
                // 被动
                ContractOrder passiveOrder = contractOrderMapper.selectById(matchBalance.getPassiveOrder());
                passiveOrder.setIsContractHands(passiveOrder.getContractHands().subtract(matchBalance.getCount()));
                if (passiveOrder.getIsContractHands().doubleValue() == 0) {
                    passiveOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
                }
                passiveOrder.setCoinNum(passiveOrder.getCoinNum().subtract(coinNum));
                contractOrderMapper.updateById(passiveOrder);
                // 保证金退还
                BigDecimal passiveSumPrice = contractMul.getContractMul()
                        .multiply(matchBalance.getCount().multiply(passiveOrder.getPrice()));
                BigDecimal activeMargin = passiveSumPrice.divide(passiveOrder.getLeverNum(), 8, BigDecimal.ROUND_HALF_UP);
                // 手续费
                BigDecimal takeFee = new BigDecimal("0");
                // 手续费
                if (passiveOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {

                    takeFee = matchBalance.getCount().multiply(contractMul.getContractMul())
                            .multiply(passiveOrder.getPrice()).multiply(contractMul.getMakerFee());
                } else {
                    takeFee = matchBalance.getCount().multiply(contractMul.getContractMul())
                            .multiply(passiveOrder.getPrice()).multiply(contractMul.getTakerFee());
                }
                BigDecimal priceResultUp = price.subtract(passiveOrder.getPrice());
                BigDecimal priceResultDown = passiveOrder.getPrice().subtract(price);
                BigDecimal matchFee = new BigDecimal("0");
                // 已实现盈亏
                if (passiveOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {

                    matchFee = priceResultUp.multiply(matchBalance.getCount()).multiply(contractMul.getContractMul());
                } else {
                    matchFee = priceResultDown.multiply(matchBalance.getCount()).multiply(contractMul.getContractMul());
                }
                QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
                wrapperActive.eq("currency", passiveOrder.getMainCur());
                wrapperActive.eq("user_id", passiveOrder.getMember());
                Balance balanceActive = balanceMapper.selectOne(wrapperActive);
                balanceActive
                        .setAssetsBalance(balanceActive.getAssetsBalance().add(activeMargin).add(matchFee).subtract(takeFee));
                balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(activeMargin));
                updateBalance(balanceActive);

                Warehouse pWarehouse = warehouseMapper.selectById(matchBalance.getPassiveWarehouse());
                if (pWarehouse != null) {
                    BigDecimal phands = pWarehouse.getHands().subtract(count);
                    if (phands.compareTo(new BigDecimal("0")) == 1) {
                        pWarehouse.setHands(phands);
                        warehouseMapper.updateById(pWarehouse);
                        if (pWarehouse.getOrdPrice() != null) {
                            refOrdTigger(pWarehouse.getId(), pWarehouse.getOrdPrice(), TokenOrderConstant.Match_Type.ORD);
                        }
                        if (pWarehouse.getTriggerPrice() != null) {
                            refOrdTigger(pWarehouse.getId(), pWarehouse.getTriggerPrice(),
                                    TokenOrderConstant.Match_Type.TRIGGER);
                        }
                    } else {
                        warehouseMapper.deleteById(pWarehouse.getId());
                        remOrdTigger(pWarehouse.getPairsName(), pWarehouse.getId());
                    }
                }

            }

            /// 添加Kline----
            // redis
            long timestamp = new Date().getTime();
            // 一分钟
            Long oneMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 1);
            String oneMinute = "1m";
            redisKline(matchBalance, price, count, oneMinuteTimestamp, oneMinute);
            // 五分钟
            Long fiveMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 5);
            String fiveMinute = "5m";
            redisKline(matchBalance, price, count, fiveMinuteTimestamp, fiveMinute);
            // 15分钟
            Long fifteenMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 15);
            String fifteenMinute = "15m";
            redisKline(matchBalance, price, count, fifteenMinuteTimestamp, fifteenMinute);
            // 30分钟
            Long thirtyMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 30);
            String thirtyMinute = "30m";
            redisKline(matchBalance, price, count, thirtyMinuteTimestamp, thirtyMinute);
            // 1小时
            Long oneHourTimestamp = DateUtil.hourTimestamp(timestamp);
            String oneHour = "1h";
            redisKline(matchBalance, price, count, oneHourTimestamp, oneHour);
            // 一天
            Long oneDayTimestamp = DateUtil.dayTimestamp(timestamp);
            String oneDay = "1d";
            redisKline(matchBalance, price, count, oneDayTimestamp, oneDay);
            // 一星期
            Long oneWeekTimestamp = DateUtil.weekTimestamp(timestamp, 1);
            String oneWeek = "1w";
            redisKline(matchBalance, price, count, oneWeekTimestamp, oneWeek);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Async
    public void redisKline(MatchBalance matchBalance, BigDecimal price, BigDecimal count, long timestamp, String time)
            throws ParseException {
        String pairsName = matchBalance.getPairsName();
        String key = CacheConstants.KLINE_KEY + time + CacheConstants.SPLIT + pairsName;
        Set<String> oneMinute = redisRepository.zsetRevRang(key, 0, 0);
        Iterator<String> oneMinuteIt = oneMinute.iterator();
        Kline kline = new Kline();
        if (oneMinuteIt.hasNext()) {
            String next = oneMinuteIt.next();
            JSONObject jsonObject = JSONObject.parseObject(next);
            Kline redisKline = JSONObject.toJavaObject(jsonObject, Kline.class);
            log.debug("kline:[{}]", kline);
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
        log.info("redis存储. key:[{}],value:[{}],score:[{}]", key, kline, timestamp);
        redisRepository.zsetRemoveRange(key, timestamp, timestamp);
        Boolean bool = redisRepository.zsetAdd(key, JSONObject.toJSONString(kline), timestamp);
        if (!bool) {
            log.error("redis存储失败");
        }
    }

    private void updateBalance(Balance balance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + balance.getCurrency()
                + CacheConstants.SPLIT + balance.getUserId(), 5000, 50, 100);
        if (lock_coin) {
            balanceMapper.updateById(balance);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + balance.getCurrency()
                    + CacheConstants.SPLIT + balance.getUserId());
        } else {
            updateBalance(balance);
        }
    }

    private void remOrdTigger(String pairsName, String id) {
        List<String> listOpenup = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY
                + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT + pairsName, id);
        for (String str : listOpenup) {
            redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                    + CacheConstants.SPLIT + pairsName, str);
        }

        List<String> listDown = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY
                + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + pairsName, id);
        for (String str : listDown) {
            redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN
                    + CacheConstants.SPLIT + pairsName, str);
        }
    }

    private void refOrdTigger(String id, BigDecimal price, TokenOrderConstant.Match_Type matchType) {
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("id", id);
        Warehouse warehouse = warehouseMapper.selectOne(wrapperWarehouse);
        List<String> listOpenup = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY
                + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT + warehouse.getPairsName(), id);
        for (String str : listOpenup) {
            redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                    + CacheConstants.SPLIT + warehouse.getPairsName(), str);
        }

        List<String> listDown = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY
                + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + warehouse.getPairsName(), id);
        for (String str : listDown) {
            redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN
                    + CacheConstants.SPLIT + warehouse.getPairsName(), str);
        }
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        wrapperOrder.eq("pairs_name", warehouse.getPairsName());
        wrapperOrder.eq("member", warehouse.getMember());
        wrapperOrder.eq("trade_type", warehouse.getTradeType());
        wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        for (ContractOrder contractOrder : contractOrders) {
            Match match = new Match();
            match.setWarehouse(id);
            match.setOrderId(contractOrder.getId());
            match.setPairsName(contractOrder.getPairsName());
            match.setPrice(price);
            match.setMemberId(contractOrder.getMember());
            match.setCount(contractOrder.getIsContractHands());
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                if (matchType.equals(TokenOrderConstant.Match_Type.TRIGGER)) {
                    match.setTradeType(ContractConstant.Trade_Type.OPEN_DOWN);
                } else {
                    match.setTradeType(ContractConstant.Trade_Type.OPEN_UP);
                }
            } else {
                if (matchType.equals(TokenOrderConstant.Match_Type.TRIGGER)) {
                    match.setTradeType(ContractConstant.Trade_Type.OPEN_UP);
                } else {
                    match.setTradeType(ContractConstant.Trade_Type.OPEN_DOWN);
                }
            }
            rabbitMqProducer.putMatch(JSONObject.toJSONString(match));
        }
    }
}
