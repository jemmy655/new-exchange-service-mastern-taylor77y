package cn.xa87.po.php;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/7 20:19
 */
@ApiModel(value = "经纪人查看伞级用户订单入参")
@Data
public class BrokerOrderDto implements Serializable {
    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "手机号(邮箱)")
    private String number;

    @ApiModelProperty(value = "订单类型(开多(OPEN_UP),开空(OPEN_DOWN))")
    private String orderType;

    @ApiModelProperty(value = "订单状态(交易中(CREATE),平仓中(无此状态),已平仓(FINAL),取消订单(CANCEL))")
    private String orderStatus;

    @ApiModelProperty(value = "下单时间(始) 例:2019-12-19 07:48:41")
    private String createTimeBegin;

    @ApiModelProperty(value = "下单时间(末) 例:2019-12-19 07:48:41")
    private String createTimeEnd;

    @ApiModelProperty(value = "平仓时间(始) 例:2019-12-19 07:48:41")
    private String closeOutTimeBegin;

    @ApiModelProperty(value = "平仓时间(末) 例:2019-12-19 07:48:41")
    private String closeOutTimeEnd;
}
