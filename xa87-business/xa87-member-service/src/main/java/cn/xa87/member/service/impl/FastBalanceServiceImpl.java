package cn.xa87.member.service.impl;

import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.utils.PasswordHelper;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.member.mapper.BalanceMapper;
import cn.xa87.member.mapper.MemberMapper;
import cn.xa87.member.mapper.WalletPoolMapper;
import cn.xa87.member.service.FastBalanceService;
import cn.xa87.model.Balance;
import cn.xa87.model.Member;
import cn.xa87.model.WalletPool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/22 10:23
 */
@Slf4j
@Service
public class FastBalanceServiceImpl implements FastBalanceService {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private WalletPoolMapper walletPoolMapper;


    @Override
    public Response userInfoByWallet(String walletAddress, String currency) {
        QueryWrapper<WalletPool> objectQueryWrapper = new QueryWrapper<WalletPool>();
        objectQueryWrapper.eq("address", walletAddress);
        WalletPool walletPool = walletPoolMapper.selectOne(objectQueryWrapper);
        Member member = memberMapper.selectById(walletPool.getMember());
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", member.getUuid());
        map.put("phone", member.getPhone() == null ? "" : member.getPhone());
        map.put("mail", member.getMail() == null ? "" : member.getMail());
        return Response.success(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response recharge(String memberId, String payPassword, String currency, BigDecimal number, String walletAddress) {

        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(AjaxResultEnum.USER_DOES_NOT_EXIST.getMessage());
        }
        String payPwd = member.getPayPassword();
        if ("".equals(payPwd)) {
            throw new BusinessException(AjaxResultEnum.PLEASE_SET_A_PAYMENT_PASSWORD.getMessage());
        }
        if (!payPwd.equals(PasswordHelper.encryString(payPassword, member.getSalt()))) {
            throw new BusinessException(AjaxResultEnum.WRONG_PAYMENT_PASSWORD.getMessage());
        }
        Balance balance = balanceMapper.selectOne(
                new QueryWrapper<Balance>()
                        .eq("user_id", memberId)
                        .eq("currency", currency));
        BigDecimal tokenBalance = balance.getAssetsBalance();
        if (tokenBalance.compareTo(number) < 0) {
            throw new BusinessException(AjaxResultEnum.NOT_ENOUGH_COINS.getMessage());
        }
        QueryWrapper<WalletPool> objectQueryWrapper = new QueryWrapper<WalletPool>();
        objectQueryWrapper.eq("address", walletAddress);
        WalletPool walletPool = walletPoolMapper.selectOne(objectQueryWrapper);
        String member1 = walletPool.getMember();
        if (memberId.equals(member1)) {
            throw new BusinessException(AjaxResultEnum.TRANSFER_MONEY_TO_MYSELF.getMessage());
        }
        //给自己减少钱
        balanceMapper.lessBalance(currency, number, memberId);
        //给别人加钱
        balanceMapper.addBalance(currency, number, member1);
        //差一个快速转币记录,如果分开查询直接建表最快,
        return Response.success();
    }

    /**
     * 获取t_wallet列名
     *
     * @param currency currency
     * @return t_wallet列名
     */
    private String getWalletColumn(String currency) {
        String walletColumn;
        switch (currency) {
            case "BTC":
                walletColumn = "btc_wallet";
                break;
            default:
                walletColumn = "eth_wallet";
        }
        return walletColumn;
    }
}
