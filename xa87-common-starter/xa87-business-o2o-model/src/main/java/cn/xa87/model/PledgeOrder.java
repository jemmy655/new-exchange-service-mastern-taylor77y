package cn.xa87.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 t_pledge_order
 *
 * @author ruoyi
 * @date 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pledge-order")
public class PledgeOrder extends Model<PledgeOrder> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 用户名称
     */
    @TableField("member_id")
    private String memberId;

    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 借款金额
     */
    @TableField("borrow_money")
    private BigDecimal borrowMoney;

    /**
     * 借款币种
     */
    @TableField("borrow_name")
    private String borrowName;

    /**
     * 质押金额
     */
    @TableField("pledge_money")
    private BigDecimal pledgeMoney;

    /**
     * 质押币名称
     */
    @TableField("pledge_name")
    private String pledgeName;

    /**
     * 借款天数（天）
     */
    @TableField("deadline")
    private Integer deadline;

    /**
     * 强平价格
     */
    @TableField("force_price")
    private BigDecimal forcePrice;

    /**
     * 质押率（%）
     */
    @TableField("pledge_rate")
    private BigDecimal pledgeRate;

    /**
     * 小时利率
     */
    @TableField("hr-rate")
    private BigDecimal hrRate;

    /**
     * 日利率
     */
    @TableField("dayR_rate")
    private BigDecimal dayRate;

    /**
     * 提前还款手续费
     */
    @TableField("fee_money")
    private BigDecimal feeMoney;

    /**
     * 总利息
     */
    @TableField("total_money")
    private BigDecimal totalMoney;

    /**
     * 总负债
     */
    @TableField("total_incur_debts")
    private BigDecimal totalIncurDebts;

    /**
     * 预计还款金额
     */
    @TableField("predict_refund_money")
    private BigDecimal predictRefundMoney;

    /**
     * 还款金额
     */
    @TableField("refund_price")
    private BigDecimal refundPrice;

    /**
     * 创建时间
     */
    @TableField("creation_time")
    private Date creationTime;

    /**
     * 到期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 0(计息中) 1（已结清）2（强平结算）
     */
    @TableField("status")
    private Integer status;


    public PledgeOrder() {
    }

    public PledgeOrder(String memberId, String orderNumber, BigDecimal borrowMoney, String borrowName, BigDecimal pledgeMoney, String pledgeName, Integer deadline, BigDecimal forcePrice, BigDecimal pledgeRate, BigDecimal hrRate, BigDecimal dayRate, BigDecimal feeMoney, BigDecimal totalMoney, BigDecimal totalIncurDebts, BigDecimal predictRefundMoney, BigDecimal refundPrice, Date creationTime, Date expireTime, Integer status) {
        this.memberId = memberId;
        this.orderNumber = orderNumber;
        this.borrowMoney = borrowMoney;
        this.borrowName = borrowName;
        this.pledgeMoney = pledgeMoney;
        this.pledgeName = pledgeName;
        this.deadline = deadline;
        this.forcePrice = forcePrice;
        this.pledgeRate = pledgeRate;
        this.hrRate = hrRate;
        this.dayRate = dayRate;
        this.feeMoney = feeMoney;
        this.totalMoney = totalMoney;
        this.totalIncurDebts = totalIncurDebts;
        this.predictRefundMoney = predictRefundMoney;
        this.refundPrice = refundPrice;
        this.creationTime = creationTime;
        this.expireTime = expireTime;
        this.status = status;
    }
}
