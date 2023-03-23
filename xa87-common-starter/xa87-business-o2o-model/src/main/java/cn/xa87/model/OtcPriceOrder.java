package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_otc_order_price")
public class OtcPriceOrder {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 商户id
     */
    private String storeId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 用户买卖方向
     */
    private String userDirection;
    /**
     * 商户买卖方向
     */
    private String storeDirection;
    /**
     * 下单数量
     */
    private BigDecimal num;
    /**
     * 下单单价
     */
    private BigDecimal price;
    /**
     * 下单总价
     */
    private BigDecimal totalPrice;
    /**
     * 付款方式
     */
    private String payType;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 币种
     */
    private String currency;
    /**
     * 申诉状态
     */
    private String appealStatus;
    /**
     * 申诉人
     */
    private String appealUserId;

    /**
     * 支付时使用的货币ID
     */
    private Integer payMccId;

    @TableField(exist = false)
    private Map<String, Object> payMap;
    @TableField(exist = false)
    private String userPhone;
    @TableField(exist = false)
    private String storePhone;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String storeName;
    private Date createTime;
    private Date updateTime;

    // 商户联系链接
    @TableField(exist = false)
    private String connectLink;

    // 汇率
    @TableField(exist = false)
    private BigDecimal rate;
    // 货币符号
    @TableField(exist = false)
    private String mark;
}
