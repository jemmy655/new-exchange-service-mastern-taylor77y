package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.job.mapper.CurrencyControlsMapper;
import cn.xa87.job.mapper.KlineDataMapper;
import cn.xa87.job.mapper.StagePriceMapper;
import cn.xa87.job.service.SelfIssuedCurrencyKlineDataService;
import cn.xa87.job.utils.RandomUtils;
import cn.xa87.model.CurrencyControls;
import cn.xa87.model.KlineData;
import cn.xa87.model.StagePrice;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class SelfIssuedCurrencyKlineDataServiceImpl implements SelfIssuedCurrencyKlineDataService {
    @Resource
    private Xa87RedisRepository redisRepository;
    @Resource
    private KlineDataMapper klineDataMapper;
    @Resource
    private CurrencyControlsMapper currencyControlsMapper;
    @Resource
    private StagePriceMapper stagePriceMapper;

    @Override
    public void createSelfIssuedCurrencyKlineData() {
        LocalDateTime localDateTime = LocalDate.now().atTime(0, 0, 0).plusDays(1);
        createKlineData(86400,localDateTime);
    }

    //        long seconds = Duration.between(LocalDateTime.now(), LocalDate.now().atTime(23,59,59)).getSeconds();
    @Override
    public void createKlineData(int fori, LocalDateTime localDateTime) {
        List<CurrencyControls> currencyControlsList = currencyControlsMapper.findCurrencyControlsList();
        BigDecimal firstOpen = null;
        for (CurrencyControls currencyControls : currencyControlsList) {
            List<KlineData> klineDataList = new ArrayList<>();
            // 测试使用，第二天数据不允许多生成
            int klineDateCount = klineDataMapper.getKlineDateCount(currencyControls.getTokenCur());
            if(klineDateCount <= 86400){
                // 币起始价格
                BigDecimal startingPrice = currencyControls.getStartingPrice();
                // 币结束价格
                BigDecimal finalPrice = currencyControls.getFinalPrice();
                // 最大交易量
                BigDecimal tradingVolumeMax = currencyControls.getTradingVolumeMax();
                // 最小交易量
                BigDecimal tradingVolumeMin = currencyControls.getTradingVolumeMin();

                firstOpen = startingPrice;
                KlineData klineData;

                //涨跌： 1 涨 2 跌
                int slice = RandomUtils.next(1, 2);
                //持续次数
                int repeat = RandomUtils.next(1, 3);

                int index = 0;
                for (int i = 0; i < fori; i++) {
                    localDateTime = localDateTime.plusSeconds(1);

                    // Double price = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());
                    Double openPrice = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());
                    Double closePrice = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());
                    double volume = RandomUtils.next(tradingVolumeMax.doubleValue(), tradingVolumeMin.doubleValue());

                    int count = RandomUtils.next(2, 11);

                    if (repeat == index) {
                        // 到了
                        index = 0;
                        slice = RandomUtils.next(1, 2);
                        repeat = RandomUtils.next(1, 3);
//                    System.out.println((slice==1?"涨":"跌") + "    持续次数=>" + repeat);
                    }
                    BigDecimal n = startingPrice.multiply(BigDecimal.valueOf(0.001));

                    if (slice == 1) {
                        startingPrice = startingPrice.add(n);
                    } else {
                        startingPrice = startingPrice.subtract(n);
                    }
                    index++;

                    klineData = new KlineData();
                    klineData.setTokenCur(currencyControls.getTokenCur());
                    klineData.setOpen(BigDecimal.valueOf(openPrice));
                    klineData.setClose(BigDecimal.valueOf(closePrice));
                    klineData.setPrice(startingPrice);
                    klineData.setVolume(BigDecimal.valueOf(volume));
                    klineData.setCount(count);
                    klineData.setCreateTime(localDateTime);
                    klineData.setVol(BigDecimal.valueOf(volume).multiply(BigDecimal.valueOf(count)));
                    klineDataList.add(klineData);
                    klineDataMapper.insert(klineData);
                }
                if(!klineDataList.isEmpty()){
                    List<List<KlineData>> m1 = split(klineDataList, 60);
                    saveKlineData(m1,"1m",firstOpen);
                    List<List<KlineData>> m5 = split(klineDataList, 300);
                    saveKlineData(m5,"5m",firstOpen);
                    List<List<KlineData>> m15 = split(klineDataList, 900);
                    saveKlineData(m15,"15m",firstOpen);
                    List<List<KlineData>> m30 = split(klineDataList, 1800);
                    saveKlineData(m30,"30m",firstOpen);
                    List<List<KlineData>> h1 = split(klineDataList, 3600);
                    saveKlineData(h1,"1h",firstOpen);
                    List<List<KlineData>> h4 = split(klineDataList, 14400);
                    saveKlineData(h4,"4h",firstOpen);
                    buildKlineData("1d",klineDataList,firstOpen);
                }
            }
        }
    }



    public static void main(String[] args) {
        double begin = 6.11;
        double end = 7.11;

        //涨跌
        int slice = RandomUtils.next(1, 2);
        //持续次数
        int repeat = RandomUtils.next(1, 3);

        List<Double> arr = new ArrayList<>();
        System.out.println((slice==1?"涨":"跌") + "    持续次数=>" + repeat);
        int index = 0;
        for (int i = 0; i < 10; i++) {
            if (repeat == index) {
                // 到了
                index = 0;
                slice = RandomUtils.next(1, 2);
                repeat = RandomUtils.next(1, 3);
                System.out.println((slice==1?"涨":"跌") + "    持续次数=>" + repeat);

            }
            double n = begin * 0.01;

            if (slice == 1) {
                begin += n;
            } else {
                begin -= n;
            }
            index++;

            arr.add(begin);
        }

    }


    private void saveKlineData(List<List<KlineData>> list,String timeType,BigDecimal firstOpen){
        for (List<KlineData> klineDataList : list) {
            buildKlineData(timeType, klineDataList, firstOpen);
        }
    }

    Double lastClose = null;
    private void buildKlineData(String timeType, List<KlineData> klineDataList,BigDecimal firstOpen) {
        KlineData klineData1 = klineDataList.get(klineDataList.size() - 1);
        LocalDateTime createTime = klineData1.getCreateTime();
        klineDataList.sort(Comparator.comparing(KlineData::getPrice));

        // 最低价(本轮交易（分，时，天）)
        KlineData klineData = klineDataList.get(0);
        // 最高价
        KlineData klineData2 = klineDataList.get(klineDataList.size() - 1);

        StagePrice stagePrice = new StagePrice();
        stagePrice.setTokenCur(klineData.getTokenCur());

        stagePrice.setVolume(klineData2.getVolume());
        stagePrice.setVol(klineData2.getVol());
        stagePrice.setCount(klineData2.getCount());

        if(lastClose == null){
            stagePrice.setOpen(firstOpen);
        }else {
            stagePrice.setOpen(BigDecimal.valueOf(lastClose));
        }
        // 闭盘价（上一分钟交易的平均数）
        double avg = klineDataList.stream().mapToDouble(value -> value.getPrice().doubleValue()).average().getAsDouble();
        stagePrice.setClose(BigDecimal.valueOf(avg));


//        stagePrice.setHigh(klineData2.getPrice());
//        stagePrice.setLow(klineData.getPrice());

        // 设置最高最低价格
        // 设置最高最低价格
        if(stagePrice.getOpen().doubleValue() > stagePrice.getClose().doubleValue()){
            //这里是跌了 下面是收盘 上面是开盘
            double next = RandomUtils.nextF6(0.001900, 0.000650);
//            double next = 0.0025;

            BigDecimal multiply = stagePrice.getOpen().multiply(BigDecimal.valueOf(next));
            stagePrice.setHigh(stagePrice.getOpen().add(multiply));
            next = RandomUtils.nextF6(0.001900, 0.000650);
            BigDecimal multiply1 = stagePrice.getClose().multiply(BigDecimal.valueOf(next));
            stagePrice.setLow(stagePrice.getClose().subtract(multiply1));
        }else {
//            double next = 0.0025;
            double next = RandomUtils.nextF6(0.001900, 0.000650);
            BigDecimal multiply = stagePrice.getClose().multiply(BigDecimal.valueOf(next));
            stagePrice.setHigh(stagePrice.getClose().add(multiply));
            next = RandomUtils.nextF6(0.001900, 0.000650);
            BigDecimal multiply1 = stagePrice.getOpen().multiply(BigDecimal.valueOf(next));
            stagePrice.setLow(stagePrice.getOpen().subtract(multiply1));
        }


        stagePrice.setTimeType(timeType);
        stagePrice.setCreateTime(createTime);
        stagePriceMapper.insert(stagePrice);

        lastClose = avg;
    }

    /**
     * 拆分集合
     *
     * @param <T>           泛型对象
     * @param resList       需要拆分的集合
     * @param subListLength 每个子集合的元素个数
     * @return 返回拆分后的各个集合组成的列表
     * 代码里面用到了guava和common的结合工具类
     **/
    public static <T> List<List<T>> split(List<T> resList, int subListLength) {
        if (CollectionUtils.isEmpty(resList) || subListLength <= 0) {
            return Lists.newArrayList();
        }
        List<List<T>> ret = Lists.newArrayList();
        int size = resList.size();
        if (size <= subListLength) {
            // 数据量不足 subListLength 指定的大小
            ret.add(resList);
        } else {
            int pre = size / subListLength;
            int last = size % subListLength;
            // 前面pre个集合，每个大小都是 subListLength 个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = Lists.newArrayList();
                for (int j = 0; j < subListLength; j++) {
                    itemList.add(resList.get(i * subListLength + j));
                }
                ret.add(itemList);
            }
            // last的进行处理
            if (last > 0) {
                List<T> itemList = Lists.newArrayList();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * subListLength + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    @Override
    public void createPeriodData(String type) {
        List<StagePrice> stagePriceByStatusList;
        if(type.equals("1w")){
            stagePriceByStatusList = stagePriceMapper.findStagePriceByWeekStatusList();
            if(stagePriceByStatusList.size() < 7){
                return;
            }
        }else {
            stagePriceByStatusList = stagePriceMapper.findStagePriceByStatusList(type);
        }

        for (StagePrice stagePrice : stagePriceByStatusList) {
            String biKey = stagePrice.getTokenCur() + "/USDT".toUpperCase();
            String redisKey = CacheConstants.KLINE_KEY + type + CacheConstants.SPLIT + biKey;

            long time = stagePrice.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));

            JSONObject jsonInfo = new JSONObject();
            // 成交量
            jsonInfo.put("volume",stagePrice.getVolume());
            // 成交量
            jsonInfo.put("amount",stagePrice.getVolume());
            // 最高价
            jsonInfo.put("high",stagePrice.getHigh());
            // 最低价
            jsonInfo.put("low",stagePrice.getLow());
            // 成交额
            jsonInfo.put("vol",stagePrice.getVol());
            // 成交笔数
            jsonInfo.put("count",stagePrice.getCount());
            // K线ID
            jsonInfo.put("id",time);
            // 收盘价（当K线为最晚的一根时，是最新成交价）
            jsonInfo.put("close",stagePrice.getClose());
            // 开盘价
            jsonInfo.put("open", stagePrice.getOpen());

            long timeLong = Long.parseLong(String.valueOf(time).concat("000"));

            jsonInfo.put("time",timeLong);

            redisRepository.zsetRmByScore(redisKey, timeLong);
            redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), timeLong);

            stagePrice.setStatus(2);
            stagePriceMapper.updateById(stagePrice);
        }

    }

}
