package cn.xa87.socket.test;

import cn.xa87.common.utils.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class test2 {
    public static void main(String [] args) {
        // 查询所有交易对

        try {
            String period = "1m";
            List<String> mainCurs = new ArrayList<String>();
            mainCurs.add("BTCUSD");
            for (String mainCur : mainCurs) {

                String pairsName = mainCur.replace("/USDT", "USD");
                String url = "https://api.polygon.io/v2/aggs/ticker/X:" + pairsName + "/prev?unadjusted=true&apiKey=025JV3RuzkUZAiSjojXv37GWSRrp6kTK";
                String content = HttpUtil.doGet(url);
                System.out.println(content);
                if (content == null) {

                    continue;
                }
                JSONObject json = JSONObject.parseObject(content);
                if (json == null || json.get("results") == null) {
                    System.out.println(json);
                    continue;
                }
                JSONArray jsonArray = json.getJSONArray("results");
                if (jsonArray.size() > 0) {
                    JSONObject jsonInfo = JSONArray.parseObject(jsonArray.get(jsonArray.size() - 1).toString());
                    JSONObject jsonRsult = new JSONObject();
                    jsonRsult.put("volume", jsonInfo.get("v"));
                    jsonRsult.put("high", jsonInfo.get("h"));
                    jsonRsult.put("low", jsonInfo.get("l"));
                    jsonRsult.put("time", jsonInfo.get("t"));
                    jsonRsult.put("close", jsonInfo.get("c"));
                    jsonRsult.put("open", jsonInfo.get("o"));
                    System.out.println(jsonRsult.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
