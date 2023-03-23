package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DateUtil;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.job.mapper.RobotConfigMapper;
import cn.xa87.job.product.MatchProducer;
import cn.xa87.job.service.RobotService;
import cn.xa87.model.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RobotServiceImpl implements RobotService {
    private final static int orderCount = 12;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private MatchProducer matchProducer;
    @Autowired
    private RobotConfigMapper robotConfigMapper;

    public static List<BigDecimal> getRandomPriceBetweenMinAndMax(BigDecimal min, BigDecimal max, int count) {
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for (int i = 0; i < count; i++) {
            float minF = min.floatValue();
            float maxF = max.floatValue();
            // 生成随机数
            BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
            // 返回保留两位小数的随机数。不进行四舍五入
            list.add(db.setScale(8, BigDecimal.ROUND_DOWN));
        }
        return list;
    }

    public static List<BigDecimal> getRandomCountBetweenMinAndMax(BigDecimal min, BigDecimal max, int count) {

        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for (int i = 0; i < count; i++) {
            float minF = min.floatValue();
            float maxF = max.floatValue();
            // 生成随机数
            BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
            // 返回保留两位小数的随机数。不进行四舍五入
            list.add(db.setScale(8, BigDecimal.ROUND_DOWN));
        }
        return list;
    }

    public static List<BigDecimal> getRandomCountBetweenMinAndMaxMatch(int min, int max, int count) {

        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for (int i = 0; i < count; i++) {
            // 生成随机数
            int db = (int) (Math.random() * (max - min) + min);
            // 返回保留两位小数的随机数。不进行四舍五入
            list.add(new BigDecimal(db));
        }
        return list;
    }

    @Override
    public void putProjectKline(String param) {
        String[] params = param.split("-");
        String pairsName = params[0];
        BigDecimal startPrice = new BigDecimal(params[1]);
        BigDecimal endPrice = new BigDecimal(params[2]);
        BigDecimal startNum = new BigDecimal(params[3]);
        BigDecimal endNum = new BigDecimal(params[4]);

        BigDecimal nowPrice = getPrice(pairsName);

        List<BigDecimal> priceList = getRandomPriceBetweenMinAndMax(startPrice, endPrice, 1);
        List<BigDecimal> countList = getRandomCountBetweenMinAndMax(startNum, endNum, 1);

        Random random = new Random();
        int randomType = random.nextInt(2);
        if (randomType == 0) {
            nowPrice = nowPrice.add(priceList.get(0));
        } else {
            nowPrice = nowPrice.subtract(priceList.get(0));
        }

        BigDecimal count = countList.get(0);
        long timestamp = new Date().getTime();
        try {
            if (nowPrice.compareTo(BigDecimal.ZERO) > 0) {
                // 一分钟
                Long oneMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 1);
                String oneMinute = "1m";
                redisKline(pairsName, nowPrice, count, oneMinuteTimestamp, oneMinute);
                // 五分钟
                Long fiveMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 5);
                String fiveMinute = "5m";
                redisKline(pairsName, nowPrice, count, fiveMinuteTimestamp, fiveMinute);
                // 15分钟
                Long fifteenMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 15);
                String fifteenMinute = "15m";
                redisKline(pairsName, nowPrice, count, fifteenMinuteTimestamp, fifteenMinute);
                // 30分钟
                Long thirtyMinuteTimestamp = DateUtil.minuteTimestamp(timestamp, 30);
                String thirtyMinute = "30m";
                redisKline(pairsName, nowPrice, count, thirtyMinuteTimestamp, thirtyMinute);
                // 1小时
                Long oneHourTimestamp = DateUtil.hourTimestamp(timestamp);
                String oneHour = "1h";
                redisKline(pairsName, nowPrice, count, oneHourTimestamp, oneHour);
                // 一天
                Long oneDayTimestamp = DateUtil.dayTimestamp(timestamp);
                String oneDay = "1d";
                redisKline(pairsName, nowPrice, count, oneDayTimestamp, oneDay);
                // 一星期
                Long oneWeekTimestamp = DateUtil.weekTimestamp(timestamp, 1);
                String oneWeek = "1w";
                redisKline(pairsName, nowPrice, count, oneWeekTimestamp, oneWeek);
                //更新最新价格
                Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
                map.put("higPrice", nowPrice);
                map.put("lowPrice", nowPrice);
                map.put("nowPrice", nowPrice);
                map.put("updown", new BigDecimal("0"));
                map.put("volume", new BigDecimal("0"));
                map.put("open", nowPrice);
                //redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + pairsName, JSONObject.toJSONString(map));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void redisKline(String pairsName, BigDecimal nowPrice, BigDecimal count, long timestamp, String time){
        String key = CacheConstants.KLINE_KEY + time + CacheConstants.SPLIT + pairsName;
        Set<String> oneMinute = redisRepository.zsetRevRang(key, 0, 0);
        Iterator<String> oneMinuteIt = oneMinute.iterator();
        Kline kline = new Kline();
        if (oneMinuteIt.hasNext()) {
            String next = oneMinuteIt.next();
            JSONObject jsonObject = JSONObject.parseObject(next);
            Kline redisKline = JSONObject.toJavaObject(jsonObject, Kline.class);
            if (timestamp == redisKline.getTime()) {
                if (nowPrice.compareTo(redisKline.getHigh()) > 0) {
                    kline.setHigh(nowPrice);
                } else {
                    kline.setHigh(redisKline.getHigh());
                }
                if (nowPrice.compareTo(redisKline.getLow()) < 0) {
                    kline.setLow(nowPrice);
                } else {
                    kline.setLow(redisKline.getLow());
                }
                kline.setOpen(redisKline.getOpen());
                kline.setClose(nowPrice);
                kline.setTime(redisKline.getTime());
                kline.setVolume(redisKline.getVolume().add(count));
            } else {
                kline.setTime(timestamp);
                kline.setVolume(count);
                kline.setClose(nowPrice);
                kline.setLow(nowPrice);
                kline.setHigh(nowPrice);
                kline.setOpen(nowPrice);
            }
        } else {
            kline.setVolume(count);
            kline.setClose(nowPrice);
            kline.setLow(nowPrice);
            kline.setHigh(nowPrice);
            kline.setOpen(nowPrice);
            kline.setTime(timestamp);
        }
        redisRepository.zsetRmByScore(key, timestamp);
        Boolean bool = redisRepository.zsetAdd(key, JSONObject.toJSONString(kline), timestamp);
        if (!bool) {
            log.error("redis存储失败");
        }
    }

    @Override
    @Async
    public void robot() {

        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });


        QueryWrapper<RobotConfig> objectQueryWrapper = new QueryWrapper<RobotConfig>();
        objectQueryWrapper.eq("is_open", "OPEN");
        List<RobotConfig> robotConfigs = robotConfigMapper.selectList(objectQueryWrapper);

        for (RobotConfig config : robotConfigs) {
            String pairName = config.getPairName();
            String params = config.getParams();
            String openKine = config.getOpenKine();
            String openTape = config.getOpenTape();
            BigDecimal willPrice = config.getWillPrice();
            Date willTime = config.getWillTime();

            if (null != openKine && openKine.equals("OPEN")) {
                putProjectKline(params);
            }
            if (null != openTape && openTape.equals("OPEN")) {
                es.submit(new putMatch(params));
                es.submit(new putEntrust(params));
            }
            if (null != willTime) {
                long time = willTime.getTime();
                long l = System.currentTimeMillis();
                if (l > time) {
                    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
                    if (willPrice==null) {
                        continue;
                    }else{
                        map.put("higPrice", willPrice);
                        map.put("lowPrice", willPrice);
                        map.put("nowPrice", willPrice);
                        map.put("updown", new BigDecimal("0"));
                        map.put("volume", new BigDecimal("0"));
                        map.put("open", willPrice);
                    }

                    //redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + pairName, JSONObject.toJSONString(map));
                }
            }
        }
        es.shutdown();
    }

    class putEntrust implements Runnable {

        private String param;

        putEntrust(String param) {
            this.param = param;
        }

        @Override
        public void run() {
            try {
                //ETH/USDT   1.001   2.999    0.02    30   10   30
                String[] params = param.split("-");
                String pairsName = params[0];
                BigDecimal startPrice = new BigDecimal(params[1]);
                BigDecimal endPrice = new BigDecimal(params[2]);
                BigDecimal startNum = new BigDecimal(params[3]);
                BigDecimal endNum = new BigDecimal(params[4]);

                BigDecimal nowPrice = getPrice(pairsName);

                List<String> listOpenUp = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + pairsName, "robot");
                List<String> listDown = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + pairsName, "robot");
                for (String str : listOpenUp) {
                    Entrust entrust = JSONObject.parseObject(str, Entrust.class);
                    if (nowPrice.compareTo(entrust.getPrice()) == -1) {
                        redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + pairsName, str);
                    }
                }
                for (String str : listDown) {
                    Entrust entrust = JSONObject.parseObject(str, Entrust.class);
                    if (entrust.getPrice().compareTo(nowPrice) == -1) {
                        redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + pairsName, str);
                    }
                }
                List<BigDecimal> priceList = getRandomPriceBetweenMinAndMax(startPrice, endPrice, 4);
                List<BigDecimal> countList = getRandomCountBetweenMinAndMax(startNum, endNum, 4);
                for (int i = 0; i < priceList.size(); i++) {
                    BigDecimal openUpPrice = nowPrice.subtract(priceList.get(i));
                    if (openUpPrice.compareTo(BigDecimal.ZERO) > 0) {
                        Entrust entrust = new Entrust();
                        entrust.setUld("UP");
                        entrust.setId("robotIdEntrust");
                        entrust.setCount(countList.get(i));
                        entrust.setPairsName(pairsName);
                        entrust.setPrice(openUpPrice);
                        entrust.setEntrustType(EntrustConstant.Entrust_Type.BUY);
                        entrust.setMember("robotMemberId");
                        matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
                    }
                }

                if (listOpenUp.size() - orderCount > 0) {
                    for (int i = 0; i < listOpenUp.size() - orderCount; i++) {
                        redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + pairsName, listOpenUp.get(i));
                    }
                }
                List<BigDecimal> priceDownList = getRandomPriceBetweenMinAndMax(startPrice, endPrice, 4);
                List<BigDecimal> countDownList = getRandomCountBetweenMinAndMax(startNum, endNum, 4);
                for (int i = 0; i < priceDownList.size(); i++) {
                    BigDecimal openDownPrice = nowPrice.add(priceDownList.get(i));
                    if (openDownPrice.compareTo(BigDecimal.ZERO) > 0) {
                        Entrust entrust = new Entrust();
                        entrust.setUld("UP");
                        entrust.setId("robotIdEntrust");
                        entrust.setCount(countDownList.get(i));
                        entrust.setPairsName(pairsName);
                        entrust.setPrice(openDownPrice);
                        entrust.setEntrustType(EntrustConstant.Entrust_Type.SELL);
                        entrust.setMember("robotMemberId");
                        matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
                    }
                }
                if (listDown.size() - orderCount > 0) {
                    for (int i = 0; i < listDown.size() - orderCount; i++) {
                        redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + pairsName, listDown.get(i));
                    }
                }
                //实时成交
                Random random = new Random();
                int randomType = random.nextInt(2);
                BigDecimal priceResult = nowPrice.subtract(priceList.get(1));
                BigDecimal countResult = countList.get(1);
                EntrustConstant.Entrust_Type entrustType = null;
                if (randomType == 0) {
                    entrustType = EntrustConstant.Entrust_Type.BUY;
                } else {
                    entrustType = EntrustConstant.Entrust_Type.SELL;
                }
                int randomCount = random.nextInt(4);
                for (int i = 0; i < randomCount; i++) {
                    EntrustResult entrustResult = new EntrustResult(pairsName, countResult, priceResult, entrustType);
                    redisRepository.zsetAdd(CacheConstants.ENTRUST_ORDER_END_KEY + pairsName, JSONObject.toJSONString(entrustResult), System.currentTimeMillis());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class putMatch implements Runnable {

        private String param;

        putMatch(String param) {
            this.param = param;
        }

        @Override
        public void run() {
            try {
                //		ETH/USDT -  1.001  -  2.999  -  0.02  -  30 -10-30"
                String[] params = param.split("-");
                String pairsName = params[0];
                BigDecimal startPrice = new BigDecimal(params[1]);
                BigDecimal endPrice = new BigDecimal(params[2]);
                BigDecimal startNum = new BigDecimal(params[5]);
                BigDecimal endNum = new BigDecimal(params[6]);

                BigDecimal nowPrice = getPrice(pairsName);

                List<String> listOpenUp = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT + pairsName, "robotIdWarehouse");
                List<String> listDown = redisRepository.keyLikeValue(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + pairsName, "robotIdWarehouse");
                for (String str : listOpenUp) {
                    Match match = JSONObject.parseObject(str, Match.class);
                    if (nowPrice.compareTo(match.getPrice()) == -1) {
                        redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT + pairsName, str);
                    }
                }
                for (String str : listDown) {
                    Match match = JSONObject.parseObject(str, Match.class);
                    if (match.getPrice().compareTo(nowPrice) == -1) {
                        redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + pairsName, str);
                    }
                }
                List<BigDecimal> priceList = getRandomPriceBetweenMinAndMax(startPrice, endPrice, 2);
                List<BigDecimal> countList = getRandomCountBetweenMinAndMaxMatch(startNum.intValue(), endNum.intValue(), 2);
                for (int i = 0; i < priceList.size(); i++) {
                    BigDecimal openUpPrice = nowPrice.subtract(priceList.get(i));
                    if (openUpPrice.compareTo(BigDecimal.ZERO) > 0) {
                        Match match = new Match();
                        match.setWarehouse("robotIdWarehouse");
                        match.setOrderId("robotOrderId");
                        match.setCount(countList.get(i));
                        match.setPairsName(pairsName);
                        match.setPrice(openUpPrice);
                        match.setTradeType(ContractConstant.Trade_Type.OPEN_UP);
                        match.setMemberId("robotMemberId");
                        matchProducer.putMatch(JSONObject.toJSONString(match));
                    }
                }

                if (listOpenUp.size() - orderCount > 0) {
                    for (int i = 0; i < listOpenUp.size() - orderCount; i++) {
                        redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT + pairsName, listOpenUp.get(i));
                    }
                }
                List<BigDecimal> priceDownList = getRandomPriceBetweenMinAndMax(startPrice, endPrice, 2);
                List<BigDecimal> countDownList = getRandomCountBetweenMinAndMaxMatch(startNum.intValue(), endNum.intValue(), 2);
                for (int i = 0; i < priceDownList.size(); i++) {
                    BigDecimal openDownPrice = nowPrice.add(priceDownList.get(i));
                    if (openDownPrice.compareTo(BigDecimal.ZERO) > 0) {
                        Match match = new Match();
                        match.setWarehouse("robotIdWarehouse");
                        match.setOrderId("robotOrderId");
                        match.setCount(countDownList.get(i));
                        match.setPairsName(pairsName);
                        match.setPrice(openDownPrice);
                        match.setTradeType(ContractConstant.Trade_Type.OPEN_DOWN);
                        match.setMemberId("robotMemberId");
                        matchProducer.putMatch(JSONObject.toJSONString(match));
                    }

                }
                if (listDown.size() - orderCount > 0) {
                    for (int i = 0; i < listDown.size() - orderCount; i++) {
                        redisRepository.zsetRemove(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT + pairsName, listDown.get(i));
                    }
                }
                //实时成交
                Random random = new Random();
                int randomType = random.nextInt(2);
                BigDecimal priceResult = nowPrice.subtract(priceList.get(1));
                BigDecimal countResult = countList.get(1);
                ContractConstant.Trade_Type tradeType = null;
                if (randomType == 0) {
                    tradeType = ContractConstant.Trade_Type.OPEN_UP;
                } else {
                    tradeType = ContractConstant.Trade_Type.OPEN_DOWN;
                }
                int randomCount = random.nextInt(4);
                for (int i = 0; i < randomCount; i++) {
                    if (priceResult.compareTo(BigDecimal.ZERO) > 0) {
                        MatchResult matchResult = new MatchResult(pairsName, countResult, priceResult, tradeType);
                        redisRepository.zsetAdd(CacheConstants.TOKEN_ORDER_END_KEY + pairsName, JSONObject.toJSONString(matchResult), System.currentTimeMillis());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public BigDecimal getPrice(String pairName){
        String result = redisRepository.get(CacheConstants.INDEX_PRICE_KEY + pairName);
        BigDecimal nowPrice = BigDecimal.ZERO;
        if(null != result){
            nowPrice = new BigDecimal(result);
            return nowPrice;
        }
        String result1 = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairName);
        if(null != result1){
            JSONObject jsonInfo = JSONObject.parseObject(result1);
            nowPrice = jsonInfo.getBigDecimal("nowPrice");
            return nowPrice;
        }

        return nowPrice;
    }

}
