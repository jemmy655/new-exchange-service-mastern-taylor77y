package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fund_order")
public class FundOrder extends Model<FundOrder> {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /** 订单号（时间+6位随机数） */
    @TableField("order_number")
    private String orderNumber;

    /** 基金产品Id */
    @TableField("fund_product_id")
    private String fundProductId;

    /** 会员id */
    @TableField("member_id")
    private String memberId;

    /** 起息日 */
    @TableField("value_date")
    private Date valueDate;

    /** 结束起息日 */
    @TableField("finish_value_date")
    private Date finishValueDate;

    /** 周期（天） */
    @TableField("period_day")
    private Integer periodDay;

    /** 剩余（天） */
    @TableField("residue_day")
    private Integer residueDay;

    /** 违约金（发生强赎产生） */
    @TableField("price")
    private BigDecimal price;

    /** 累计收益额 */
    @TableField("accumulated_income")
    private BigDecimal accumulatedIncome;

    /** 违约金（发生强赎产生） */
    @TableField("penal_price")
    private BigDecimal penalPrice;

    /** 状态(0托管中，1已赎回，2强赎) */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    public FundOrder() {
    }

    public FundOrder(String orderNumber, String fundProductId, String memberId, Date valueDate, Date finishValueDate, Integer periodDay, Integer residueDay, BigDecimal price, BigDecimal accumulatedIncome, BigDecimal penalPrice, Integer enabled, Date createTime, Date updateTime) {
        this.orderNumber = orderNumber;
        this.fundProductId = fundProductId;
        this.memberId = memberId;
        this.valueDate = valueDate;
        this.finishValueDate = finishValueDate;
        this.periodDay = periodDay;
        this.residueDay = residueDay;
        this.price = price;
        this.accumulatedIncome = accumulatedIncome;
        this.penalPrice = penalPrice;
        this.enabled = enabled;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
