package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.MatchBusiness;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RabbitListener(queues = {RabbitConstants.MATCH_QUEUE_PUT})
@Service
public class MatchConsumer {

    @Autowired
    private MatchBusiness matchBusiness;

    @RabbitHandler
    public void listener(String message) {
        matchBusiness.execute(message);
    }
}
