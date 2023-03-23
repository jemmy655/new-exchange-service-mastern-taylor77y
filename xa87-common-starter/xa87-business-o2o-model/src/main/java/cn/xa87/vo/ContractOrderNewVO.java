package cn.xa87.vo;

import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import com.baomidou.mybatisplus.annotation.TableField;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

public class ContractOrderNewVO {

    private String id;//数据库唯一键（自增涨）
    private String OrderId;//订单号 （时间年月日时分秒+6位随机数  20230323111213111111）
    private BigDecimal matchFee; //收益
    private String matchFeeUp;//收益率
    private BigDecimal KPrice;//开仓金额
    private BigDecimal BPrice;//可平金额
    private BigDecimal margin;//保证金
    private BigDecimal takeFee;//手续费
    private BigDecimal price;//键仓价格
    private BigDecimal matchPrice;//平仓价格
    private Date createTime;//开仓时间
    private Date updateTime;//平仓时间

    private TokenOrderConstant .Order_Type orderType; //操作类型

    private TokenOrderConstant.Order_State orderState; //订单状态


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public BigDecimal getMatchFee() {
        return matchFee;
    }

    public void setMatchFee(BigDecimal matchFee) {
        this.matchFee = matchFee;
    }

    public String getMatchFeeUp() {
        return matchFeeUp;
    }

    public void setMatchFeeUp(String matchFeeUp) {
        this.matchFeeUp = matchFeeUp;
    }

    public BigDecimal getKPrice() {
        return KPrice;
    }

    public void setKPrice(BigDecimal KPrice) {
        this.KPrice = KPrice;
    }

    public BigDecimal getBPrice() {
        return BPrice;
    }

    public void setBPrice(BigDecimal BPrice) {
        this.BPrice = BPrice;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getTakeFee() {
        return takeFee;
    }

    public void setTakeFee(BigDecimal takeFee) {
        this.takeFee = takeFee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(BigDecimal matchPrice) {
        this.matchPrice = matchPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public TokenOrderConstant.Order_Type getOrderType() {
        return orderType;
    }

    public void setOrderType(TokenOrderConstant.Order_Type orderType) {
        this.orderType = orderType;
    }

    public TokenOrderConstant.Order_State getOrderState() {
        return orderState;
    }

    public void setOrderState(TokenOrderConstant.Order_State orderState) {
        this.orderState = orderState;
    }
}
