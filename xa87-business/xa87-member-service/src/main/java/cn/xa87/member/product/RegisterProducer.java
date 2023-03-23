package cn.xa87.member.product;

import cn.xa87.common.constants.RabbitConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class RegisterProducer implements RabbitTemplate.ConfirmCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RegisterProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    public void putRegister(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.REGISTER_PUT, RabbitConstants.REGISTER_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putBrokerManage(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.BROKER_MANAGE_PUT, RabbitConstants.BROKER_MANAGE_ROUTINGKEY_PUT, content, correlationId);
    }

    public void putWalletPool(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstants.WALLET_PUT, RabbitConstants.WALLET_ROUTINGKEY_PUT, content, correlationId);
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