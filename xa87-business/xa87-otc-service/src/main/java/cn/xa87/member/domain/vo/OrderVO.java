package cn.xa87.member.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author peter
 * @create 2020-07-08-18:28
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {

    public interface AddOrder { }

    public interface GetOrderInfo { }

    @ApiModelProperty(value = "商户id")
    @NotNull(message = "商户id不能为空", groups = {GetOrderInfo.class})
    private Long companyId;

    @ApiModelProperty(value = "商户订单号")
    @NotBlank(message = "商户订单号不能为空", groups = {GetOrderInfo.class})
    private String companyOrderNum;

    @ApiModelProperty(value = "商户用户id")
    private Long companyUserId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "实名认证")
    private Integer kyc;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "手机区号")
    private String areaCode;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "证件类型")
    private Integer idCardType;

    @ApiModelProperty(value = "证件号码")
    private String idCardNum;

    @ApiModelProperty(value = "支付账户")
    private String payCardNo;

    @ApiModelProperty(value = "开户银行")
    private String payCardBank;

    @ApiModelProperty(value = "开户支行")
    private String payCardBranch;

    @ApiModelProperty(value = "支付渠道")
    private Integer orderPayChannel;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "币种标识")
    private String coinSign;

    @ApiModelProperty(value = "支付币种标识")
    private String payCoinSign;

    @ApiModelProperty(value = "订单币种总数")
    private BigDecimal coinSum;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "订单总价")
    private BigDecimal coinPrice;

    @ApiModelProperty(value = "显示总价")
    private BigDecimal displayAmount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal fee;

    @ApiModelProperty(value = "派单模式")
    //@NotNull(message = "派单模式不能为空", groups = {AddOrder.class,WebAddOrder.class})
    private Integer mode = 1;

    @ApiModelProperty(value = "交易员id")
    private Long traderId;

    @ApiModelProperty(value = "交易员用户id")
    private Long traderUserId;

    @ApiModelProperty(value = "交易员收付款账号id")
    private Long traderPaymentId;

    @ApiModelProperty(value = "下单时间")
    //@NotBlank(message = "下单时间不能为空", groups = {AddOrder.class})
    private String orderTime;

    public String getCompanyOrderNum() {
        return companyOrderNum;
    }

    public void setCompanyOrderNum(String companyOrderNum) {
        this.companyOrderNum = companyOrderNum;
    }

    public Long getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(Long companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getKyc() {
        return kyc;
    }

    public void setKyc(Integer kyc) {
        this.kyc = kyc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Integer idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(String payCardNo) {
        this.payCardNo = payCardNo;
    }

    public String getPayCardBank() {
        return payCardBank;
    }

    public void setPayCardBank(String payCardBank) {
        this.payCardBank = payCardBank;
    }

    public String getPayCardBranch() {
        return payCardBranch;
    }

    public void setPayCardBranch(String payCardBranch) {
        this.payCardBranch = payCardBranch;
    }

    public Integer getOrderPayChannel() {
        return orderPayChannel;
    }

    public void setOrderPayChannel(Integer orderPayChannel) {
        this.orderPayChannel = orderPayChannel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCoinSign() {
        return coinSign;
    }

    public void setCoinSign(String coinSign) {
        this.coinSign = coinSign;
    }

    public String getPayCoinSign() {
        return payCoinSign;
    }

    public void setPayCoinSign(String payCoinSign) {
        this.payCoinSign = payCoinSign;
    }

    public BigDecimal getCoinSum() {
        return coinSum;
    }

    public void setCoinSum(BigDecimal coinSum) {
        this.coinSum = coinSum;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(BigDecimal coinPrice) {
        this.coinPrice = coinPrice;
    }

    public BigDecimal getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(BigDecimal displayAmount) {
        this.displayAmount = displayAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public Long getTraderUserId() {
        return traderUserId;
    }

    public void setTraderUserId(Long traderUserId) {
        this.traderUserId = traderUserId;
    }

    public Long getTraderPaymentId() {
        return traderPaymentId;
    }

    public void setTraderPaymentId(Long traderPaymentId) {
        this.traderPaymentId = traderPaymentId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSyncUrl() {
        return syncUrl;
    }

    public void setSyncUrl(String syncUrl) {
        this.syncUrl = syncUrl;
    }

    public String getAsyncUrl() {
        return asyncUrl;
    }

    public void setAsyncUrl(String asyncUrl) {
        this.asyncUrl = asyncUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @ApiModelProperty(value = "同步回调地址")
    //@NotBlank(message = "同步回调地址不能为空", groups = {AddOrder.class})
    private String syncUrl;

    @ApiModelProperty(value = "异步回调地址")
    //@NotBlank(message = "异步回调地址不能为空", groups = {AddOrder.class})
    private String asyncUrl;

    @ApiModelProperty(value = "签名")
    @NotBlank(message = "签名不能为空", groups = {AddOrder.class, GetOrderInfo.class})
    private String sign;

}

