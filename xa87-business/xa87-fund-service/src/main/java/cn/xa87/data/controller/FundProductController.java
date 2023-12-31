package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.service.FundOrderService;
import cn.xa87.data.service.FundProductService;
import cn.xa87.vo.FundOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 基金理财 API
 * @author Administrator
 */
@Api(value = "基金理财", tags = {"基金理财全部接口"})
@RestController
@RequestMapping("/fund_product")
public class FundProductController {

    @Autowired
    private FundOrderService fundOrderService;
    @Autowired
    private FundProductService fundProductService;
    @ApiOperation("获取基金理财产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getFundProduct")
    public Response getFundProduct() {
        return Response.success(fundProductService.getFundProduct());
    }

    @ApiOperation("获取用户的理财订单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getFundOrderByUserId")
    public Response getFundOrderByUserId(@RequestParam String userId,@RequestParam String status) {
        return Response.success(fundOrderService.getFundOrderByUserId(userId,status));
    }

    @ApiOperation("获取确认订单信息")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/getCheckFundOrder")
    public Response getCheckFundOrder(@RequestParam String productId) {
        return Response.success(fundOrderService.getCheckFundOrder(productId));
    }

    @ApiOperation("获取用户购买基金统计信息")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/getCountFundOrderByUserId")
    public Response getCountFundOrderByUserId(@RequestParam String userId) {
        return Response.success(fundOrderService.getCountFundOrderByUserId(userId));
    }

    @ApiOperation("买入理财产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderPurchase")
    public Response setFundOrderPurchase(@RequestBody FundOrderVo fundOrderVo) {
        return Response.success(fundOrderService.setFundOrderPurchase(fundOrderVo));
    }

    @ApiOperation("强赎回理财产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderRedeem")
    public Response setFundOrderRedeem(@RequestBody FundOrderVo fundOrderVo) {
        return Response.success(fundOrderService.setFundOrderRedeem(fundOrderVo));
    }
}
