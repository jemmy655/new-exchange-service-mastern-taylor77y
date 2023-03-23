package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.constant.CoinConstant;
import cn.xa87.job.mapper.PairsMapper;
import cn.xa87.job.mapper.WarehouseMapper;
import cn.xa87.job.service.TokenService;
import cn.xa87.model.Pairs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public void upTokenOrder() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        wrapper.eq("type", CoinConstant.Coin_Type.MAIN_COIN);
        List<Pairs> list = pairsMapper.selectList(wrapper);
        for (Pairs pairs : list) {
            warehouseMapper.updateOrder1(pairs.getPrice().toPlainString(), pairs.getPairsName());
            warehouseMapper.updateOrder2(pairs.getPrice().toPlainString(), pairs.getPairsName());
        }

    }

    @Override
    public void getIndexPrice() {

        List<Pairs> pairsList = pairsMapper.selectList(new QueryWrapper<>());




        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });

        for (Pairs pairs : pairsList) {
            es.submit(new LoadIndexPrice(pairs.getPairsName()));
        }
        es.shutdown();

    }

    class LoadIndexPrice implements Runnable {

        private String pairName;

        private String symbol;

        /*LoadIndexPrice(String pairName) {
            this.pairName = pairName;
            this.symbol = pairName.replace("/USDT", "/USD");
        }
*//*
        @Override
        public void run() {

            /*
            String content = HttpUtil.doGet("https://api.huobi.pr/market/history/kline?symbol=" + symbol + "&period=1min&size=10");

            if (content == null) {
                log.info("接口返回空");
                return;
            }
            JSONObject json = JSONObject.parseObject(content);
            if (json == null || json.get("data") == null) {
                log.info("查询无数据");
                return;
            }
            JSONArray jsonArray = json.getJSONArray("data");
            for (Object obj : jsonArray) {
                JSONObject jsonInfo = JSONObject.parseObject(obj.toString());
                redisRepository.set(CacheConstants.INDEX_PRICE_KEY + pairName, jsonInfo.getString("close"));
            }
*/
/*
            String content = HttpUtil.doGet("https://data.block.cc/api/v3/kline?api_key=G1OZLXDGGXLWUABHHILY6IHYW4KMISX1XZMJAZTW&desc=gate-io_"+symbol+"&interval=1m");

            if (content == null) {
                log.info("接口返回空");
                return;
            }
            JSONArray jsonArray = JSONObject.parseArray(content);
            if (jsonArray == null|| jsonArray.isEmpty() ) {
                log.info("查询无数据");
                return;
            }
            for (Object obj : jsonArray) {
                JSONObject jsonInfo = JSONObject.parseObject(obj.toString());
                redisRepository.set(CacheConstants.INDEX_PRICE_KEY + pairName, jsonInfo.getString("c"));
            }
            *//*
            String pairsName = symbol;
            String url="https://api.polygon.io/v1/last/crypto/"+pairsName+"?apiKey=025JV3RuzkUZAiSjojXv37GWSRrp6kTK";
            //String url="https://api.polygon.io/v2/aggs/ticker/X:"+pairsName+"/prev?unadjusted=true&apiKey=025JV3RuzkUZAiSjojXv37GWSRrp6kTK";
            String content = HttpUtil.doGet(url);
            if (content == null) {
                log.info("接口返回空");
                return;
            }
            JSONObject json = JSONObject.parseObject(content);
            if (json == null || json.get("last") == null) {
                log.info(url+ "查询无数据");
                return;
            }
            JSONObject jsonInfo = json.parseObject("last");
            if (jsonInfo.get("price") != null) {
                redisRepository.set(CacheConstants.INDEX_PRICE_KEY + pairName, jsonInfo.getString("price"));

              }
        }*/
        LoadIndexPrice(String pairName) {
            this.pairName = pairName;
            this.symbol = pairName.replace("/", "").toLowerCase();
        }

        @Override
        public void run() {

            String content = HttpUtil.doGet("https://api.huobi.pro/market/history/kline?symbol=" + symbol + "&period=1min&size=10");

            if (content == null) {
                log.info("接口返回空");
                return;
            }
            JSONObject json = JSONObject.parseObject(content);
            if (json == null || json.get("data") == null) {
                log.info("查询无数据");
                return;
            }
            JSONArray jsonArray = json.getJSONArray("data");
            for (Object obj : jsonArray) {
                JSONObject jsonInfo = JSONObject.parseObject(obj.toString());
                redisRepository.set(CacheConstants.INDEX_PRICE_KEY + pairName, jsonInfo.getString("close"));
            }

        }
    }
}
