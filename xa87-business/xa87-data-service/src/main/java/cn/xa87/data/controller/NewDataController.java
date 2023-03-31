package cn.xa87.data.controller;

import cn.xa87.common.utils.HttpUtil;
import cn.xa87.common.web.Response;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "获取主页信息", tags = {"获取主页信息"})
@RestController
@RequestMapping("/data/new")
public class NewDataController {
    private String baseUrl = "https://www.okx.com/priapi/v5/market";
    @ApiOperation("新获币数据")
    @GetMapping(value = "/realtime")
    public Response realTime(String symbol,String order) {
        symbol=symbol.toUpperCase();
        if(StringUtils.isBlank(symbol)){
            symbol = "BTC,ETH,USDT,BNB,USDC,XRP,ADA,DOGE,MATIC,OKB,SOL,DOT,LTC,SHIB,TRX,AVAX,DAI,UNI,WBTC,LINK,ATOM,LEO,TON,ETC,XMR,XLM,BCH,FIL,APT,LDO,TUSD,HBAR,CRO,NEAR,ARB,APE,ALGO,ICP,STX,GRT,FTM,EOS,SAND,MANA,EGLD,XTZ,AAVE,THETA,FLOW,AXS,NEO,RPL,CFX,USDP,PAX,LUNC,OP,KLAY,BSV,MINA";
        }
        String res2 = HttpUtil.doGet(baseUrl.concat("/mult-cup-tickers?t="+new Date().getTime() +"&ccys="+symbol));
        JSONObject jsonObject = JSONObject.parseObject(res2);
        return Response.success(jsonObject.get("data"));
    }


    @ApiOperation("新获币详情数据")
    @GetMapping(value = "/depth")
    public Response depth(String symbol) {
        if (!StringUtils.isBlank(symbol)){
            symbol=symbol.toUpperCase()+"-USDT";
            String res2 = HttpUtil.doGet(baseUrl.concat("/mult-tickers?t=" + new Date().getTime() + "&instIds=" + symbol));
            if (!StringUtils.isBlank(res2)) {
                JSONObject jsonObject = JSONObject.parseObject(res2);
                return Response.success(jsonObject.get("data"));
            }else {
                return Response.failure("链接没有数据"+baseUrl.concat("/mult-tickers?t=" + new Date().getTime() + "&instIds=" + symbol));
            }
        }
        return Response.success("");
    }

    @ApiOperation("新获币交易数据")
    @GetMapping(value = "/trade")
    public Response trade(String symbol) {
        if (!StringUtils.isBlank(symbol)){
            symbol=symbol.toUpperCase()+"-USDT";
            String res2 = HttpUtil.doGet(baseUrl.concat("/books?t="+new Date().getTime()+"&instId="+symbol+"&sz=10"));
            if (!StringUtils.isBlank(res2)) {
                JSONObject jsonObject = JSONObject.parseObject(res2);
                return Response.success(jsonObject.get("data"));
            }else {
                return Response.failure("链接没有数据"+baseUrl.concat("/books?t="+new Date().getTime()+"&instId="+symbol+"&sz=10"));
            }
        }
        return Response.success("");
    }

    @ApiOperation("新获币趋势数据")
    @GetMapping(value = "/trend")
    public Response trend(String symbol,String bar,Integer limit) {
        if (StringUtils.isBlank(bar)){bar="15m";}
        if (limit==0 || limit <0){limit=2000;}
        if (!StringUtils.isBlank(symbol)) {
            symbol = symbol.toUpperCase() + "-USDT";
            String res2 = HttpUtil.doGet(baseUrl.concat("/candles?instId="+symbol+"&bar="+bar+"&limit="+limit+"&t="+new Date().getTime()));
            if (!StringUtils.isBlank(res2)) {
                JSONObject jsonObject = JSONObject.parseObject(res2);
                return Response.success(jsonObject.get("data"));
            }else {
                return Response.failure("链接没有数据"+baseUrl.concat("/candles?instId="+symbol+"&bar="+bar+"&limit="+limit+"&t="+new Date().getTime()));
            }
        }
        return Response.success("");
    }

}
