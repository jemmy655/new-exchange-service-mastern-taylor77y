package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.RegisterBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RabbitListener(queues = {RabbitConstants.REGISTER_QUEUE_PUT})
@Service
public class RegisterConsumer {

    @Autowired
    private RegisterBusiness registerBusiness;

    @RabbitHandler
    public void listener(String message) {
        registerBusiness.execute(message);
    }
}
