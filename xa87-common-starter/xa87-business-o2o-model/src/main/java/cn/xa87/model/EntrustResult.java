package cn.xa87.model;


import cn.xa87.constant.EntrustConstant;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EntrustResult {

    private String pairsName;

    private BigDecimal count;

    private BigDecimal price;

    private long time;

    private EntrustConstant.Entrust_Type entrustType;


    public EntrustResult(String pairsName, BigDecimal count, BigDecimal price, EntrustConstant.Entrust_Type entrustType) {
        this.pairsName = pairsName;
        this.count = count;
        this.price = price;
        this.time = System.currentTimeMillis();
        this.entrustType = entrustType;
    }

}
