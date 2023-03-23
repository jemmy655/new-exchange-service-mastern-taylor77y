package cn.xa87.model;


import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Match {
    private String warehouse;

    private String orderId;

    private String memberId;

    private BigDecimal count;

    private BigDecimal price;

    private String pairsName;

    private long sort;

    private ContractConstant.Trade_Type tradeType;

    private TokenOrderConstant.Match_Type matchType;

}
