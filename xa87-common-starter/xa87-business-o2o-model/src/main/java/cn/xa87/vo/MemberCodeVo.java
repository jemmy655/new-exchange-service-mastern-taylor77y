package cn.xa87.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "二维码和邀请码")
@Data
public class MemberCodeVo implements Serializable {

    @ApiModelProperty(value = "二维码")
    private String quickMark;

    @ApiModelProperty(value = "邀请码")
    private String welCode;
}
