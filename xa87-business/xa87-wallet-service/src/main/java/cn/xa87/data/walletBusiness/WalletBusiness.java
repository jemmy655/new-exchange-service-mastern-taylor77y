package cn.xa87.data.walletBusiness;

import cn.xa87.data.mapper.SysConfigMapper;
import cn.xa87.data.mapper.WalletPoolMapper;
import cn.xa87.data.walletBusiness.exception.RpcServiceException;
import cn.xa87.data.walletBusiness.service.EthereumService;
import cn.xa87.data.walletBusiness.service.ZtpayService;
import cn.xa87.model.WalletPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class WalletBusiness {
    @Autowired
    private WalletPoolMapper walletPoolMapper;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private ZtpayService ethereumService;

    @Async
    public void execute(String msg) throws RpcServiceException {
            /*String passoword = UUID.randomUUID().toString();
            String account = ethereumService.createAccount(passoword);
            WalletPool walletPool1 = new WalletPool();
            walletPool1.setMember(msg);
            walletPool1.setAddress(account);
            walletPool1.setId(passoword);
            walletPool1.setCreateTime(new Date());
            walletPoolMapper.insert(walletPool1);*/
        String coins[]={"BTC","ETH","USDT","LTC"};
        for(String coin : coins){
            String passoword = UUID.randomUUID().toString();

                String account = ethereumService.get_address(coin);// ethereumService.createAccount(passoword);
                WalletPool walletPool1 = new WalletPool();
                walletPool1.setMember(msg);
                walletPool1.setAddress(account);
                walletPool1.setId(passoword);
                walletPool1.setCoin(coin);
                walletPool1.setCreateTime(new Date());
                walletPoolMapper.insert(walletPool1);


        }


    }


}
