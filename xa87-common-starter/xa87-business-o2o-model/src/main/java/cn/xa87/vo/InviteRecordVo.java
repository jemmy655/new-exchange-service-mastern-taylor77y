package cn.xa87.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "邀请记录")
@Data
public class InviteRecordVo implements Serializable {

    @ApiModelProperty(value = "被邀请人手机号")
    private String phone;

    @ApiModelProperty(value = "被邀请人邮箱")
    private String mail;

    @ApiModelProperty(value = "被邀请时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
