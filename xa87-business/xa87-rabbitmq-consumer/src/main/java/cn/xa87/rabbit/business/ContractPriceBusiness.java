package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.ContractOrder;
import cn.xa87.rabbit.mapper.ContractOrderMapper;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class ContractPriceBusiness {
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Async
    public void execute(String msg) {
        try {
            String[] splitResult = msg.split("-");
            BigDecimal price = new BigDecimal(splitResult[0]);
            String pairsName = splitResult[1];
            Set<String> set_custom = new HashSet<String>();
            set_custom.addAll(redisRepository.zsetRangByScore(CacheConstants.TOKEN_ORDER_CUSTOM_KEY
                            + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + pairsName, 0,
                    price.doubleValue()));
            set_custom
                    .addAll(redisRepository.zsetRangByScore(
                            CacheConstants.TOKEN_ORDER_CUSTOM_KEY + ContractConstant.Trade_Type.OPEN_UP
                                    + CacheConstants.SPLIT + pairsName,
                            price.doubleValue(), Long.parseLong("99999999999")));
            // 修改普通委托单到持仓阶段
            senMatchToContract(set_custom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void senMatchToContract(Set<String> set_custom) {
        if (!set_custom.isEmpty()) {
            for (String str : set_custom) {
                ContractOrder contractOrder = JSONObject.parseObject(str, ContractOrder.class);
                redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_CUSTOM_KEY + contractOrder.getTradeType()
                        + CacheConstants.SPLIT + contractOrder.getPairsName(), str);
                contractOrder.setOrderType(TokenOrderConstant.Order_Type.POSITIONS);
                contractOrderMapper.updateById(contractOrder);
                rabbitMqProducer.putMatchToContract(JSONObject.toJSONString(contractOrder));
            }
        }
    }
}
