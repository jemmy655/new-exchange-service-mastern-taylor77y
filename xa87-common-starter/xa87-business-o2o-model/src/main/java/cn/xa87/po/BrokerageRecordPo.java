package cn.xa87.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 计算返佣消息接收体
 */
@Data
public class BrokerageRecordPo implements Serializable {
    /**
     * 消费者userId
     */
    private String consumeUserId;
    /**
     * 消费币种
     */
    private String currency;
    /**
     * 消费数量
     */
    private BigDecimal number;

    public BrokerageRecordPo(String consumeUserId, String currency, BigDecimal number) {
        this.consumeUserId = consumeUserId;
        this.currency = currency;
        this.number = number;
    }


}
