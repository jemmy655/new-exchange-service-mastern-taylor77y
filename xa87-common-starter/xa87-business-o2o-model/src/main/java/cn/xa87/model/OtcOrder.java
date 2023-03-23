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
@EqualsAndHashCode(callSuper = false)
@TableName("t_otc_order")
public class OtcOrder extends Model<OtcOrder> {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 会员id
     */
    private String member;
    /**
     * 会员法币名称
     */
    private String memberFbName;
    /**
     * 币种
     */
    private String currency;
    /**
     * 数量
     */
    private BigDecimal num;
    /**
     * 浮动比例
     */
    private BigDecimal upDownNumber;
    /**
     * 最小成交价格
     */
    private BigDecimal minPrice;
    /**
     * 极值
     */
    private BigDecimal extremum;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 方向
     */
    private String direction;
    /**
     * 收款方式
     */
    private String payType;
    /**
     * 订单状态
     */
    private String status;
    private String autoStatus;
    private String autoMsg;
    /**
     * 冻结数量
     */
    private BigDecimal freeze;
    /**
     * 已成交数量
     */
    private BigDecimal dealNum;
    @TableField(exist = false)
    private BigDecimal nowPrice;
    @TableField(exist = false)
    private Integer dealCount;
    @TableField(exist = false)
    private Integer jisuanNum;

    private Date createTime;
    private Date updateTime;

    private Integer mccId;
}
