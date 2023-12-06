package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.ServiceNameConst;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.job.mapper.PairsMapper;
import cn.xa87.job.product.MatchProducer;
import cn.xa87.job.service.KlineJobService;
import cn.xa87.model.Pairs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class KlineServiceImpl implements KlineJobService {
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private MatchProducer matchProducer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public void ClearKline() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        List<Pairs> list = pairsMapper.selectList(wrapper);
        for (Pairs pairs : list) {
            List<String> resultSet = new ArrayList<String>(
                    redisRepository.zsetRevRang(CacheConstants.TOKEN_ORDER_END_KEY + pairs.getPairsName(), 0, -1));
            if (resultSet.size() > 50) {
                for (int i = 50; i < resultSet.size(); i++) {
                    redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_END_KEY + pairs.getPairsName(),
                            resultSet.get(i));
                }
            }
            List<String> resultEntrustSet = new ArrayList<String>(
                    redisRepository.zsetRevRang(CacheConstants.ENTRUST_ORDER_END_KEY + pairs.getPairsName(), 0, -1));
            if (resultEntrustSet.size() > 50) {
                for (int i = 50; i < resultEntrustSet.size(); i++) {
                    redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_END_KEY + pairs.getPairsName(),
                            resultEntrustSet.get(i));
                }
            }
            if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                redisRepository.del(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN
                        + CacheConstants.SPLIT + pairs.getPairsName());
                redisRepository.del(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                        + CacheConstants.SPLIT + pairs.getPairsName());

                List<String> listOpenUp = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                        + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + pairs.getPairsName(), "robot");
                List<String> listDown = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                        + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + pairs.getPairsName(), "robot");
                for (String str : listOpenUp) {
                    redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY
                            + CacheConstants.SPLIT + pairs.getPairsName(), str);
                }
                for (String str : listDown) {
                    redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL
                            + CacheConstants.SPLIT + pairs.getPairsName(), str);
                }
            }


            delKline(CacheConstants.KLINE_KEY + "1m" + CacheConstants.SPLIT + pairs.getPairsName(), 5000L);
            delKline(CacheConstants.KLINE_KEY + "5m" + CacheConstants.SPLIT + pairs.getPairsName(), 2000L);
            delKline(CacheConstants.KLINE_KEY + "15m" + CacheConstants.SPLIT + pairs.getPairsName(), 1000L);
            delKline(CacheConstants.KLINE_KEY + "30m" + CacheConstants.SPLIT + pairs.getPairsName(), 1000L);
            delKline(CacheConstants.KLINE_KEY + "1h" + CacheConstants.SPLIT + pairs.getPairsName(), 1000L);
            delKline(CacheConstants.KLINE_KEY + "1d" + CacheConstants.SPLIT + pairs.getPairsName(), 1000L);
            delKline(CacheConstants.KLINE_KEY + "1w" + CacheConstants.SPLIT + pairs.getPairsName(), 1000L);
        }
    }

    public void delKline(String key, long count) {
        Long size = redisRepository.zsetCount(key);
        if (size > count) {
            redisRepository.zsetRemoveRange(key, 0, size - count - 1);
        }
    }

    private Long getLastScore(String timeType, String key) throws ParseException {
        Set<ZSetOperations.TypedTuple<String>> set = redisRepository.zsetRangWithScore(key, -1, -1);
        String score = "0";
        DecimalFormat df = new DecimalFormat("#");
        if (set.iterator().hasNext()) {
            ZSetOperations.TypedTuple<String> tuple = set.iterator().next();
            score = df.format(tuple.getScore());
        } else {
            if ("1w".equals(timeType)) {
                score = new SimpleDateFormat("yyyyMM01").format(new Date()) + "000000";
            } else {
                score = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "000000";
            }
        }
        return Long.parseLong(score);
    }

    @Override
    public void cnyPrice() {
        // 查询所有交易对
        Map<String, BigDecimal> mainCurMap = new HashMap<String, BigDecimal>();
        mainCurMap.put("USDT", new BigDecimal("6.4704"));
        try {
            List<String> mainCurs = pairsMapper.getMainCurs();
            for (String mainCur : mainCurs) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat dfNow = new SimpleDateFormat("yyyyMMddHHmm00");// 设置日期格式
                long now = sdf.parse(dfNow.format(new Date())).getTime();
                String content = HttpUtil.doGet("https://api.zb.com/data/v1/kline?market=" + mainCur.replace("/", "").toLowerCase() + "_QC" + "&type=1min&size=1&since=" + now);
                JSONObject json = JSONObject.parseObject(content);
                if (json == null || json.get("data") == null) {
                    log.info("https://api.zb.com/data/v1/kline?market=" + mainCur.replace("/", "").toLowerCase() + "_QC" + "&type=1min&size=1&since=" + now + "查询无数据");
                    continue;
                }
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.size() > 0) {
                    JSONArray jsonInfo = JSONArray.parseArray(jsonArray.get(jsonArray.size() - 1).toString());
                    JSONObject jsonRsult = new JSONObject();
                    jsonRsult.put("volume", jsonInfo.get(5).toString());
                    jsonRsult.put("high", jsonInfo.get(2).toString());
                    jsonRsult.put("low", jsonInfo.get(3).toString());
                    jsonRsult.put("time", jsonInfo.get(0).toString());
                    jsonRsult.put("close", jsonInfo.get(4).toString());
                    jsonRsult.put("open", jsonInfo.get(1).toString());
                    mainCurMap.put(mainCur, new BigDecimal(jsonInfo.get(4).toString()));
                    redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + mainCur, jsonRsult.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mainCurMap.isEmpty()) {
            QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
            wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
            wrapper.eq("pairs_type", 1);
            List<Pairs> list = pairsMapper.selectList(wrapper);

            ExecutorService threadPool = Executors.newFixedThreadPool(5);
            for (Pairs pairs : list) {
                try {
                    updatePairs(pairs, mainCurMap.get(pairs.getMainCur()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
				/*threadPool.execute(new Runnable() {
					public void run() {
						try {
							updatePairs(pairs, mainCurMap.get(pairs.getMainCur()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});*/
            }
            threadPool.shutdown();
        }

    }


    public void updatePairs(Pairs pairs, BigDecimal mainCurPrice) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // 更新成交量--涨跌幅--最新价--开盘价
        SimpleDateFormat dfMax = new SimpleDateFormat("yyyyMMddHHmm00");// 设置日期格式
        long max = sdf.parse(dfMax.format(new Date())).getTime();

        SimpleDateFormat sdfMin = new SimpleDateFormat("yyyyMMdd000000");
        long min = sdf.parse(sdfMin.format(new Date())).getTime();

        Set<String> sets = redisRepository.zsetRevRangByScore(CacheConstants.KLINE_KEY + "1m" + CacheConstants.SPLIT + pairs.getPairsName(), min, max);

        BigDecimal nowPrice = new BigDecimal("0");
        if (!sets.isEmpty()) {
            BigDecimal volume = new BigDecimal("0");
            BigDecimal open = new BigDecimal("0");
            List<BigDecimal> priceList = new ArrayList<BigDecimal>();
            for (String str : sets) {
                JSONObject jsonInfo = JSONObject.parseObject(str);
                volume = volume.add(new BigDecimal(jsonInfo.getString("volume")));
                open = new BigDecimal(jsonInfo.getString("open"));
                priceList.add(new BigDecimal(jsonInfo.getString("high")));
                priceList.add(new BigDecimal(jsonInfo.getString("low")));
            }
            Collections.sort(priceList);
            nowPrice = new BigDecimal(JSONObject.parseObject(sets.iterator().next()).getString("close"));// 当前价
            /**
             * {
             *  "higPrice":"1000",
             *  "lowPrice":"1000",
             *  "nowPrice":"1000",
             *  "updown":"1000",
             *  "volume":"1000",
             *  "open":"1000"
             *  }
             */
            Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
            map.put("higPrice", priceList.get(priceList.size() - 1));
            map.put("lowPrice", priceList.get(0));
            map.put("nowPrice", nowPrice);
            map.put("updown", nowPrice.subtract(open).divide(open, 8, BigDecimal.ROUND_HALF_UP));
            map.put("volume", volume);
            map.put("open", open);
            if (pairs.getMainCur().equals(ServiceNameConst.MAIN_CUR)) {
                map.put("chPrice", nowPrice);
            } else {
                map.put("chPrice", mainCurPrice.multiply(nowPrice));
            }
            redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName(), JSONObject.toJSONString(map));
            pairs.setHigPrice(map.get("higPrice"));
            pairs.setLowPrice(map.get("lowPrice"));
            pairs.setPrice(map.get("nowPrice"));
            pairs.setUpdown(map.get("updown"));
            pairs.setVolume(map.get("volume"));
            pairs.setOpen(map.get("open"));
            pairs.setChPrice(map.get("chPrice"));
        } else {
            if (pairs.getPrice() == null) {
                nowPrice = pairs.getOpenPrice();// 当前价
            } else {
                nowPrice = pairs.getPrice();// 当前价
            }
            if (nowPrice == null) {
                return;
            }
            Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
            map.put("higPrice", nowPrice);
            map.put("lowPrice", nowPrice);
            map.put("nowPrice", nowPrice);
            map.put("updown", new BigDecimal("0"));
            map.put("volume", new BigDecimal("0"));
            map.put("open", nowPrice);
            if (pairs.getMainCur().equals(ServiceNameConst.MAIN_CUR)) {
                map.put("chPrice", nowPrice);
            } else {
                map.put("chPrice", mainCurPrice.multiply(nowPrice));
            }
            redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName(), JSONObject.toJSONString(map));
            pairs.setHigPrice(map.get("higPrice"));
            pairs.setLowPrice(map.get("lowPrice"));
            pairs.setPrice(map.get("nowPrice"));
            pairs.setUpdown(map.get("updown"));
            pairs.setVolume(map.get("volume"));
            pairs.setOpen(map.get("open"));
            pairs.setChPrice(map.get("chPrice"));
        }
        pairsMapper.updateById(pairs);
        if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
            matchProducer.putTokenPrice(nowPrice.toPlainString() + "-" + pairs.getPairsName());
            matchProducer.putProfitlossPrice(nowPrice.toPlainString() + "-" + pairs.getPairsName());
            matchProducer.putContractPrice(nowPrice.toPlainString() + "-" + pairs.getPairsName());
            //matchProducer.putEntrustPrice(nowPrice.toPlainString() + "-" + pairs.getPairsName());
            // 标记
        }
    }

    class getInitMatch implements Runnable {

        private Pairs pairs;

        getInitMatch(Pairs pairs) {
            this.pairs = pairs;
        }

        @Override
        public void run() {
            try {
                initkline("1m", pairs);
                initkline("5m", pairs);
                initkline("15m", pairs);
                initkline("30m", pairs);
                initkline("1d", pairs);
                initkline("1w", pairs);
            } catch (Exception e) {
                log.error("执行数据出现异常", e);
            }
        }

    }

    public void initkline(String timeType, Pairs pairs) {
       /* String redisKey = CacheConstants.KLINE_KEY + timeType + CacheConstants.SPLIT + pairs.getPairsName();
        String pairsName = pairs.getPairsName().replace("/USDT", "USD");
        String sdate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String period="";

        switch (timeType) {
            case "1m":
                period="range/1/minute";
                break;
            case "5m":
                period="range/5/minute";
                break;
            case "15m":
                period="range/15/minute";
                break;
            case "30m":
                period="range/30/minute";
                break;
            case "1h":
                period="range/60/minute";
                break;
            case "1d":
                period="range/1/day";
                break;
            case "1w":
                period="range/7/day";
                break;
            default:
                break;
        }
*/
        //"https://data.block.cc/api/v3/kline?api_key=G1OZLXDGGXLWUABHHILY6IHYW4KMISX1XZMJAZTW&desc=gate-io_BTC_USDT&interval=1m"
        //String content = HttpUtil.doGet("https://api.huobi.pr/market/history/kline?symbol="+pairsName+"&period="+period+"&size=10");
        /*String content = HttpUtil.doGet("https://data.block.cc/api/v3/kline?api_key=G1OZLXDGGXLWUABHHILY6IHYW4KMISX1XZMJAZTW&desc=gate-io_"+pairsName+"&interval="+period);

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

            JSONObject jsonInfo =JSONObject.parseObject(obj.toString());
            jsonInfo.put("volume", jsonInfo.get("v"));
            jsonInfo.put("open", jsonInfo.get("o"));
            jsonInfo.put("close", jsonInfo.get("c"));
            jsonInfo.put("high", jsonInfo.get("h"));
            jsonInfo.put("low", jsonInfo.get("l"));
            long time=Long.parseLong(jsonInfo.get("T")+"");
            jsonInfo.put("time", time);
            redisRepository.zsetRmByScore(redisKey, time);
            redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), time);
        }*/
/*

        String url="https://api.polygon.io/v2/aggs/ticker/X:"+pairsName+"/"+period+"/2020-10-01/"+sdate+"?unadjusted=true&sort=desc&limit=120&apiKey=025JV3RuzkUZAiSjojXv37GWSRrp6kTK";
        String content = HttpUtil.doGet(url);
        if (content == null) {
            log.info("接口返回空");
            return;
        }
        JSONObject json = JSONObject.parseObject(content);
        if (json == null || json.get("results") == null) {
            log.info(url+ "查询无数据");
            return;
        }
        JSONArray jsonArray = json.getJSONArray("results");
        if (jsonArray.size() > 0) {
            for (Object obj : jsonArray) {
                JSONObject jsonInfo =JSONObject.parseObject(obj.toString());
                jsonInfo.put("volume", jsonInfo.get("v"));
                jsonInfo.put("open", jsonInfo.get("o"));
                jsonInfo.put("close", jsonInfo.get("c"));
                jsonInfo.put("high", jsonInfo.get("h"));
                jsonInfo.put("low", jsonInfo.get("l"));
                long time=Long.parseLong(jsonInfo.get("t")+"");
                jsonInfo.put("time", time);
                redisRepository.zsetRmByScore(redisKey, time);
                redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), time);

            }
        }*/
        /*火币API
        JSONObject json = JSONObject.parseObject(content);
        if (json == null || json.get("data") == null) {
            log.info("查询无数据");
            return;
        }
        JSONArray jsonArray = json.getJSONArray("data");
        for (Object obj : jsonArray) {
            JSONObject jsonInfo =JSONObject.parseObject(obj.toString());
            jsonInfo.put("volume", jsonInfo.get("amount"));
            long time=Long.parseLong(jsonInfo.get("id")+"000");
            jsonInfo.put("time", time);
            redisRepository.zsetRmByScore(redisKey, time);
            redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), time);
        }*/
        String redisKey = CacheConstants.KLINE_KEY + timeType + CacheConstants.SPLIT + pairs.getPairsName();
        String pairsName = pairs.getPairsName().replace("/", "").toLowerCase();
        String period = "";
        switch (timeType) {
            case "1m":
                period = "1min";
                break;
            case "5m":
                period = "5min";
                break;
            case "15m":
                period = "15min";
                break;
            case "30m":
                period = "30min";
                break;
            case "1h":
                period = "60min";
                break;
            case "1d":
                period = "1day";
                break;
            case "1w":
                period = "1week";
                break;
            default:
                break;
        }

        String content = HttpUtil.doGet("https://api.huobi.pro/market/history/kline?symbol=" + pairsName + "&period=" + period + "&size=10");

//        log.info(content);

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
            jsonInfo.put("volume", jsonInfo.get("amount"));
            long time = Long.parseLong(jsonInfo.get("id") + "000");
            jsonInfo.put("time", time);
            redisRepository.zsetRmByScore(redisKey, time);
            redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), time);
        }
    }

    ThreadPoolExecutor es = new ThreadPoolExecutor(7, 10, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    }, new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void synchKline() {
        getInitMatch match;
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        wrapper.ne("token_cur", "USDT");
        List<Pairs> list = pairsMapper.selectList(wrapper);
        log.info("K线图抓取执行===》List--> {}", list.size());
        for (Pairs p : list) {
            match = new getInitMatch(p);
            match.run();
//            es.submit(new getInitMatch(p));
        }
        es.shutdown();
    }

    @Override
    public void trend() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        wrapper.eq("pairs_type", 1);
        List<Pairs> pairsList = pairsMapper.selectList(wrapper);
        pairsList.forEach(item -> {
            String res2 = HttpUtil.doGet("https://www.okx.com/priapi/v5/market/mult-tickers?t="+new Date().getTime()+"&instIds="+item.getTokenCur()+"-USDT");
            if (res2!=null) {
                System.out.println("请求的数据api"+"https://www.okx.com/priapi/v5/market/mult-tickers?t="+new Date().getTime()+"&instIds="+item.getTokenCur()+"-USDT");
                System.out.println("请求的数据"+res2);
                JSONObject object = JSONObject.parseObject(res2);
                List<JSONObject> list= (List<JSONObject>) object.get("data");
                if (list.size()>0) {
                    data(item, list.get(0));
                }
            }
        });
    }

    @Async
    public void data(Pairs pairs, JSONObject red) {

        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("higPrice", new BigDecimal(red.get("high24h").toString()));
        map.put("lowPrice", new BigDecimal(red.get("low24h").toString()));
        map.put("nowPrice", new BigDecimal(red.get("last").toString()));
        map.put("updown", new BigDecimal(0));
        map.put("volume", new BigDecimal(red.get("lastSz").toString()));
        map.put("open", new BigDecimal(red.get("open24h").toString()));
        map.put("chPrice", new BigDecimal(red.get("last").toString()).multiply(new BigDecimal("6.4704")));

        redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName(), JSONObject.toJSONString(map));

        pairs.setHigPrice(map.get("higPrice"));
        pairs.setLowPrice(map.get("lowPrice"));
        pairs.setPrice(map.get("nowPrice"));
        pairs.setUpdown(map.get("updown"));
        pairs.setVolume(map.get("volume"));
        pairs.setOpen(map.get("open"));
        pairs.setChPrice(map.get("chPrice"));
        pairsMapper.updateById(pairs);
    }

}
