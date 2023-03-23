package cn.xa87.data.product;

import cn.xa87.common.constants.RabbitConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class MatchProducer implements RabbitTemplate.ConfirmCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MatchProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    public void putMatch(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.MATCH_PUT, RabbitConstants.MATCH_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putCurrency(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.CONSUME_CURRENCY_PUT, RabbitConstants.CONSUME_CURRENCY_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putBrokerManage(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.BROKER_MANAGE_PUT, RabbitConstants.BROKER_MANAGE_ROUTINGKEY_PUT, content, correlationId);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }

}