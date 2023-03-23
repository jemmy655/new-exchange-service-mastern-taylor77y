package cn.xa87.job.scheduled;

import cn.xa87.job.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledJob {


    @Autowired
    private UserBlancehanlerService userBlancehanler;

    @Autowired
    private BiBiDayRecordService biBiDayRecordService;

    @Autowired
    private BrokerageService brokerageService;

    @Autowired
    private BurstService burstService;

    @Autowired
    private KlineJobService klineJobService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OtchandService otcHandService;

    @Autowired
    private SecondsContractService secondsContractService;

    @Autowired
    private MatchingTransactionService matchingTransactionService;

    @Autowired
    private OwnCurrencyService ownCurrencyService;

    @Autowired
    private SelfIssuedCurrencyKlineDataService selfIssuedCurrencyKlineDataService;


    /**
     * 秒合约计算每1秒执行一次(秒合约结算)
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void secondsContractTask(){
        secondsContractService.secondsContractTask();
    }


    /**
     * 币币撮合交易
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void currencyMatchingTransaction(){
        matchingTransactionService.currencyMatchingTransaction();
    }

    /**
     * 读取数据存储到redis
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void generateCandlestickData(){
        ownCurrencyService.generateCandlestickData();
    }


    /**
     * 创建自发币K线数据
     */
//    @Scheduled(cron = "0 0/30 23 * * ? ")
    public void createSelfIssuedCurrencyKlineData(){
        selfIssuedCurrencyKlineDataService.createSelfIssuedCurrencyKlineData();
    }


    /**
     * 币币昨日交易统计凌晨一点
     *
     * @return
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void bibiDayHanler() {
        biBiDayRecordService.isBurst();
    }

    /**
     * 定时计算回扣 凌晨两点
     *
     * @return
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void brokerageHandler() {
        brokerageService.calculateBrokerage();
    }

    /**
     * 统计月度榜单 每月一号执行
     *
     * @return
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void brokerageTopHandler() {
        brokerageService.statisticsBrokerageTopMonth();
    }

    /**
     * 爆仓任务调度 每秒执行一次
     *
     * @return
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void bursthanler() {
        burstService.isBurst();
    }

    /**
     * 开始分析用户财务 凌晨三点
     *
     * @return
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void userBlancehanler() {
        userBlancehanler.start();
    }


    /**
     * 清理多余盈亏数据
     *
     * @return
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void clearPrefithanler() {
        burstService.clearProfit();
    }


    /**
     * 更改订单收益率
     *
     * @return
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void tokenhanler() {
        tokenService.upTokenOrder();
    }



    //////////////////////////////////////////  自发币K线数据  //////////////////////////////////////////
    /**
     * 一分钟执行一次
     */
   // @Scheduled(cron = "0 */1 * * * ?")
    public void executeOnceAMinute(){
        selfIssuedCurrencyKlineDataService.createPeriodData("1m");
    }

    /**
     * 五分钟执行一次
     */
   // @Scheduled(cron = "0 */5 * * * ?")
    public void everyFiveMinutes(){
        selfIssuedCurrencyKlineDataService.createPeriodData("5m");
    }

    /**
     * 十五分钟执行一次
     */
    //@Scheduled(cron = "0 */15 * * * ?")
    public void executeEveryFifteenMinutes(){
        selfIssuedCurrencyKlineDataService.createPeriodData("15m");
    }

    /**
     * 三十分钟执行一次
     */
    //@Scheduled(cron = "0 0/30 * * * ?")
    public void executeEveryThirtyMinutes(){
        selfIssuedCurrencyKlineDataService.createPeriodData("30m");
    }

    /**
     * 一个小时执行一次
     */
    //@Scheduled(cron = "0 0 * * * ?")
    public void executeOnceAnHour(){
        selfIssuedCurrencyKlineDataService.createPeriodData("1h");
    }

    /**
     * 四个小时执行一次
     */
    //@Scheduled(cron = "0 0 0/4 * * ? ")
    public void everyFourHours(){
        selfIssuedCurrencyKlineDataService.createPeriodData("4h");
    }

    /**
     * 一天执行一次
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    public void executeOnceADay(){
        selfIssuedCurrencyKlineDataService.createPeriodData("1d");
    }

    /**
     * 一周执行一次
     */
    //@Scheduled(cron = "0 0 1 ? * 7")
    public void runOnceAWeek(){
        selfIssuedCurrencyKlineDataService.createPeriodData("1w");
    }

    //////////////////////////////////////////  自发币K线数据  //////////////////////////////////////////
}
