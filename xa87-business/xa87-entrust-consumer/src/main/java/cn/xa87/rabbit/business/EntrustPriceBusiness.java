package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.model.Entrust;
import cn.xa87.model.Pairs;
import cn.xa87.rabbit.mapper.PairsMapper;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class EntrustPriceBusiness {
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;
    @Autowired
    private PairsMapper pairsMapper;

    public void execute(String msg) {
        String[] splitResult = msg.split("-");
        BigDecimal price = new BigDecimal(splitResult[0]);
        String pairsName = splitResult[1];
        QueryWrapper<Pairs> wrapperPairs = new QueryWrapper<Pairs>();
        wrapperPairs.eq("pairs_name", pairsName);
        Pairs pairs = pairsMapper.selectOne(wrapperPairs);
        // 以下是卖单
        Set<String> setSell = redisRepository.zsetRangByScore(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + pairsName, 0, price.doubleValue());
        for (String str : setSell) {
            Entrust entrustSetInfo = JSONObject.parseObject(str, Entrust.class);
            if (entrustSetInfo.getId().contains("robot")) {
                continue;
            }
            BigDecimal entrustCount = entrustSetInfo.getCount();
            Entrust entrust = new Entrust();
            entrust.setUld("UP");
            entrust.setId("robotIdEntrust");
            entrust.setCount(entrustCount);
            entrust.setPairsName(pairsName);
            entrust.setPrice(price);
            entrust.setEntrustType(EntrustConstant.Entrust_Type.BUY);
            entrust.setMember("robotMemberId");
            if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                rabbitMqProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
            } else {
                rabbitMqProducer.putProjectEntrustMatch(JSONObject.toJSONString(entrust));
            }
        }
        Set<String> setBuy = redisRepository.zsetRevRangByScore(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                        + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + pairsName, price.doubleValue(),
                Long.parseLong("99999999999"));
        for (String str : setBuy) {
            Entrust entrustSetInfo = JSONObject.parseObject(str, Entrust.class);
            if (entrustSetInfo.getId().contains("robot")) {
                continue;
            }
            BigDecimal entrustCount = entrustSetInfo.getCount();
            Entrust entrust = new Entrust();
            entrust.setUld("UP");
            entrust.setId("robotIdEntrust");
            entrust.setCount(entrustCount);
            entrust.setPairsName(pairsName);
            entrust.setPrice(price);
            entrust.setEntrustType(EntrustConstant.Entrust_Type.SELL);
            entrust.setMember("robotMemberId");
            if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                rabbitMqProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
            } else {
                rabbitMqProducer.putProjectEntrustMatch(JSONObject.toJSONString(entrust));
            }
        }

    }
}
