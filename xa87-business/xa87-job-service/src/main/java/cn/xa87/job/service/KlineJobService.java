package cn.xa87.job.service;

public interface KlineJobService {

    void ClearKline();

    void cnyPrice();

    void synchKline();

    /**
     * 拉取数据库 币种最新数据 存入redis
     */
    void trend();
}
