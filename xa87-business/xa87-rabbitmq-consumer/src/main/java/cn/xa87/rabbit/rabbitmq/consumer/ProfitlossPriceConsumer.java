package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.ProfitlossPriceBusiness;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RabbitListener(queues = {RabbitConstants.PROFITLOSS_PRICE_QUEUE_PUT})
@Service
public class ProfitlossPriceConsumer {

    @Autowired
    private ProfitlossPriceBusiness profitlossPriceBusiness;

    @RabbitHandler
    public void listener(String message) {
        profitlossPriceBusiness.execute(message);
    }
}
