package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.check.HeaderChecker;
import cn.xa87.data.check.LogHeaderChecker;
import cn.xa87.data.service.EntrustService;
import cn.xa87.model.Entrust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "币币交易", tags = {"币币交易"})
@RestController
@RequestMapping("/entrust")
public class EntrustController {

    @Autowired
    private EntrustService entrustService;

    @ApiOperation("交易/买/卖")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/setEntrust")
    public Response setEntrust(@RequestBody Entrust entrust) {
        return Response.success(entrustService.setEntrust(entrust));
    }


    @ApiOperation("获取历史记录")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getHistoryEntrust")
    public Response getHistoryEntrust(@RequestParam String member, String pairsName,@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.success(entrustService.getHistoryEntrust(member, pairsName,pageNum,pageSize));
    }

    @ApiOperation("获取委托列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getEntrustList")
    public Response getEntrustList(@RequestParam String member, String pairsName) {
        return Response.success(entrustService.getEntrustList(member, pairsName));
    }

    @ApiOperation("撤销委托/买/卖")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/closeEntrust")
    @LogHeaderChecker
    public Response closeEntrust(@RequestParam String entrust) {
        return Response.success(entrustService.closeEntrust(entrust));
    }


    @ApiOperation("撤销委托/买/卖(后台API使用)")
    @PostMapping(value = "/closeEntrustBackstage")
    @LogHeaderChecker
    public Response closeEntrustBackstage(@RequestParam String entrust) {
        return Response.success(entrustService.closeEntrust(entrust));
    }

    @ApiOperation("一键收购(后台API使用)")
    @PostMapping(value = "/setEntrustBackstage")
    @LogHeaderChecker
    public Response setEntrustBackstage(@RequestParam String entrust) {
        return Response.success(entrustService.setEntrustBackstage(entrust));
    }

    @ApiOperation("创建K线数据(后台API使用)")
    @PostMapping(value = "/createKlineData")
    @LogHeaderChecker
    public Response createKlineData(Integer number,Boolean isClear,String coin) {
        return Response.success(entrustService.createKlineData(number,isClear,coin));
    }

    @ApiOperation("测试")
    @GetMapping(value = "/test")
    public Response test() {
        return Response.success(entrustService.test());
    }
}
