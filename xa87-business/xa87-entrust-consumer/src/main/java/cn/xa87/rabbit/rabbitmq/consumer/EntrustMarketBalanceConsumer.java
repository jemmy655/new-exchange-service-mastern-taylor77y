package cn.xa87.rabbit.rabbitmq.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.rabbit.business.EntrustMarketBalanceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Component
@RabbitListener(queues = {RabbitConstants.ENTRUST_MARKET_BALANCE_QUEUE_PUT})
@Service
public class EntrustMarketBalanceConsumer {

    @Autowired
    private EntrustMarketBalanceBusiness entrustMarketBalanceBusiness;

    @RabbitHandler
    public void listener(String message) throws ParseException {
        log.info("rabbit Consumer receiver a market entrust balance message ==>{}", message);
        entrustMarketBalanceBusiness.execute(message);
    }
}
