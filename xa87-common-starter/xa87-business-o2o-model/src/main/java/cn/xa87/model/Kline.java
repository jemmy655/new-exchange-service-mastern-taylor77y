package cn.xa87.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * redis. kline 对象的实体
 * 属性名含义来自 yuZhen
 */
@Data
public class Kline implements Serializable {

    /**
     * 成交量
     */
    private BigDecimal volume;

    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 成交分钟时间戳
     */
    private Long time;
    /**
     * 闭盘价
     */
    private BigDecimal close;
    /**
     * 开盘价
     */
    private BigDecimal open;
}
