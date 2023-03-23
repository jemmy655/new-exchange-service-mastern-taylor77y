package cn.xa87.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EntrustBalance {
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

    public EntrustBalance(BigDecimal price, BigDecimal count, String pairsName, String activeOrder, String passiveOrder) {
        this.price = price;
        this.count = count;
        this.pairsName = pairsName;
        this.activeOrder = activeOrder;
        this.passiveOrder = passiveOrder;
    }

}
