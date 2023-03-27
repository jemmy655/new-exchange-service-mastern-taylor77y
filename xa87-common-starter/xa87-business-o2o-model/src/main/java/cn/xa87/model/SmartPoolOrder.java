package cn.xa87.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 t_smart_pool_order
 * 
 * @author ruoyi
 * @date 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_smart_pool_order")
public class SmartPoolOrder extends Model<SmartPoolOrder> {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.UUID)
    private Long id;

    /** 订单号（时间+6位随机数） */
    @TableField(  "order_number")
    private String orderNumber;

    /** 基金产品Id */
    @TableField(  "product_id")
    private Long productId;

    /** 会员id */
    @TableField(  "member_id")
    private String memberId;

    /** 起息日 */
    @TableField(  "value_date")
    private Date valueDate;

    /** 结束起息日 */
    @TableField(  "finish_value_date")
    private Date finishValueDate;

    /** 周期（天） */
    @TableField(  "period_day")
    private Integer periodDay;

    /** 剩余（天） */
    @TableField(  "residue_day")
    private Integer residueDay;

    /** 金额 */
    @TableField(  "price")
    private BigDecimal price;

    /** 累计收益额 */
    @TableField(  "accumulated_income")
    private BigDecimal accumulatedIncome;

    /** 违约金（发生强赎产生） */
    @TableField(  "penal_price")
    private BigDecimal penalPrice;

    /** 开始时间 */
    @TableField(  "start_time")
    private Date startTime;

    /** 结束时间 */
    @TableField(  "end_time")
    private Date endTime;

    /** 状态(0托管中，1已赎回，2强赎) */
    @TableField(  "enabled")
    private Integer enabled;

    @TableField(  "product_name")
    private String productName;

    @TableField(  "product_name_en")
    private String productNameEn;

    @TableField(  "UID")
    private String UID;

    @TableField(  "U_name")
    private String UName;

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

    public SmartPoolOrder() {
    }

    public SmartPoolOrder(String orderNumber, Long productId, String memberId, Date valueDate, Date finishValueDate, Integer periodDay, Integer residueDay, BigDecimal price, BigDecimal accumulatedIncome, BigDecimal penalPrice, Date startTime, Date endTime, Integer enabled, String productName, String productNameEn, String UID, String UName, Date createTime, Date updateTime) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.memberId = memberId;
        this.valueDate = valueDate;
        this.finishValueDate = finishValueDate;
        this.periodDay = periodDay;
        this.residueDay = residueDay;
        this.price = price;
        this.accumulatedIncome = accumulatedIncome;
        this.penalPrice = penalPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enabled = enabled;
        this.productName = productName;
        this.productNameEn = productNameEn;
        this.UID = UID;
        this.UName = UName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
