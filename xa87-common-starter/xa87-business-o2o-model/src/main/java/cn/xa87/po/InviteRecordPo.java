package cn.xa87.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "邀请记录")
@Data
public class InviteRecordPo implements Serializable {

    @ApiModelProperty(value = "被邀请人手机号(也可能是邮箱)")
    private String phone;

    @ApiModelProperty(value = "被邀请时间")
    private String createTime;

    @ApiModelProperty(value = "从被邀请人处获得收益")
    private BigDecimal number;
}
