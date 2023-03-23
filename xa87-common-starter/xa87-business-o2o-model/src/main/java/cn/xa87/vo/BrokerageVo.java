package cn.xa87.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "回扣榜单")
@Data
public class BrokerageVo implements Serializable {

    @ApiModelProperty(value = "手机号(邮箱)")
    private String brokeragePhone;

    @ApiModelProperty(value = "回扣数量")
    private BigDecimal number;
}
