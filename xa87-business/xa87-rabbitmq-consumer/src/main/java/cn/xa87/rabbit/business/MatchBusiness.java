package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.ContractOrder;
import cn.xa87.model.Match;
import cn.xa87.model.MatchBalance;
import cn.xa87.model.MatchResult;
import cn.xa87.rabbit.mapper.ContractOrderMapper;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class MatchBusiness {

    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    public void execute(String msg) {
        try {
            Match match = JSONObject.parseObject(msg, Match.class);
            Set<String> set = new HashSet<String>();
            Set<String> set_custom = new HashSet<String>();
            ContractConstant.Trade_Type trade_Type = null;
            if (match.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                trade_Type = ContractConstant.Trade_Type.OPEN_DOWN;
                Set<String> set_open_up = redisRepository
                        .zsetRangByScore(
                                CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                                        + CacheConstants.SPLIT + match.getPairsName(),
                                match.getPrice().doubleValue(), match.getPrice().doubleValue());
                if (!set_open_up.isEmpty()) {
                    Match match_open_up = JSONObject.parseObject(set_open_up.iterator().next(), Match.class);
                    match.setSort(match_open_up.getSort() - 1);
                } else {
                    match.setSort((long) 9999999999f);
                }
                set.addAll(redisRepository.zsetRangByScore(CacheConstants.TOKEN_ORDER_MATCH_KEY
                                + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + match.getPairsName(), 0,
                        match.getPrice().doubleValue()));
                set_custom.addAll(redisRepository.zsetRangByScore(CacheConstants.TOKEN_ORDER_CUSTOM_KEY
                                + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + match.getPairsName(), 0,
                        match.getPrice().doubleValue()));
            } else {
                trade_Type = ContractConstant.Trade_Type.OPEN_UP;
                match.setSort(System.currentTimeMillis());
                set.addAll(
                        redisRepository.zsetRevRangByScore(
                                CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                                        + CacheConstants.SPLIT + match.getPairsName(),
                                match.getPrice().doubleValue(), Long.parseLong("99999999999")));
                set_custom
                        .addAll(redisRepository.zsetRevRangByScore(
                                CacheConstants.TOKEN_ORDER_CUSTOM_KEY + ContractConstant.Trade_Type.OPEN_UP
                                        + CacheConstants.SPLIT + match.getPairsName(),
                                match.getPrice().doubleValue(), Long.parseLong("99999999999")));
            }
            if (set.isEmpty()) {
                redisRepository.zsetAdd(CacheConstants.TOKEN_ORDER_MATCH_KEY + match.getTradeType() + CacheConstants.SPLIT
                        + match.getPairsName(), JSONObject.toJSONString(match), match.getPrice().doubleValue());
                // 修改普通委托单到持仓阶段
                senMatchToContract(set_custom, match.getPrice());
            } else {
                // 进入撮合阶段
                matchOrder(set, match, trade_Type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void senMatchToContract(Set<String> set_custom, BigDecimal price) {
        if (!set_custom.isEmpty()) {
            for (String str : set_custom) {
                ContractOrder contractOrder = JSONObject.parseObject(str, ContractOrder.class);
                redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_CUSTOM_KEY + contractOrder.getTradeType()
                        + CacheConstants.SPLIT + contractOrder.getPairsName(), str);
                contractOrder.setOrderType(TokenOrderConstant.Order_Type.POSITIONS);
                //contractOrder.setPriceType(ContractConstant.Price_Type.MARKET_PRICE);
                contractOrder.setPrice(price);
                contractOrderMapper.updateById(contractOrder);
                rabbitMqProducer.putMatchToContract(JSONObject.toJSONString(contractOrder));
            }
        }
    }


    public void matchOrder(Set<String> set, Match match, ContractConstant.Trade_Type trade_Type) {
        BigDecimal subTotal = match.getCount();
        for (String str : set) {
            if (subTotal.doubleValue() <= 0) {
                break;
            }
            Match matchSetInfo = JSONObject.parseObject(str, Match.class);
            BigDecimal count = matchSetInfo.getCount();
            BigDecimal price = matchSetInfo.getPrice();
            int result = count.compareTo(subTotal);
            if (result == 1) {
                redisRepository.zsetRemove(
                        CacheConstants.TOKEN_ORDER_MATCH_KEY + trade_Type + CacheConstants.SPLIT + match.getPairsName(),
                        str);
                matchSetInfo.setCount(count.subtract(subTotal));
                redisRepository.zsetAdd(
                        CacheConstants.TOKEN_ORDER_MATCH_KEY + trade_Type + CacheConstants.SPLIT + match.getPairsName(),
                        JSONObject.toJSONString(matchSetInfo), matchSetInfo.getPrice().doubleValue());
                setRedis(match.getPairsName(), subTotal, price, match.getTradeType());
                subTotal = new BigDecimal("0");
                matchResult(match.getPrice(), subTotal, match.getPairsName(), match.getOrderId(), matchSetInfo.getOrderId(), match.getWarehouse(), matchSetInfo.getWarehouse(), match.getMatchType(), matchSetInfo.getMatchType());
                break;
            }
            if (result == 0) {
                redisRepository.zsetRemove(
                        CacheConstants.TOKEN_ORDER_MATCH_KEY + trade_Type + CacheConstants.SPLIT + match.getPairsName(),
                        str);
                setRedis(match.getPairsName(), subTotal, price, match.getTradeType());
                matchResult(match.getPrice(), subTotal, match.getPairsName(), match.getOrderId(), matchSetInfo.getOrderId(), match.getWarehouse(), matchSetInfo.getWarehouse(), match.getMatchType(), matchSetInfo.getMatchType());
                subTotal = new BigDecimal("0");
                break;
            }
            if (result == -1) {
                subTotal = subTotal.subtract(count);
                redisRepository.zsetRemove(
                        CacheConstants.TOKEN_ORDER_MATCH_KEY + trade_Type + CacheConstants.SPLIT + match.getPairsName(),
                        str);
                setRedis(match.getPairsName(), count, price, match.getTradeType());
                matchResult(match.getPrice(), count, match.getPairsName(), match.getOrderId(), matchSetInfo.getOrderId(), match.getWarehouse(), matchSetInfo.getWarehouse(), match.getMatchType(), matchSetInfo.getMatchType());
            }
        }
        if (subTotal.doubleValue() > 0f) {
            match.setCount(subTotal);
            this.execute(JSONObject.toJSONString(match));
        }
    }

    // 撮合结果进行资产更新，扣除手续费
    private void matchResult(BigDecimal price, BigDecimal count, String pairsName, String activeOrder, String passiveOrder, String activeWarehouse, String passiveWarehouse, TokenOrderConstant.Match_Type activeMatchType, TokenOrderConstant.Match_Type passiveMatchType) {
        MatchBalance matchBalance = new MatchBalance(price, count, pairsName, activeOrder, passiveOrder, activeWarehouse, passiveWarehouse, activeMatchType, passiveMatchType);
        rabbitMqProducer.putBalance(JSONObject.toJSONString(matchBalance));
    }

    private void setRedis(String pairsName, BigDecimal count, BigDecimal price, ContractConstant.Trade_Type tradeType) {
        MatchResult matchResult = new MatchResult(pairsName, count, price, tradeType);
        redisRepository.zsetAdd(CacheConstants.TOKEN_ORDER_END_KEY + pairsName, JSONObject.toJSONString(matchResult),
                System.currentTimeMillis());
    }
}
