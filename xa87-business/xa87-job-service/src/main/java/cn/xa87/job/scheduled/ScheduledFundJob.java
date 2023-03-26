package cn.xa87.job.scheduled;

import cn.xa87.job.service.FundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *  理财产品 定时器
 * @author Administrator
 */
@Slf4j
@Component
public class ScheduledFundJob {
    @Autowired
    private FundOrderService fundOrderService;

    /**
     * 计算理财收益率 和 结束时间
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void countFundYield(){
        fundOrderService.countFundYield();
    }
}
