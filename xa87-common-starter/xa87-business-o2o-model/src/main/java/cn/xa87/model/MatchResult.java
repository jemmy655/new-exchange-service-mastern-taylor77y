package cn.xa87.model;


import cn.xa87.constant.ContractConstant;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchResult {

    private String pairsName;

    private BigDecimal count;

    private BigDecimal price;

    private long time;

    private ContractConstant.Trade_Type tradeType;


    public MatchResult(String pairsName, BigDecimal count, BigDecimal price, ContractConstant.Trade_Type tradeType) {
        this.pairsName = pairsName;
        this.count = count;
        this.price = price;
        this.time = System.currentTimeMillis();
        this.tradeType = tradeType;
    }

}
