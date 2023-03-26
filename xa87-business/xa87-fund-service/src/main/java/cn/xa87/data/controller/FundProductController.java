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
    @PostMapping(value = "/getFundOrderByUserId")
    public Response getFundOrderByUserId(@RequestParam String userId,String status) {
        return Response.success(fundOrderService.getFundOrderByUserId(userId,status));
    }

    @ApiOperation("买入理财产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderPurchase")
    public Response setFundOrderPurchase(FundOrderVo fundOrderVo) {
        return Response.success(fundOrderService.setFundOrderPurchase(fundOrderVo));
    }

    @ApiOperation("强赎回理财产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderRedeem")
    public Response setFundOrderRedeem(FundOrderVo fundOrderVo) {
        return Response.success(fundOrderService.setFundOrderRedeem(fundOrderVo));
    }
}
