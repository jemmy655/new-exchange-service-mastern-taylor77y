package cn.xa87.vo.php;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/4 22:34
 */
@ApiModel(value = "经纪人查看伞级用户订单实体")
@Data
public class BrokerOrderVo implements Serializable {

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "手机号(邮箱)")
    private String number;

    /**
     * 取自trade_type
     */
    @ApiModelProperty(value = "订单类型(开多(OPEN_UP),开空(OPEN_DOWN))")
    private String orderType;

    /**
     * 取自order_state
     */
    @ApiModelProperty(value = "订单状态(交易中(CREATE),平仓中(无此状态),已平仓(FINAL),取消订单(CANCEL))")
    private String orderStatus;

    /**
     * 取自price(持仓价格)
     */
    @ApiModelProperty(value = "开仓价格")
    private BigDecimal price;

    /**
     * 取自match_price
     */
    @ApiModelProperty(value = "平仓价格")
    private BigDecimal closingPrice;

    /**
     * 取自match_fee
     */
    @ApiModelProperty(value = "盈亏")
    private BigDecimal profitLoss;

    /**
     * take_fee
     */
    @ApiModelProperty(value = "手续费")
    private BigDecimal handlingFee;

    /**
     * contract_hands(手数)
     */
    @ApiModelProperty(value = "下单手数")
    private BigDecimal trouble;

    /**
     * lever_num(倍数)
     */
    @ApiModelProperty(value = "杠杆倍数")
    private BigDecimal leverNum;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "平仓时间")
    private Date updateTime;
}
