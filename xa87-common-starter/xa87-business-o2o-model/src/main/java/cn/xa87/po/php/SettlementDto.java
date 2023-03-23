package cn.xa87.po.php;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/11 11:29
 */
@ApiModel(value = "查看结算列表入参")
@Data
public class SettlementDto implements Serializable {

    @ApiModelProperty(value = "账号(手机号/邮箱)")
    private String number;

    @ApiModelProperty(value = "盈亏状态:盈(0)亏(1)")
    private Byte state;

    @ApiModelProperty(value = "时间(始) 例:2019-12-19 07:48:41")
    private String beginTime;

    @ApiModelProperty(value = "时间(末) 例:2019-12-19 07:48:41")
    private String endTime;
}
