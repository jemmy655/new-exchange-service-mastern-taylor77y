package cn.xa87.data.consumer;

import cn.xa87.common.constants.RabbitConstants;
import cn.xa87.data.walletBusiness.WalletBusiness;
import cn.xa87.data.walletBusiness.exception.RpcServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
//@Component
//@RabbitListener(queues = {RabbitConstants.WALLET_QUEUE_PUT})
//@Service
public class WalletConsumer {

    @Autowired
    private WalletBusiness walletBusiness;

    @RabbitHandler
    public void listener(String message) throws RpcServiceException {

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> WALLET_QUEUE_PUT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        walletBusiness.execute(message);
    }
}
