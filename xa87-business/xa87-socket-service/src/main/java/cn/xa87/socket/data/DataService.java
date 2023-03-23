package cn.xa87.socket.data;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.model.Entrust;
import cn.xa87.model.Match;
import cn.xa87.model.Pairs;
import cn.xa87.socket.mapper.PairsMapper;
import cn.xa87.socket.server.WebSocketServer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

@EnableScheduling
@Order(value = 1)
@Component
@Service
public class DataService implements ApplicationRunner {

    //k线最新数据
    public ConcurrentHashMap<String, Set<String>> map_kline = new ConcurrentHashMap<String, Set<String>>();
    //盘口最新数据
    public ConcurrentHashMap<String, Object> map_match = new ConcurrentHashMap<String, Object>();
    //委托最新数据
    public ConcurrentHashMap<String, Object> map_entrust = new ConcurrentHashMap<String, Object>();
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        KlineInitLoad();
        EntrustLoad();
        // 开启netty服务
        (new WebSocketServer()).start(this);
    }

    @Scheduled(fixedRate = 1000)
    public void KlineInitLoad() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        List<Pairs> pairs = pairsMapper.selectList(wrapper);
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (Pairs pair : pairs) {
            es.submit(new LoadInitKline(pair.getPairsName(), redisRepository));
        }
        es.shutdown();
    }

    @Scheduled(fixedRate = 1000)
    public void MatchLoad() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        List<Pairs> pairs = pairsMapper.selectList(wrapper);
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (Pairs pair : pairs) {
            es.submit(new LoadInitMatch(pair.getPairsName(), redisRepository));
        }
        es.shutdown();
    }

    @Scheduled(fixedRate = 2000)
    public void EntrustLoad() {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        List<Pairs> pairs = pairsMapper.selectList(wrapper);
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (Pairs pair : pairs) {
            es.submit(new LoadInitEntrust(pair.getPairsName(), redisRepository));
        }
        es.shutdown();
    }

    public Set<String> getKlineByTime(String pairsName, String timeType, Long start, Long end) {
        Set<String> set = redisRepository
                .zsetRangByScore(CacheConstants.KLINE_KEY + timeType + CacheConstants.SPLIT + pairsName, start, end);
        return set;
    }

    public Set<String> getInintKline(String pairsName) {
        return map_kline.get(pairsName);
    }

    class LoadInitEntrust implements Runnable {

        private String pairsName;

        private Xa87RedisRepository redisRepository;

        LoadInitEntrust(String pairsName, Xa87RedisRepository redisRepository) {
            this.pairsName = pairsName;
            this.redisRepository = redisRepository;
        }

        @Override
        public void run() {
            Map<String, Object> entrust_end_map = new HashMap<String, Object>();
            Set<String> buy_set = redisRepository.zsetRevRang(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT
                    + pairsName, 0, -1);
            Map<BigDecimal, BigDecimal> map_buy = new TreeMap<>((k1, k2) -> {
                return k2.compareTo(k1);
            });

            for (String str : buy_set) {
                Entrust entrust = JSONObject.parseObject(str, Entrust.class);
                if (!map_buy.containsKey(entrust.getPrice())) {
                    map_buy.put(entrust.getPrice(), entrust.getCount());
                } else {
                    map_buy.put(entrust.getPrice(),
                            entrust.getCount().add(map_buy.get(entrust.getPrice())));
                }
            }
            entrust_end_map.put("buy", map_buy);

            Set<String> sell_set = redisRepository.zsetRang(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT
                    + pairsName, 0, -1);
            Map<BigDecimal, BigDecimal> map_sell = new TreeMap<>((k1, k2) -> {
                return k1.compareTo(k2);
            });
            for (String str : sell_set) {
                Entrust entrust = JSONObject.parseObject(str, Entrust.class);
                if (!map_sell.containsKey(entrust.getPrice())) {
                    map_sell.put(entrust.getPrice(), entrust.getCount());
                } else {
                    map_sell.put(entrust.getPrice(),
                            entrust.getCount().add(map_sell.get(entrust.getPrice())));
                }
            }
            entrust_end_map.put("sell", map_sell);
            Set<String> entrust_end_set = redisRepository.zsetRevRang(CacheConstants.ENTRUST_ORDER_END_KEY + pairsName, 0, 50);
            entrust_end_map.put("entrustList", entrust_end_set);
            map_entrust.put(pairsName, entrust_end_map);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    class LoadInitMatch implements Runnable {

        private String pairsName;

        private Xa87RedisRepository redisRepository;

        LoadInitMatch(String pairsName, Xa87RedisRepository redisRepository) {
            this.pairsName = pairsName;
            this.redisRepository = redisRepository;
        }

        @Override
        public void run() {
            Map<String, Object> match_end_map = new HashMap<String, Object>();
            Set<String> open_up_set = redisRepository.zsetRevRang(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP + CacheConstants.SPLIT
                    + pairsName, 0, -1);
            Map<BigDecimal, BigDecimal> map_open_up = new TreeMap<>((k1, k2) -> {
                return k2.compareTo(k1);
            });

            for (String str : open_up_set) {
                Match match = JSONObject.parseObject(str, Match.class);
                if (!map_open_up.containsKey(match.getPrice())) {
                    map_open_up.put(match.getPrice(), match.getCount());
                } else {
                    map_open_up.put(match.getPrice(),
                            match.getCount().add(map_open_up.get(match.getPrice())));
                }
            }
            match_end_map.put("openup", map_open_up);
            Set<String> open_down_set = redisRepository.zsetRang(CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN + CacheConstants.SPLIT
                    + pairsName, 0, -1);
            Map<BigDecimal, BigDecimal> map_open_down = new TreeMap<>((k1, k2) -> {
                return k1.compareTo(k2);
            });
            for (String str : open_down_set) {
                Match match = JSONObject.parseObject(str, Match.class);
                if (!map_open_down.containsKey(match.getPrice())) {
                    map_open_down.put(match.getPrice(), match.getCount());
                } else {
                    map_open_down.put(match.getPrice(),
                            match.getCount().add(map_open_down.get(match.getPrice())));
                }
            }
            match_end_map.put("opendown", map_open_down);
            Set<String> match_end_set = redisRepository.zsetRevRang(CacheConstants.TOKEN_ORDER_END_KEY
                    + pairsName, 0, 50);
            match_end_map.put("matchList", match_end_set);
            map_match.put(pairsName, match_end_map);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    class LoadInitKline implements Runnable {

        private String pairsName;

        private Xa87RedisRepository redisRepository;

        LoadInitKline(String pairsName, Xa87RedisRepository redisRepository) {
            this.pairsName = pairsName;
            this.redisRepository = redisRepository;
        }

        @Override
        public void run() {
            Set<String> set_1m = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "1m" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_5m = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "5m" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_15m = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "15m" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_30m = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "30m" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_1h = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "1h" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_1d = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "1d" + CacheConstants.SPLIT + pairsName, 0, 0);
            Set<String> set_1w = redisRepository.zsetRevRang(CacheConstants.KLINE_KEY + "1w" + CacheConstants.SPLIT + pairsName, 0, 0);
            map_kline.put(pairsName + "-1m", set_1m);
            map_kline.put(pairsName + "-5m", set_5m);
            map_kline.put(pairsName + "-15m", set_15m);
            map_kline.put(pairsName + "-30m", set_30m);
            map_kline.put(pairsName + "-1h", set_1h);
            map_kline.put(pairsName + "-1d", set_1d);
            map_kline.put(pairsName + "-1w", set_1w);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
