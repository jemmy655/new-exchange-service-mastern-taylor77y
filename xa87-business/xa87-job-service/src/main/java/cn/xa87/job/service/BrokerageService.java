package cn.xa87.job.service;

public interface BrokerageService {

    /**
     * 计算回扣,并赋值,统计前一天
     */
    void calculateBrokerage();

    /**
     * 统计月度排行榜,上个月
     */
    void statisticsBrokerageTopMonth();
}
