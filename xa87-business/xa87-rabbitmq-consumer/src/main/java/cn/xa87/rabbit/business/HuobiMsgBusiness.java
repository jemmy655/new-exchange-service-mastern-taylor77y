package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HuobiMsgBusiness {
    @Autowired
    private Xa87RedisRepository redisRepository;

    public void execute(String msg) {
        try {
            if (msg.contains("tick")) {
                JSONObject jsonObject = JSONObject.parseObject(msg);
                String ch = jsonObject.getString("ch");
                String[] chs = ch.split("\\.");
                String pairs = chs[1];

                String nwPairs = pairs.substring(0, pairs.length() - 4) + "/" + pairs.substring(pairs.length() - 4, pairs.length());
                String period = "";
                switch (chs[3]) {
                    case "1min":
                        period = "1m";
                        break;
                    case "5min":
                        period = "5m";
                        break;
                    case "15min":
                        period = "15m";
                        break;
                    case "30min":
                        period = "30m";
                        break;
                    case "60min":
                        period = "1h";
                        break;
                    case "1day":
                        period = "1d";
                        break;
                    case "1week":
                        period = "1w";
                        break;
                    default:
                        break;
                }

                String redisKey = CacheConstants.KLINE_KEY + period + CacheConstants.SPLIT + nwPairs.toUpperCase();
                JSONObject jsonObjectTick = JSONObject.parseObject(jsonObject.getString("tick"));
                long time = Long.parseLong(jsonObjectTick.get("id") + "000");
                jsonObjectTick.put("volume", jsonObjectTick.get("amount"));
                jsonObjectTick.put("time", time);
                redisRepository.zsetRmByScore(redisKey, time);
                redisRepository.zsetAdd(redisKey, jsonObjectTick.toJSONString(), time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
