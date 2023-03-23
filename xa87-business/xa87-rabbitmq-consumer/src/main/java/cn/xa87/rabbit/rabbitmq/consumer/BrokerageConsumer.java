package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.BrokerageBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 计算回扣返佣
 */
@Slf4j
@Component
@RabbitListener(queues = {RabbitConstants.CONSUME_CURRENCY_QUEUE_PUT})
public class BrokerageConsumer {

    @Autowired
    private BrokerageBusiness brokerageBusiness;

    @RabbitHandler
    public void registerListener(String message) {
        brokerageBusiness.execute(message);
    }
}
