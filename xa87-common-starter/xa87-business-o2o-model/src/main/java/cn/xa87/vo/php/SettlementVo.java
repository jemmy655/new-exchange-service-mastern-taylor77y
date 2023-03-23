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
 * @date 2020/2/4 23:50
 */
@ApiModel(value = "结算列表实体")
@Data
public class SettlementVo implements Serializable {

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "手机号(邮箱)")
    private String number;

    @ApiModelProperty(value = "订单状态(已平仓(FINAL))")
    private String orderStatus;

    @ApiModelProperty(value = "盈亏状态:盈(0)亏(1)")
    private Byte state;

    @ApiModelProperty(value = "初始USDT余额")
    private BigDecimal initialBalance;

    @ApiModelProperty(value = "最终USDT余额")
    private BigDecimal finallyBalance;

    @ApiModelProperty(value = "贡献收益")
    private BigDecimal contribution;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结算时间")
    private Date updateTime;
}
