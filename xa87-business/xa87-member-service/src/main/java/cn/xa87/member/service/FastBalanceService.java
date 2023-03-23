package cn.xa87.member.service;

import cn.xa87.common.web.Response;

import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/22 10:23
 */
public interface FastBalanceService {
    /**
     * 根据钱包地址查询用户信息
     *
     * @param wallet   钱包地址
     * @param currency 币种
     * @return
     */
    Response userInfoByWallet(String wallet, String currency);

    /**
     * 快速充币
     *
     * @param memberId    memberId
     * @param payPassword 支付密码
     * @param currency    币种
     * @param number      数量
     * @param wallet      被充币钱包地址
     * @return Response
     */
    Response recharge(String memberId, String payPassword, String currency, BigDecimal number, String wallet);
}
