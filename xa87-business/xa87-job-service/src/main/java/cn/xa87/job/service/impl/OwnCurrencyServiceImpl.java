package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.CoinConstant;
import cn.xa87.job.mapper.CurrencyControlsMapper;
import cn.xa87.job.mapper.KlineDataMapper;
import cn.xa87.job.mapper.PairsMapper;
import cn.xa87.job.service.OwnCurrencyService;
import cn.xa87.job.utils.RandomUtils;
import cn.xa87.model.CurrencyControls;
import cn.xa87.model.KlineData;
import cn.xa87.model.Pairs;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OwnCurrencyServiceImpl implements OwnCurrencyService {

    @Resource
    private Xa87RedisRepository redisRepository;
    @Resource
    private PairsMapper pairsMapper;
    @Resource
    private KlineDataMapper klineDataMapper;
    @Resource
    private CurrencyControlsMapper currencyControlsMapper;
    @Override
    public void generateCandlestickData() {
        List<CurrencyControls> currencyControlsList = currencyControlsMapper.findCurrencyControlsList();
        for (CurrencyControls currencyControls : currencyControlsList) {
            KlineData klineData = klineDataMapper.findKlineData(currencyControls.getTokenCur());
            if(null != klineData){
                String biKey = klineData.getTokenCur() + "/USDT".toUpperCase();

                Map<String, BigDecimal> map = new HashMap<>();
                map.put("higPrice", klineData.getPrice());
                map.put("lowPrice", klineData.getPrice());
                map.put("nowPrice", klineData.getPrice());
                map.put("updown", klineData.getPrice().subtract(klineData.getOpen()).divide(klineData.getOpen(), 8, BigDecimal.ROUND_HALF_UP));
                map.put("volume", klineData.getVolume());
                map.put("open", klineData.getOpen());
                map.put("chPrice", klineData.getPrice());
                redisRepository.set(CacheConstants.PRICE_HIG_LOW_KEY + biKey, JSONObject.toJSONString(map));

                klineData.setStatus(2);
                klineDataMapper.updateById(klineData);

                QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
                wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
                wrapper.eq("pairs_type",2);
                wrapper.eq("token_cur",klineData.getTokenCur());
                List<Pairs> pairsList = pairsMapper.selectList(wrapper);
                if(!pairsList.isEmpty()){
                    Pairs pairs = pairsList.get(0);

                    pairs.setHigPrice(map.get("higPrice"));
                    pairs.setLowPrice(map.get("lowPrice"));
                    pairs.setPrice(map.get("nowPrice"));
                    pairs.setUpdown(map.get("updown"));
                    pairs.setVolume(map.get("volume"));
                    pairs.setOpen(map.get("open"));
                    pairs.setChPrice(map.get("chPrice"));
                    pairsMapper.updateById(pairs);

                    // 生成交易数据
                    int next = RandomUtils.next(2, 6);
                    boolean oddOrEven = getOddOrEven(next);
                    if(oddOrEven){
                        saveEntrustRedis(klineData, biKey, "BUY");
                    }else {
                        int next2 = RandomUtils.next(2, 5);
                        boolean oddOrEven2 = getOddOrEven(next2);
                        if(oddOrEven2) {
                            saveEntrustRedis(klineData, biKey, "SELL");
                        }
                    }
                }
            }
        }
    }

    private void saveEntrustRedis(KlineData klineData, String biKey, String type) {
        Double next = RandomUtils.next(0.01, 0.09);
        JSONObject entrustJson = new JSONObject();
        entrustJson.put("count", klineData.getCount() - next);
        entrustJson.put("entrustType", type);
        entrustJson.put("id", "robotIdEntrust");
        entrustJson.put("member", "robotMemberId");
        entrustJson.put("pairsName", biKey);
        entrustJson.put("price", klineData.getPrice());
        entrustJson.put("sort", "10000000000");
        entrustJson.put("uld", "UP");

        long time = klineData.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));

        redisRepository.zsetAdd(
                CacheConstants.ENTRUST_ORDER_MATCH_KEY + type + CacheConstants.SPLIT
                        + biKey,
                entrustJson.toJSONString(), time);
    }


    // 判断是奇数还是偶数
    private static boolean getOddOrEven(int in){
        return in % 2 == 0;
    }
}
