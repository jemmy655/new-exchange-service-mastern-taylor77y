package cn.xa87.data.controller;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.service.ContractOrderService;
import cn.xa87.data.service.PerpetualContractOrderService;
import cn.xa87.model.PerpetualContractOrder;
import cn.xa87.vo.ContractDeliveryVo;
import cn.xa87.vo.PerpetualContractOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(value = "新交易", tags = {"亚琴新交易"})
@RestController
@RequestMapping("/perpetual_contract")
public class PerpetualContractOrderController {

    @Autowired
    private PerpetualContractOrderService perpetualContractOrderService;
    @Resource
    private Xa87RedisRepository redisRepository;

    @ApiOperation("交易/开多/开空")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setContractOrderBuy")
    public Response setContractOrderBuy(@RequestBody PerpetualContractOrderVO perpetualContractOrder) {
        return Response.success(perpetualContractOrderService.setContractOrder(perpetualContractOrder));
    }

    @ApiOperation("交易/平多/平空")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setContractOrderSell")
    public Response setContractOrderSell(@RequestBody PerpetualContractOrderVO perpetualContractOrder) {
        return Response.success(perpetualContractOrderService.setContractOrderSell(perpetualContractOrder));
    }

    @ApiOperation("单个平仓")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setOrderMatch")
    //@LogHeaderChecker
    public Response setOrderMatch(@RequestParam String id) {
        return Response.success(perpetualContractOrderService.setOrderMatch(id));
    }

    @ApiOperation("一键平仓")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setAllContractMatch")
   // @LogHeaderChecker
    public Response setAllContractMatch(@RequestParam String memberId, String pairsName) {
        return Response.success(perpetualContractOrderService.setAllContractMatch(memberId, pairsName));
    }


    @ApiOperation("获取持仓列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getWarehouses")
    public Response getWarehouses(@RequestParam String memberId,@RequestParam String pairsName,@RequestParam String price) {
        return Response.success(perpetualContractOrderService.getWarehouses(memberId, pairsName, price));
    }

    @ApiOperation("获取订单详情")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/getContractOrder")
    public Response getContractOrder(@RequestParam String orderId) {
        return Response.success(perpetualContractOrderService.getContractOrder(orderId));
    }


    @ApiOperation("获取历史列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getHistoryOrders")
    public Response getHistoryOrders(@RequestParam String member, String pairsName, TokenOrderConstant.Order_State orderState,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.success(perpetualContractOrderService.getHistoryOrders(member, pairsName, orderState,pageNum,pageSize));
    }

}
