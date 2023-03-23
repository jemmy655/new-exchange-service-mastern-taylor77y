package cn.xa87.job.service;

import java.time.LocalDateTime;

public interface SelfIssuedCurrencyKlineDataService {

    void createSelfIssuedCurrencyKlineData();

    //        long seconds = Duration.between(LocalDateTime.now(), LocalDate.now().atTime(23,59,59)).getSeconds();
    void createKlineData(int fori, LocalDateTime localDateTime);

    void createPeriodData(String s);


}
