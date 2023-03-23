package cn.xa87.member.controller;

import cn.xa87.common.web.Response;
import cn.xa87.constant.BalanceConstant;
import cn.xa87.constant.CoinConstant;
import cn.xa87.member.check.HeaderChecker;
import cn.xa87.member.check.LogHeaderChecker;
import cn.xa87.member.service.BalanceService;
import cn.xa87.vo.MemberRechargeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Api(value = "用户资产操作", tags = {"用户资产操作"})
@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @ApiOperation("查询资产")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getBalances")
    public Response getBalances(String currency, @RequestParam String member) {
        return Response.success(balanceService.getBalances(currency, member));
    }

    @ApiOperation("划转资产")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/transferBalances")
    @LogHeaderChecker
    public Response transferBalances(@RequestParam String currency, @RequestParam String member, @RequestParam BigDecimal balance, @RequestParam BalanceConstant.Move_Type moveType) {
        return Response.success(balanceService.transferBalances(currency, member, balance, moveType));
    }

    @ApiOperation("充币")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getWallet")
    public Response getWallet(@RequestParam String currency, @RequestParam String member) {
        return Response.success(balanceService.getETHAddress(member,currency));
    }

    @ApiOperation("USDT充币")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getUsdtWallet")
    public Response getUsdtWallet(@RequestParam String member, String type) {
        return Response.success(balanceService.getETHAddress(member,"USDT"));
    }

    @ApiOperation("提币")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/extractCoin")
    @LogHeaderChecker
    public Response extractCoin(String currency, @RequestParam String member, String wallet, @RequestParam BigDecimal balance, @RequestParam Integer type, String chainName, Integer mccId,Integer baId) {
        return Response.success(balanceService.extractCoin(currency, member, wallet, balance,type,chainName,mccId,baId));
    }

    @ApiOperation("查询资产列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getBalanceList")
    public Response getBalanceList(@RequestParam String member, String currency, CoinConstant.Coin_Type coinType) {
        return Response.success(balanceService.getBalanceList(member, currency, coinType));
    }

    @ApiOperation(value = "充币记录", notes = "查看")
    @GetMapping("recharge_recording")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response getRechargeRecording(@RequestParam(value = "memberId") String memberId,
                                         @RequestParam(value = "currency", required = false) String currency,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return balanceService.getRechargeRecording(memberId, currency, pageNum, pageSize);
    }


    @ApiOperation(value = "提现记录", notes = "查看")
    @GetMapping("withdraw_recording")
    @HeaderChecker(headerNames = {"token", "userId"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态(全部(不传值)审核中(AUDIT)审核通过(PASS)拒绝(REJECT))", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currency", value = "币种(默认全部)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录条数", defaultValue = "10", paramType = "query", dataType = "Integer")
    })
    public Response getWithdrawRecording(@RequestParam(value = "memberId") String memberId,
                                         @RequestParam(value = "state", required = false) String state,
                                         @RequestParam(value = "currency", required = false) String currency,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return balanceService.getWithdrawRecording(memberId, state, currency, pageNum, pageSize);
    }

    @ApiOperation(value = "提币币种", notes = "查看")
    @GetMapping("withdraw_currency")
    @HeaderChecker(headerNames = {"token", "userId"})
    @ApiImplicitParam(name = "memberId", value = "用户id", required = true, paramType = "query", dataType = "String")
    public Response withdrawCurrency(@RequestParam(value = "memberId") String memberId) {
        return balanceService.selectWithdrawCurrency(memberId);
    }

    @ApiOperation(value = "划转记录")
    @GetMapping("transfer_recording")
    @HeaderChecker(headerNames = {"token", "userId"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "用户id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "划转类型(币币到合(BALANCE_MOVE_TOKEN)合约到比比(TOKEN_MOVE_BALANCE)法币到币币(FB_MOVE_BALANCE)币币到法币(BALANCE_MOVE_FB))", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录条数", defaultValue = "10", paramType = "query", dataType = "Integer")
    })
    public Response getTransferRecording(@RequestParam("memberId") String memberId,
                                         @RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return balanceService.getTransferRecording(memberId, type, pageNum, pageSize);
    }


    @ApiOperation("充值币")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/rechargeCurrency")
    @LogHeaderChecker
    public Response rechargeCurrency(@RequestParam String memberId, @RequestParam BigDecimal amount , @RequestParam String currency, String chainName, @RequestParam String paymentVoucher) {
        return Response.success(balanceService.rechargeCurrency(memberId,amount,currency,chainName,paymentVoucher));
    }

    @ApiOperation(value = "充币记录2", notes = "查看")
    @GetMapping("/rechargeCurrencyRecord")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response rechargeCurrencyRecord(@RequestParam(value = "memberId") String memberId,
                                         @RequestParam(value = "currency", required = false) String currency,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return balanceService.rechargeCurrencyRecord(memberId, currency, pageNum, pageSize);
    }


    @ApiOperation("获取充值接口")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getRechargeWallet")
    public Response getRechargeWallet(@RequestParam(value = "type")Integer type) {
        return Response.success(balanceService.getRechargeWallet(type));
    }


    @ApiOperation("获取充值接口")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getRechargeConfiguration")
    public Response getRechargeConfiguration() {
        return Response.success(balanceService.getRechargeConfiguration());
    }

    @ApiOperation("(新)获取提币手续费接口")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/new/getRechargeConfiguration")
    public Response newGetRechargeConfiguration(@RequestParam(value = "key")String key) {
        return Response.success(balanceService.newGetRechargeConfiguration(key));
    }


    @ApiOperation("币币兑换")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/currencyExchange")
    public Response currencyExchange(@RequestParam String member,
                                     @RequestParam String currency,
                                     @RequestParam String currencyTarget,
                                     @RequestParam BigDecimal quantity) {
        return Response.success(balanceService.currencyExchange(member,currency,currencyTarget,quantity));
    }



    @ApiOperation(value = "币币兑换记录", notes = "查看")
    @GetMapping("/currencyExchangeRecord")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response currencyExchangeRecord(@RequestParam(value = "member") String member,
                                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return balanceService.currencyExchangeRecord(member, pageNum, pageSize);
    }
}
