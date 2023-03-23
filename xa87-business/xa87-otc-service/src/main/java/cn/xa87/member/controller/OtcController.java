package cn.xa87.member.controller;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Otc365RespData;
import cn.xa87.common.web.Response;
import cn.xa87.member.check.HeaderChecker;
import cn.xa87.member.check.LogHeaderChecker;
import cn.xa87.member.common.util.Callback;
import cn.xa87.member.service.FbOrderService;
import cn.xa87.member.service.impl.Otc365FbOrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@Api(value = "商户发布广告/确认付款/确认放币", tags = {"商户发布广告/确认付款/确认放币"})
@RestController
@RequestMapping("/order")
@Slf4j
public class OtcController {
    @Autowired
    private Otc365FbOrderServiceImpl fbOrderService;

    @Resource
    private Xa87RedisRepository redisRepository;

    @ApiOperation("发布广告")
    @PostMapping(value = "/orderReleasing")
    @HeaderChecker(headerNames = {"token", "userId"})
    @LogHeaderChecker
    public Response resetPassword(@RequestParam String userId, @RequestParam String upDownNumber, @RequestParam String currency, @RequestParam String extremum,
                                  @RequestParam String num, @RequestParam String minPrice, @ApiParam(value = "收款方式,多种的话用-拼接(ALIAY,WECHAT,BANKCARD)") @RequestParam String payType,
                                  @RequestParam String remarks, @ApiParam(value = "是否开启自动回复(OPEN,UNOPEN)") @RequestParam String autoStatus,
                                  @RequestParam String autoMsg, @ApiParam(value = "方向(BUY,SELL)") @RequestParam String direction) {
        return Response.success(fbOrderService.orderReleasing(userId, upDownNumber, currency, num, minPrice, payType, remarks, autoStatus, direction, extremum, autoMsg));
    }

    @ApiOperation("广告列表")
    @PostMapping(value = "/orderList")
    public Response resetPassword(@RequestParam String page, @RequestParam String size, @RequestParam String currency, @ApiParam(value = "方向(BUY,SELL)") @RequestParam String direction, Integer mccId) {
        return Response.success(fbOrderService.orderList(page, size, currency, direction, mccId));
    }

    @ApiOperation("币种列表")
    @PostMapping(value = "/currencyList")
    public Response currencyList() {
        return Response.success(fbOrderService.currencyList());
    }

    @ApiOperation("下单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/placeAnOrder")
    @LogHeaderChecker
    public Response placeAnOrder(@RequestParam String orderId, @RequestParam String userId, @ApiParam(value = "下单类型(PRICE,NUM)") @RequestParam String type, @RequestParam String num , @RequestParam Integer mccId) {
        Object result = null;
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (lock_coin) {
            result = fbOrderService.placeAnOrder(orderId, userId, type, num, mccId);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        }

        return Response.success(result);
    }

    @ApiOperation("订单详情")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/orderDetails")
    public Response orderDetails(@RequestParam String priceOrderId, @RequestParam String userId) {
        return Response.success(fbOrderService.orderDetails(priceOrderId, userId));
    }

    @ApiOperation("个人订单列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/ownOrderList")
    public Response ownOrderList(@ApiParam(value = "订单状态(NONPAYMENT,FINISH,CALLOFF)") @RequestParam String priceOrderStatus, @RequestParam String userId, @RequestParam String page, @RequestParam String size) {
        return Response.success(fbOrderService.ownOrderList(priceOrderStatus, userId, page, size));
    }

    @ApiOperation("确认付款")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/payment")
    @LogHeaderChecker
    public Response payment(@RequestParam String priceOrderId, @RequestParam String userId, @RequestParam String payType) {
        return Response.success(fbOrderService.payment(priceOrderId, userId, payType));
    }

    @ApiOperation("一键购买")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/fastPlaceAnOrder")
    @LogHeaderChecker
    public Response fastPlaceAnOrder(@RequestParam String userId, @RequestParam String price, @ApiParam(value = "方向(BUY,SELL)") @RequestParam String direction, @RequestParam String currency, @ApiParam(value = "收款方式,(ALIAY,WECHAT,BANKCARD)") @RequestParam String payType, @RequestParam Integer mccId) {
        Object result = null;
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (lock_coin) {
            result = fbOrderService.fastPlaceAnOrder(userId, price, direction, currency, payType, mccId);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        }
        return Response.success(result);
    }

    @ApiOperation("一键购买匹配价格")
    @PostMapping(value = "/fastPlaceAnOrderGetPrice")
    public Response fastPlaceAnOrderGetPrice(@RequestParam String userId, @ApiParam(value = "下单类型(PRICE,NUM)") @RequestParam String type, @RequestParam String price, @ApiParam(value = "方向(BUY,SELL)") @RequestParam String direction, @RequestParam String currency, @ApiParam(value = "收款方式,(ALIAY,WECHAT,BANKCARD)") @RequestParam String payType) {
        return Response.success(fbOrderService.fastPlaceAnOrderGetPrice(userId, type, price, direction, currency, payType));
    }

    @ApiOperation("撤回广告")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/orderBack")
    @LogHeaderChecker
    public Response orderBack(@RequestParam String orderId, @RequestParam String userId) {
        return Response.success(fbOrderService.orderBack(orderId, userId));
    }

    @ApiOperation("确认放币")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/deliverGoods")
    @LogHeaderChecker
    public Response deliverGoods(@RequestParam String priceOrderId, @RequestParam String userId) {
        return Response.success(fbOrderService.deliverGoods(priceOrderId, userId));
    }

    @ApiOperation("获取申诉类型")
    @PostMapping(value = "/getAppealList")
    public Response getAppealList() {
        return Response.success(fbOrderService.getAppealList());
    }

    @ApiOperation("取消订单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/backOrder")
    @LogHeaderChecker
    public Response backOrder(@RequestParam String priceOrderId, @RequestParam String userId) {
        return Response.success(fbOrderService.backOrder(priceOrderId, userId));
    }

    @ApiOperation("获取订单结束时间")
    @PostMapping(value = "/orderEndTime")
    public Response orderEndTime(@RequestParam String priceOrderId) {
        return Response.success(fbOrderService.orderEndTime(priceOrderId));
    }

    @ApiOperation("申诉订单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/appeal")
    @LogHeaderChecker
    public Response appeal(@RequestParam String priceOrderId, @RequestParam String userId, @RequestParam String msg, String file, String file1) {
        return Response.success(fbOrderService.appeal(priceOrderId, userId, msg, file, file1));
    }

    @ApiOperation("取消申诉")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/appealBack")
    @LogHeaderChecker
    public Response appealBack(@RequestParam String priceOrderId, @RequestParam String userId) {
        return Response.success(fbOrderService.appealBack(priceOrderId, userId));
    }

    @ApiOperation("商户广告列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/merchantsOrders")
    public Response merchantsOrders(@RequestParam String userId, @RequestParam String page, @RequestParam String size) {
        return Response.success(fbOrderService.merchantsOrders(userId, page, size));
    }

    @ApiOperation("当前价")
    @PostMapping(value = "/getPrice")
    public Response getNowPrice(@RequestParam String currency) {
        return Response.success(fbOrderService.getNowPrice(currency));
    }

    @ApiOperation("溢价配置")
    @PostMapping(value = "/ratioConfig")
    public Response ratioConfig() {
        return Response.success(fbOrderService.ratioConfig());
    }

    @ApiOperation("修改广告")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/updataOrder")
    @LogHeaderChecker
    public Response updataOrder(@RequestParam String orderId, @RequestParam String userId, @RequestParam String upDownNumber, @RequestParam String extremum,
                                @RequestParam String num, @RequestParam String minPrice, @ApiParam(value = "收款方式,多种的话用-拼接(ALIAY,WECHAT,BANKCARD)") @RequestParam String payType,
                                @RequestParam String remarks, @ApiParam(value = "是否开启自动回复(OPEN,UNOPEN)") @RequestParam String autoStatus,
                                @RequestParam String autoMsg) {
        return Response.success(fbOrderService.updataOrder(orderId, userId, upDownNumber, extremum, num, minPrice, payType, remarks, autoStatus, autoMsg));
    }
    @ApiOperation("OTC365回调")
    @PostMapping(value = "/callback")
    @LogHeaderChecker
    public Otc365RespData callback(@RequestBody Callback msg) {
        return Otc365RespData.success(fbOrderService.callback(msg));
    }

    @ApiOperation("一键下单OTC365")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/addOrder")
    @LogHeaderChecker
    public Response addOrder(@RequestParam String userId, @RequestParam String price, @ApiParam(value = "方向(BUY,SELL)") @RequestParam String direction, @RequestParam String currency, @ApiParam(value = "收款方式,(ALIAY,WECHAT,BANKCARD)") @RequestParam String payType) {
        return Response.success(fbOrderService.addOrder(userId, price, direction, currency, payType));
    }
}
