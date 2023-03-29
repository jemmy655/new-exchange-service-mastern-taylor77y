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
import org.checkerframework.checker.units.qual.A;

/**
 * 【请填写功能名称】对象 t_pledge_order_detail
 *
 * @author ruoyi
 * @date 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pledge_order_detail")
public class PledgeOrderDetail extends Model<PledgeOrderDetail> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 质押订单id
     */
    @TableField("order_id")
    private String orderId;

    /**
     * （BORROW）借款，(REFUND)还款 （RENEWAL）续借（NEWPLEDGE)新增质押
     */
    @TableField("type")
    private String type;

    /**
     * 借款金额，还款金额
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 质押类型
     */
    @TableField("pledge_type")
    private String pledgeType;

    /**
     * 质押金额
     */
    @TableField("pledge_price")
    private BigDecimal pledgePrice;

    /**
     * 质押币名称
     */
    @TableField("pledge_name")
    private String pledgeName;



    /**
     * 创建时间
     */
    @TableField("creation_time")
    private Date creationTime;
}
