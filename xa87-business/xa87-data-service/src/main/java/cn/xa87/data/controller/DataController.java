package cn.xa87.data.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.common.web.Response;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.DataConstant;
import cn.xa87.data.mapper.ShareImgMapper;
import cn.xa87.data.service.DataService;
import cn.xa87.model.Balance;
import cn.xa87.model.DepositHistory;
import cn.xa87.model.ShareImg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
@Slf4j
@Api(value = "获取主页信息", tags = {"获取主页信息"})
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private ShareImgMapper shareImgMapper;


    @ApiOperation("获取banner列表")
    @GetMapping(value = "/getBanners")
    public Response getBanners(DataConstant.Banner_Type bannerType, DataConstant.Global_Type global) {
        return Response.success(dataService.getBanners(bannerType, global));
    }

    @ApiOperation("获取banner列表")
    @GetMapping(value = "/getShareImg")
    public Response getShareImg(String type) {
        QueryWrapper<ShareImg> queryWrapper = new QueryWrapper<ShareImg>();
        if (StringUtils.isNoneEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        return Response.success(shareImgMapper.selectList(queryWrapper));
    }

    @ApiOperation("获取公告列表")
    @GetMapping(value = "/getNotices")
    public Response sendMail(@RequestParam DataConstant.Notice_Type noticeType, DataConstant.Global_Type global) {
        log.info("公告枚举 {}",global);
        return Response.success(dataService.getNotices(noticeType, global));
    }

    @ApiOperation("获取主页币列表/3个币(TOP)/涨幅榜(UPDOWN)/成交榜(VOLUME)/新币榜(PROJECT)")
    @GetMapping(value = "/getIndexCoin")
    public Response getIndexCoin(@RequestParam CoinConstant.Get_Coin_Type getCoinType) {

        return Response.success(dataService.getIndexCoin(getCoinType));
    }

    @ApiOperation("获取主页币列表/3个币(TOP)/涨幅榜(UPDOWN)/成交榜(VOLUME)/新币榜(PROJECT)")
    @GetMapping(value = "/getIndexCoinNew")
    public Response getIndexCoinNew() {
        String res = HttpUtil.doGet("https://db23app.vip/wap/api/hobi!getRealtime.action?symbol=btc,eth,algo,mln,dot,neo,iota,yfi,etc,xrp,axs,sand,ltc,mana,sol,eos,bhd,link,mx,chr,chz");
        return Response.success(res);
    }

    @ApiOperation("行情列表")
    @GetMapping(value = "/getCoinQuotation")
    public Response getCoinQuotation(@RequestParam CoinConstant.Get_Coin_Sort getCoinSort, CoinConstant.Coin_Type coinType) {
        return Response.success(dataService.getCoinQuotation(getCoinSort, coinType));
    }

    @ApiOperation("获取单个币价格详情")
    @GetMapping(value = "/getCoinInfo")
    public Response getCoinInfo(@RequestParam String pairsName) {
        return Response.success(dataService.getCoinInfo(pairsName));
    }

    @ApiOperation("获取所有交易对主币")
    @GetMapping(value = "/getMainCurs")
    public Response getMainCurs() {
        return Response.success(dataService.getMainCurs());
    }

    @ApiOperation("拉取K线数据")
    @GetMapping(value = "/syncKline")
    public Response syncKline() {
        dataService.syncKline();
        return Response.success();
    }

    @ApiOperation("获取交易对对应币列表")
    @GetMapping(value = "/getPairsByMainCur")
    public Response getPairsByMainCur(@RequestParam(required = false) String mainCur, CoinConstant.Coin_Type type) {
        return Response.success(dataService.getPairsByMainCur(mainCur, type));
    }

    @ApiOperation("玩家转入")
    @PostMapping(value = "/apiListen")
    public String apiListen(@RequestBody apidata data) {
        return dataService.apiListen(data);
    }
}
