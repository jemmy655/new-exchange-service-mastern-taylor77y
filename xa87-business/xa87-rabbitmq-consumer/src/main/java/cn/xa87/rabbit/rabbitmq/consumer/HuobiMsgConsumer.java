package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.HuobiMsgBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RabbitListener(queues = {RabbitConstants.HUOBI_MANAGE_QUEUE_PUT})
@Service
public class HuobiMsgConsumer {

    @Autowired
    private HuobiMsgBusiness huobiMsgBusiness;

    @RabbitHandler
    public void listener(String message) {
        huobiMsgBusiness.execute(message);
    }
}
