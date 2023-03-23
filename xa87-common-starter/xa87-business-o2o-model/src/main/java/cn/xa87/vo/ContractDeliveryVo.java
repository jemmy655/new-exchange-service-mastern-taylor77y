package cn.xa87.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractDeliveryVo {
    // 用户ID
    public String member;
    // 币种
    public String currency;
    // 合约方向: 1涨2跌
    public Integer contractType;
    // 现价
    public BigDecimal currentPrice;
    // 交割时间
    public Integer deliveryTime;
    // 合约交割金额
    public BigDecimal deliveryAmount;
}
