package cn.xa87.member.common.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: peter
 * @Version: 1.0
 * @Date: 2020/4/2 19:23
 * @Modify
 */
public class Callback implements Serializable {

    private BigDecimal coinAmount;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private BigDecimal successAmount;
    private int orderType;
    private int tradeStatus;
    private String coinSign;
    private String companyOrderNum;
    private String otcOrderNum;
    private String tradeOrderTime;
    private String sign;

    public BigDecimal getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(BigDecimal coinAmount) {
        this.coinAmount = coinAmount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "Callback{" +
                "coinAmount=" + coinAmount +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", successAmount=" + successAmount +
                ", orderType=" + orderType +
                ", tradeStatus=" + tradeStatus +
                ", coinSign='" + coinSign + '\'' +
                ", companyOrderNum='" + companyOrderNum + '\'' +
                ", otcOrderNum='" + otcOrderNum + '\'' +
                ", tradeOrderTime='" + tradeOrderTime + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getCoinSign() {
        return coinSign;
    }

    public void setCoinSign(String coinSign) {
        this.coinSign = coinSign;
    }

    public String getCompanyOrderNum() {
        return companyOrderNum;
    }

    public void setCompanyOrderNum(String companyOrderNum) {
        this.companyOrderNum = companyOrderNum;
    }

    public String getOtcOrderNum() {
        return otcOrderNum;
    }

    public void setOtcOrderNum(String otcOrderNum) {
        this.otcOrderNum = otcOrderNum;
    }

    public String getTradeOrderTime() {
        return tradeOrderTime;
    }

    public void setTradeOrderTime(String tradeOrderTime) {
        this.tradeOrderTime = tradeOrderTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
