package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.service.FundOrderService;
import cn.xa87.data.service.FundProductService;
import cn.xa87.data.service.PledgeOrderDetailService;
import cn.xa87.data.service.PledgeOrderService;
import cn.xa87.model.PledgeOrder;
import cn.xa87.vo.FundOrderVo;
import cn.xa87.vo.PledgeOrderDetailVo;
import cn.xa87.vo.PledgeOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 基金理财 API
 * @author Administrator
 */
@Api(value = "质押借币", tags = {"质押借币全部接口"})
@RestController
@RequestMapping("/pledge_order")
public class PledgeOrderController {

    @Autowired
    private PledgeOrderService pledgeOrderService;
    @Autowired
    private PledgeOrderDetailService pledgeOrderDetailService;

    @ApiOperation("获取币种列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getFundOrderByUserId")
    public Response getMemberCionName(@RequestParam String userId) {
        return Response.success(pledgeOrderService.getMemberCionName(userId));
    }

    @ApiOperation("获取借款金额")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getLoanAmount")
    public Response getLoanMoney(@RequestParam String userId,@RequestParam String pledge_name,
                                         @RequestParam BigDecimal borrow_Price,@RequestParam BigDecimal pledge_Price,@RequestParam BigDecimal loanCycle) {
        return Response.success(pledgeOrderService.getLoanMoney(userId,loanCycle,pledge_name,borrow_Price,pledge_Price));
    }

    @ApiOperation("获取订单列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getCountFundOrderByUserId")
    public Response getPledgeOrderList(@RequestParam String userId) {
        return Response.success(pledgeOrderService.getPledgeOrderList(userId));
    }

    @ApiOperation("新增质押/续借")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderPurchase")
    public Response setPledgeOrderDetail(@RequestBody PledgeOrderDetailVo pledgeOrderDetailVo) {
        return Response.success(pledgeOrderDetailService.setPledgeOrderDetail(pledgeOrderDetailVo));
    }

    @ApiOperation("借款/下订单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setCheckFundOrder")
    public Response setPledgeOrderBorrow(@RequestBody PledgeOrderVo pledgeOrderVo) {
        return Response.success(pledgeOrderService.setPledgeOrderBorrow(pledgeOrderVo));
    }

    @ApiOperation("还款")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setFundOrderRedeem")
    public Response setPledgeOrderRepayment(@RequestBody PledgeOrderVo pledgeOrderVo) {
        return Response.success(pledgeOrderService.setPledgeOrderRepayment(pledgeOrderVo));
    }
}
