package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.CoinConstant.Get_Coin_Sort;
import cn.xa87.constant.CoinConstant.Get_Coin_Type;
import cn.xa87.constant.DataConstant;
import cn.xa87.data.controller.apidata;
import cn.xa87.data.controller.msgdata;
import cn.xa87.data.mapper.*;
import cn.xa87.data.service.DataService;
import cn.xa87.data.service.MD5Util;
import cn.xa87.model.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
// 分支
@Slf4j
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private WalletPoolMapper walletPoolMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private DepositHistoryMapper depositHistoryMapper;
    @Override
    public List<Banner> getBanners(DataConstant.Banner_Type bannerType, DataConstant.Global_Type global) {
        QueryWrapper<Banner> wrapper = new QueryWrapper<Banner>();
        if (null != bannerType) {
            wrapper.eq("banner_type", bannerType);
        }
        if (null != global) {
            wrapper.eq("global", global);
        }
        List<Banner> banners = bannerMapper.selectList(wrapper);
        return banners;
    }


    @Override
    public List<Notice> getNotices(DataConstant.Notice_Type noticeType, DataConstant.Global_Type global) {
        QueryWrapper<Notice> wrapper = new QueryWrapper<Notice>();
        if (Strings.isNotBlank(noticeType.getNoticeType())) {
            wrapper.eq("notice_type", noticeType);
        }
        if (Strings.isNotBlank(global.getGlobalType())) {
            wrapper.eq("global", global.getGlobalType());
        }
        wrapper.orderByDesc("create_time");
        List<Notice> notices = noticeMapper.selectList(wrapper);
        return notices;
    }

    @Override
    public List<Pairs> getIndexCoin(Get_Coin_Type getCoinType) {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        if(!getCoinType.getGetCoinType().equals("TRANSFER")){
            wrapper.ne("token_cur","USDT");
        }

        switch (getCoinType.getGetCoinType()) {
            case "TOP":
                wrapper.eq("is_top", true);
                break;
            case "UPDOWN":
                wrapper.orderByDesc("updown");
                break;
            case "VOLUME":
                wrapper.orderByAsc("volume");
                break;
            case "PROJECT":
                wrapper.eq("type", CoinConstant.Coin_Type.PROJECT_COIN);
                break;
            default:
                break;
        }
        wrapper.orderByDesc("sort");
        List<Pairs> pairs = pairsMapper.selectList(wrapper);
        return pairs;
    }


    @Override
    public String getCoinInfo(String pairsName) {
        return redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairsName);
    }

    @Override
    public List<String> getMainCurs() {
        return pairsMapper.getMainCurs();
    }


    @Override
    public List<Pairs> getPairsByMainCur(String pairsMainCur, CoinConstant.Coin_Type trade_type) {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        //wrapper.select("id","pairs_name");
        wrapper.eq("main_cur", pairsMainCur);
        wrapper.like("trade_type", trade_type);
        wrapper.ne("token_cur","USDT");
        wrapper.orderByDesc("sort");
        List<Pairs> pairs = pairsMapper.selectList(wrapper);
        return pairs;
    }


    @Override
    public List<Pairs> getCoinQuotation(Get_Coin_Sort getCoinSort, CoinConstant.Coin_Type coinType) {
        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.ne("token_cur","USDT");
        if (!coinType.getCoinType().isEmpty()) {
            wrapper.like("trade_type", coinType.getCoinType());
        }
        switch (getCoinSort.getGetCoinSort()) {
            case "NAME_UP":
                wrapper.orderByDesc("pairs_name");
                break;
            case "NAME_DOWN":
                wrapper.orderByAsc("pairs_name");
                break;
            case "PRICE_UP":
                wrapper.orderByAsc("price");
                break;
            case "PRICE_DOWN":
                wrapper.orderByDesc("price");
                break;
            case "UP":
                wrapper.orderByAsc("updown");
                break;
            case "DOWN":
                wrapper.orderByDesc("updown");
                break;
            default:
                break;
        }
        return pairsMapper.selectList(wrapper);
    }


    public static void main(String[] args) {
//        String content = HttpUtil.doGet("https://api.huobi.pr/market/history/kline?symbol=BTCUSD&period=range/1/day&size=10");
//        String content = HttpUtil.doGet("https://api.huobi.pro/market/history/kline?symbol=btcusdt&period=30min&size=10");
//        System.out.println(content);
        // 开始价格
        BigDecimal kbj = new BigDecimal("16853.15450034");
        // 现在价格
        BigDecimal dqj = new BigDecimal("16842.09998770");

        BigDecimal subtract = dqj.subtract(kbj);
        BigDecimal divide = subtract.divide(kbj, 8, BigDecimal.ROUND_HALF_UP);
        //BigDecimal multiply = divide.multiply(new BigDecimal("1"));

        System.out.println(divide);


    }


    @Override
    public void syncKline() {
        List<String> list = new ArrayList<String>();
        list.add("BCH/USDT");
        list.add("DASH/USDT");
        list.add("LTC/USDT");
        list.add("XRP/USDT");
        list.add("ETC/USDT");
        list.add("EOS/USDT");
        list.add("BTC/USDT");
        list.add("ETH/USDT");


        for (String pairs : list) {
            List<String> times = new ArrayList<String>();
            times.add("1m");
            times.add("5m");
            times.add("15m");
            times.add("30m");
            times.add("1h");
            times.add("1d");
            times.add("1w");
            String sdate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            for (String timeStr : times) {
                try {
                    String redisKey = CacheConstants.KLINE_KEY + timeStr + CacheConstants.SPLIT + pairs;
                    String pairsName = pairs.replace("/USDT", "USDT");
                    String period="";
                    switch (timeStr) {
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

                    //"https://data.block.cc/api/v3/kline?api_key=G1OZLXDGGXLWUABHHILY6IHYW4KMISX1XZMJAZTW&desc=gate-io_BTC_USDT&interval=1m"
                    String content = HttpUtil.doGet("https://api.huobi.pro/market/history/kline?symbol="+pairsName+"&period="+period+"&size=10");
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
                        JSONObject jsonArrayInfo = JSONArray.parseObject(obj.toString());
                        JSONObject jsonRedis = new JSONObject();
                        long time=Long.parseLong(jsonArrayInfo.getString("id")+"000");
                        jsonRedis.put("close", jsonArrayInfo.getBigDecimal("close"));
                        jsonRedis.put("high", jsonArrayInfo.getBigDecimal("high"));
                        jsonRedis.put("low", jsonArrayInfo.getBigDecimal("low"));
                        jsonRedis.put("open", jsonArrayInfo.getBigDecimal("open"));
                        jsonRedis.put("time", time);
                        jsonRedis.put("volume", jsonArrayInfo.getBigDecimal("vol"));
                        redisRepository.zsetRmByScore(redisKey,time);
                        redisRepository.zsetAdd(redisKey, jsonRedis.toJSONString(), time);
                    }
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
                        long time=Long.parseLong(jsonInfo.get("T")+"");
                        JSONObject jsonRedis = new JSONObject();
                        jsonRedis.put("close",jsonInfo.get("c"));
                        jsonRedis.put("high", jsonInfo.get("h"));
                        jsonRedis.put("low", jsonInfo.get("l"));
                        jsonRedis.put("open", jsonInfo.get("o"));
                        jsonRedis.put("time", time);
                        jsonRedis.put("volume", jsonInfo.get("v"));
                        redisRepository.zsetRmByScore(redisKey, time);
                        redisRepository.zsetAdd(redisKey, jsonRedis.toJSONString(), time);

                    }
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
                            JSONObject jsonInfo = JSONObject.parseObject(obj.toString());
                            long time = Long.parseLong(jsonInfo.get("t").toString());
                            JSONObject jsonRedis = new JSONObject();
                            jsonRedis.put("close", jsonInfo.get("c"));
                            jsonRedis.put("high", jsonInfo.get("h"));
                            jsonRedis.put("low", jsonInfo.get("l"));
                            jsonRedis.put("open", jsonInfo.get("o"));
                            jsonRedis.put("time", time);
                            jsonRedis.put("volume", jsonInfo.get("v"));
                            redisRepository.zsetRmByScore(redisKey, time);
                            redisRepository.zsetAdd(redisKey, jsonRedis.toJSONString(), time);
                        }
                    }

                     */

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @Override
    public String apiListen(apidata data) {
        msgdata msg = new msgdata();
        if(generateSignature(data.getData(),"key").equals(data.getSign())){
            transMap2Bean2(data.getData(),msg);

        }else {
            return "failed";
        }

        BigDecimal bigDecimal = msg.getAmount();
        if(bigDecimal.compareTo(BigDecimal.ZERO)>0 && data.getName_alias().equals("") && msg.getType()==1 & msg.getState()==1){
            QueryWrapper<WalletPool> objectQueryWrapper = new QueryWrapper<WalletPool>();
            objectQueryWrapper.eq("address", msg.getTo());
            WalletPool wallet = walletPoolMapper.selectOne(objectQueryWrapper);
            String member = wallet.getMember();
            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setId(UUID.randomUUID().toString());
            depositHistory.setAmount(bigDecimal);
            depositHistory.setFrom(msg.getFrom());
            depositHistory.setTo(msg.getTo());
            depositHistory.setContract(msg.getTo());
            depositHistory.setCoin(data.getName());
            depositHistory.setHash(msg.getHash());
            depositHistory.setMember(member);
            depositHistory.setCreateTime(new Date());
            depositHistoryMapper.insert(depositHistory);
            //更新用户资产
            final QueryWrapper<Balance> balanceWrapper = new QueryWrapper<Balance>().eq("user_id", member).eq("currency", data.getName().toUpperCase()).last("LIMIT 1");
            Balance balance = balanceMapper.selectOne(balanceWrapper);
            BigDecimal newBalance = balance.getAssetsBalance().add(bigDecimal);
            balance.setAssetsBalance(newBalance);
            balanceMapper.updateById(balance);
            return "success";
        }
        return "failed";
    }
    //数组签名
    public  String generateSignature(Map<String, String> data, String key)  {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if(k=="sign" || data.get(k).trim()=="0"){
                continue;
            }
            if(data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5Util.encrypt(sb.toString()).toUpperCase();
        //return sb.toString();
    }
    public  void transMap2Bean2(Map<String, String> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
                try {
                       BeanUtils.populate(obj, map);
                   } catch (Exception e) {
                        System.out.println("transMap2Bean2 Error " + e);
                    }
        }

}
