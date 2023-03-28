package cn.xa87.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PledgeOrderVo implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value ="用户名称")
    private String memberId;

    /**
     * 订单号
     */
    @ApiModelProperty(value ="订单号")
    private String orderNumber;

    /**
     * 借款金额
     */
    @ApiModelProperty(value ="借款金额")
    private BigDecimal borrowMoney;

    /**
     * 借款币种
     */
    @ApiModelProperty(value ="借款币种")
    private String borrowName;

    /**
     * 质押金额
     */
    @ApiModelProperty(value ="质押金额")
    private BigDecimal pledgeMoney;

    /**
     * 质押币名称
     */
    @ApiModelProperty(value ="质押币名称")
    private String pledgeName;

    /**
     * 借款天数（天）
     */
    @ApiModelProperty(value ="借款天数")
    private Integer deadline;

    /**
     * 强平价格
     */
    @ApiModelProperty(value ="强平价格")
    private BigDecimal forcePrice;

    /**
     * 质押率（%）
     */
    @ApiModelProperty(value ="质押率")
    private BigDecimal pledgeRate;

    /**
     * 小时利率
     */
    @ApiModelProperty(value ="小时利率")
    private BigDecimal hrRate;

    /**
     * 日利率
     */
    @ApiModelProperty(value ="日利率")
    private BigDecimal dayRate;

    /**
     * 提前还款手续费
     */
    @ApiModelProperty(value ="提前还款手续费")
    private BigDecimal feeMoney;

    /**
     * 总利息
     */
    @ApiModelProperty(value ="总利息")
    private BigDecimal totalMoney;

    /**
     * 总利息
     */
    @ApiModelProperty(value ="总负责")
    private BigDecimal totalIncurDebts;

    /**
     * 预计还款金额
     */
    @ApiModelProperty(value ="预计还款金额")
    private BigDecimal predictRefundMoney;

    /**
     * 还款金额
     */
    @ApiModelProperty(value ="还款金额")
    private BigDecimal refundPrice;

    /**
     * 创建时间
     */
    @ApiModelProperty(value ="创建时间")
    private Date creationTime;

    /**
     * 到期时间
     */
    @ApiModelProperty(value ="到期时间")
    private Date expireTime;

    /**
     * 0(计息中) 1（已结清）2（强平结算）
     */
    @ApiModelProperty(value ="状态 0(计息中) 1（已结清）2（强平结算）")
    private Integer status;
}
