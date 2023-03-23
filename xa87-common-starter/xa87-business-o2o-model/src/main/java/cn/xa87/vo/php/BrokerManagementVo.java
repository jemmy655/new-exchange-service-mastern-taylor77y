package cn.xa87.vo.php;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/4 22:14
 */
@ApiModel(value = "经纪人管理要用的实体,伞下用户信息")
@Data
public class BrokerManagementVo implements Serializable {

    @ApiModelProperty(value = "自己的账号")
    private String myselfNumber;

    @ApiModelProperty(value = "上级账号")
    private String superiorNumber;
}
