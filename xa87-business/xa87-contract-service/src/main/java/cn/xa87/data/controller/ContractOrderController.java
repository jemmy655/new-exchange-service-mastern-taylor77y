package cn.xa87.data.controller;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.check.LogHeaderChecker;
import cn.xa87.data.service.ContractOrderService;
import cn.xa87.model.ContractOrder;
import cn.xa87.vo.ContractDeliveryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(value = "交易", tags = {"交易"})
@RestController
@RequestMapping("/contract")
public class ContractOrderController {

    @Autowired
    private ContractOrderService contractOrderService;
    @Resource
    private Xa87RedisRepository redisRepository;

    @ApiOperation("交易/开多/开空")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setContractOrder")
    //@LogHeaderChecker
    public Response setContractOrder(@RequestBody ContractOrder contractOrder) {
        return Response.success(contractOrderService.setContractOrderNew(contractOrder));
    }

    @ApiOperation("获取订单详情")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/getContractOrder")
    public Response getContractOrder(@RequestParam String orderId) {
        return Response.success(contractOrderService.getContractOrder(orderId));
    }

    @ApiOperation("止盈/止损")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setOrdTriggerMatch")
    public Response setOrdTriggerMatch(@RequestParam String id, @RequestParam BigDecimal price, @RequestParam TokenOrderConstant.Match_Type matchType) {
        return Response.success(contractOrderService.setOrdTriggerMatch(id, price, matchType));
    }

    @ApiOperation("平仓")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setOrderMatch")
    //@LogHeaderChecker
    public Response setOrderMatch(@RequestParam String id, BigDecimal hands) {
        return Response.success(contractOrderService.setOrderMatch(id, hands));
    }

    @ApiOperation("一键平仓")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setAllContractMatch")
   // @LogHeaderChecker
    public Response setAllContractMatch(@RequestParam String memberId, String pairsName) {
        return Response.success(contractOrderService.setAllContractMatch(memberId, pairsName));
    }

    @ApiOperation("获取持仓列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getWarehouses")
    public Response getWarehouses(@RequestParam String member, String pairsName) {
        return Response.success(contractOrderService.getWarehouses(member, pairsName));
    }

    @ApiOperation("获取历史列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getHistoryOrders")
    public Response getHistoryOrders(@RequestParam String member, String pairsName, TokenOrderConstant.Order_State orderState,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.success(contractOrderService.getHistoryOrders(member, pairsName, orderState,pageNum,pageSize));
    }

    @ApiOperation("撤销普通委托单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/closeContractOrder")
   // @LogHeaderChecker
    public Response closeContractOrder(@RequestParam String orderId) {
        return Response.success(contractOrderService.closeContractOrder(orderId));
    }

    @ApiOperation("获取普通委托单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/getEntrustOrder")
    public Response getEntrustOrder(@RequestParam String member, String pairsName) {
        return Response.success(contractOrderService.getEntrustOrder(member, pairsName));
    }

    @ApiOperation("获取风险度")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getRisk")
    public Response getRisk(@RequestParam String member) {
        return Response.success(contractOrderService.getRisk(member));
    }

    @ApiOperation("获取最大可开手数")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getMaxHands")
    public Response getMaxHands(@RequestParam String member, String pairsName, BigDecimal price, BigDecimal level) {
        return Response.success(contractOrderService.getMaxHands(member, pairsName, price, level));
    }

    @ApiOperation("秒合约下注")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/secondContractBetting")
    public Response secondContractBetting(@RequestBody ContractDeliveryVo vo) {
        Object result = null;
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + vo.getMember(), 5000, 50, 100);
        if (lock_coin) {
            result = contractOrderService.secondContractBetting(vo);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + vo.getMember());
        }

        return Response.success(result);
    }

    @ApiOperation("获取秒合约交割时间配置")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getSecondsContract")
    public Response getSecondsContract() {
        return Response.success(contractOrderService.getSecondsContract());
    }


    @ApiOperation("获取货币配置")
    @GetMapping(value = "/getCurrencyConfiguration")
    public Response getCurrencyConfiguration() {
        return Response.success(contractOrderService.getCurrencyConfiguration());
    }

    @ApiOperation("获取秒合约数据")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getSecondContract")
    public Response getSecondContract(@RequestParam Integer secondId) {
        return Response.success(contractOrderService.getSecondContract(secondId));
    }


    @ApiOperation("获取秒合约记录")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getSecondContractRecord")
    public Response getSecondContractRecord(@RequestParam String member,Integer type,
                                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.success(contractOrderService.getSecondContractRecord(member,type,pageNum,pageSize));
    }


}
