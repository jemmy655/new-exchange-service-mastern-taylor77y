package cn.xa87.po.php;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/10 13:48
 */
@ApiModel(value = "经纪人查看出入金列表入参")
@Data
public class DealVoDto implements Serializable {

    @ApiModelProperty(value = "手机号或邮箱")
    private String number;

    @ApiModelProperty(value = "划转类型(币币到合(BALANCE_MOVE_TOKEN)合约到比比(TOKEN_MOVE_BALANCE))")
    private String transferType;

    @ApiModelProperty(value = "划转时间(始) 例:2019-12-19 07:48:41")
    private String createTimeBegin;

    @ApiModelProperty(value = "划转时间(末) 例:2019-12-19 07:48:41")
    private String createTimeEnd;
}
