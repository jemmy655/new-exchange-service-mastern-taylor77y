package cn.xa87.rabbit.rabbitmq.producer;

import cn.xa87.common.constants.RabbitConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class RabbitMqProducer implements RabbitTemplate.ConfirmCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容RabbitConstants.
    }

    public void putMatch(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.MATCH_PUT, RabbitConstants.MATCH_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putBalance(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.BALANCE_PUT, RabbitConstants.BALANCE_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putMatchToContract(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.MATCH_TO_CONTRACT_PUT, RabbitConstants.MATCH_TO_CONTRACT_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putCurrency(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.CONSUME_CURRENCY_PUT, RabbitConstants.CONSUME_CURRENCY_ROUTINGKEY_PUT, content, correlationId);
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //这里是回调，根据业务编写，例如：分配骑手检测骑手消费者是否消费成功
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }


}