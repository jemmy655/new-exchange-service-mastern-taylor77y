package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.service.SmartPoolOrderService;
import cn.xa87.data.service.SmartPoolProductService;
import cn.xa87.vo.SmartPoolOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 智能矿池 API
 * @author Administrator
 */
@Api(value = "智能矿池", tags = {"智能矿池全部接口"})
@RestController
@RequestMapping("/smart_pool_product")
public class SmartPoolProductController {

    @Autowired
    private SmartPoolOrderService  smartPoolProductService;
    @Autowired
    private SmartPoolProductService smartPoolOrderService;

    @ApiOperation("获取智能矿池产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getSmartPoolProduct")
    public Response getSmartPoolProduct() {
        return Response.success(smartPoolOrderService.getSmartPoolProduct());
    }

    @ApiOperation("获取用户的智能矿池订单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getSmartPoolOrderByUserId")
    public Response getSmartPoolOrderByUserId(@RequestParam String userId,String status) {
        return Response.success(smartPoolProductService.getSmartPoolOrderByUserId(userId,status));
    }

    @ApiOperation("获取确认订单信息")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getCheckSmartPoolOrder")
    public Response getCheckSmartPoolOrder(@RequestParam String productId) {
        return Response.success(smartPoolProductService.getCheckSmartPoolOrder(productId));
    }

    @ApiOperation("获取用户购买智能矿池统计信息")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getCountSmartPoolOrderByUserId")
    public Response getCountSmartPoolOrderByUserId(@RequestParam String userId) {
        return Response.success(smartPoolProductService.getCountSmartPoolOrderByUserId(userId));
    }

    @ApiOperation("买入智能矿池产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setSmartPoolOrderPurchase")
    public Response setSmartPoolOrderPurchase(@RequestBody SmartPoolOrderVo fundOrderVo) {
        return Response.success(smartPoolProductService.setSmartPoolOrderPurchase(fundOrderVo));
    }

    @ApiOperation("赎回智能矿池产品")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setSmartPoolOrderRedeem")
    public Response setSmartPoolOrderRedeem(@RequestBody SmartPoolOrderVo fundOrderVo) {
        Boolean isOk=smartPoolProductService.setSmartPoolOrderRedeem(fundOrderVo);
        if (!isOk){
            return Response.failure("1000","未到达可解锁日期，不可赎回");
        }
        return Response.success(smartPoolProductService.setSmartPoolOrderRedeem(fundOrderVo));
    }
}
