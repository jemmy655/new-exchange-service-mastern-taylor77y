package cn.xa87.job.scheduled;

import cn.xa87.job.service.KlineJobService;
import cn.xa87.job.service.OtchandService;
import cn.xa87.job.service.RobotService;
import cn.xa87.job.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ScheduledKlineJob {

    @Autowired
    private KlineJobService klineJobService;


    @Autowired
    private TokenService tokenService;


    @Autowired
    private OtchandService otcHandService;

    @Autowired
    private RobotService robotService;

    /**
     * 机器人委托下单 每2秒执行一次
     */
    @Scheduled(cron = "*/2 * * * * ?")
    public void robotJobHandler() {
        robotService.robot();
    }

    /**
     * 抓取指数价格
     *
     * @return
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void indexPricehanler() {
        tokenService.getIndexPrice();
    }


    /**
     * 更新最新价-最低价-最高价-涨跌幅-开盘价
     *
     * @return
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void priceJobHandler() {
        klineJobService.cnyPrice();
    }


    /**
     * 每月一号
     *
     * @return
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void clearKlineHandler() {
        klineJobService.ClearKline();
    }


    /**
     * 开始清理过期订单数据
     *
     * @return
     */
//    @Scheduled(cron = "*/60 * * * * ?")
    public void OtcHandler() {
        otcHandService.startHandler();
    }


    /**
     * 更新最新价-最低价-最高价-涨跌幅-开盘价
     *
     * @return
     */
//    @Scheduled(fixedDelay = 1000*2)
    public void synchKline() {
        klineJobService.synchKline();
    }
}
