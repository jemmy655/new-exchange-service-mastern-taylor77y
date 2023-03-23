package cn.xa87.vo.php;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/4 16:26
 */
@ApiModel(value = "用户信息")
@Data
public class MemberVo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "邀请码")
    private String welCode;

    @ApiModelProperty(value = "邀请码二维码")
    private String quickMark;

    @ApiModelProperty(value = "上级用户账号")
    private String superiorNumber;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "时间")
    private Date createTime;

}
