package cn.xa87.model;

import cn.xa87.constant.TokenOrderConstant;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchBalance {
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private BigDecimal count;
    /**
     * 交易对
     */
    private String pairsName;
    /**
     * 主动方单子
     */
    private String activeOrder;
    /**
     * 被动方
     */
    private String passiveOrder;
    /**
     * 主动方单子
     */
    private String activeWarehouse;
    /**
     * 主动方单子
     */
    private String passiveWarehouse;
    /**
     * 主动方止盈止损
     */
    private TokenOrderConstant.Match_Type activeMatchType;
    /**
     * 被动方止盈止损
     */
    private TokenOrderConstant.Match_Type passiveMatchType;

    public MatchBalance(BigDecimal price, BigDecimal count, String pairsName, String activeOrder, String passiveOrder, String activeWarehouse, String passiveWarehouse, TokenOrderConstant.Match_Type activeMatchType, TokenOrderConstant.Match_Type passiveMatchType) {
        this.price = price;
        this.count = count;
        this.pairsName = pairsName;
        this.activeOrder = activeOrder;
        this.passiveOrder = passiveOrder;
        this.activeWarehouse = activeWarehouse;
        this.passiveWarehouse = passiveWarehouse;
        this.activeMatchType = activeMatchType;
        this.passiveMatchType = passiveMatchType;
    }

}
