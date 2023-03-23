package cn.xa87.member.controller;

import cn.xa87.common.web.Response;
import cn.xa87.member.check.LogHeaderChecker;
import cn.xa87.member.service.FastBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/22 10:21
 */
@Api(tags = "快速资产操作")
@RestController
@RequestMapping("fast_balance")
public class FastBalanceController {

    @Autowired
    private FastBalanceService fastBalanceService;

    @ApiOperation(value = "根据钱包地址查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wallet", value = "钱包地址", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currency", value = "币种", required = true, paramType = "query", dataType = "String")
    })

    @GetMapping("wallet")
    public Response userInfoByWallet(@RequestParam String wallet,
                                     @RequestParam String currency) {
        return fastBalanceService.userInfoByWallet(wallet, currency);
    }

    @ApiOperation(value = "快速充币")
    @PostMapping("recharge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "memberId", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "payPassword", value = "支付密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currency", value = "币种", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "number", value = "数量", required = true, paramType = "query", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "wallet", value = "被充币钱包地址", required = true, paramType = "query", dataType = "String")
    })
    @LogHeaderChecker
    public Response recharge(@RequestParam String memberId,
                             @RequestParam String payPassword,
                             @RequestParam String currency,
                             @RequestParam BigDecimal number,
                             @RequestParam String wallet) {
        return fastBalanceService.recharge(memberId, payPassword, currency, number, wallet);
    }

}
