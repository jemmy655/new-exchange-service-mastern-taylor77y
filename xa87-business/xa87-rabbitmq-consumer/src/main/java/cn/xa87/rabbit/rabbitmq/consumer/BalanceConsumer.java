package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.BalanceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Component
@RabbitListener(queues = {RabbitConstants.BALANCE_QUEUE_PUT})
@Service
public class BalanceConsumer {

    @Autowired
    private BalanceBusiness balanceBusiness;

    @RabbitHandler
    public void listener(String message) throws ParseException {
        balanceBusiness.execute(message);
    }
}
