package cn.xa87.data.mapper;

import cn.xa87.model.WalletPool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface WalletPoolMapper extends BaseMapper<WalletPool> {

    //type = MAIN_DEPOSIT_ACCOUNT   MAIN_WITHDRAW_ACCOUNT
    WalletPool getMainAccount(String type);

    WalletPool getWalletPoolByMember(String member);
}