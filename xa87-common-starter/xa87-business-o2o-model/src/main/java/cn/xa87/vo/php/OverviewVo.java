package cn.xa87.vo.php;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/7 13:36
 */
@ApiModel(value = "概览实体")
@Data
public class OverviewVo implements Serializable {

    @ApiModelProperty(value = "订单总量")
    private Integer orderNumber;

    @ApiModelProperty(value = "充值总金额")
    private BigDecimal rechargeTotal;

    @ApiModelProperty(value = "提取总金额")
    private BigDecimal extractTotal;
}
