package cn.xa87.member.mapper;

import cn.xa87.model.WalletPool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface WalletPoolMapper extends BaseMapper<WalletPool> {

    public static final  String MAIN_DEPOSIT_ACCOUNT="MAIN_DEPOSIT_ACCOUNT";
    public static final String MAIN_WITHDRAW_ACCOUNT="MAIN_WITHDRAW_ACCOUNT";
    //type = MAIN_DEPOSIT_ACCOUNT
    WalletPool getMainAccount(String type);

    WalletPool getWalletPoolByMember(String member);


}