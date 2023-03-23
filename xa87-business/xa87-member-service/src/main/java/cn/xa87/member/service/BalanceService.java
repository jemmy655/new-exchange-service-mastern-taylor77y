package cn.xa87.member.service;

import cn.xa87.common.web.Response;
import cn.xa87.constant.BalanceConstant;
import cn.xa87.constant.CoinConstant;
import cn.xa87.model.Balance;
import cn.xa87.model.MoneyAccount;
import cn.xa87.vo.MemberRechargeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BalanceService extends IService<Balance> {

    List<Balance> getBalances(String currency, String userId);

    Boolean transferBalances(String currency, String userId, BigDecimal balance, BalanceConstant.Move_Type moveType);

    String getETHAddress(String member,String coinname);

    Boolean extractCoin(String currency, String member, String wallet, BigDecimal balance,Integer type,String chainName, Integer mccId,Integer baId);

    Map<String, Object> getBalanceList(String member, String currency, CoinConstant.Coin_Type coinType);


    /**
     * 查看充值记录
     *
     * @param memberId memberId
     * @param currency 币种
     * @param pageNum  当前页码
     * @param pageSize 每页记录条数
     * @return
     */
    Response getRechargeRecording(String memberId, String currency, Integer pageNum, Integer pageSize);

    /**
     * 查看提现记录
     *
     * @param memberId memberId
     * @param state    state
     * @param currency 币种
     * @param pageNum  当前页码
     * @param pageSize 每页记录条数
     * @return
     */
    Response getWithdrawRecording(String memberId, String state, String currency, Integer pageNum, Integer pageSize);

    /**
     * 查看提币币种
     *
     * @param memberId memberId
     * @return
     */
    Response selectWithdrawCurrency(String memberId);

    /**
     * 查看划转记录
     *
     * @param memberId memberId
     * @param type     type
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return
     */
    Response getTransferRecording(String memberId, String type, Integer pageNum, Integer pageSize);

    Object rechargeCurrency(String memberId,BigDecimal amount,String currency, String chainName, String paymentVoucher);

    Response rechargeCurrencyRecord(String memberId, String currency, Integer pageNum, Integer pageSize);

    List<MoneyAccount> getRechargeWallet(Integer type);

    Response getRechargeConfiguration();

    Object currencyExchange(String member, String currency, String currencyTarget, BigDecimal quantity);

    Response currencyExchangeRecord(String member, Integer pageNum, Integer pageSize);
}