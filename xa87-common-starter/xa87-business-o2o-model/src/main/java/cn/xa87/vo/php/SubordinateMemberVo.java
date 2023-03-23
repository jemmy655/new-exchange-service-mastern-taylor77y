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
 * @date 2020/2/4 17:44
 */
@ApiModel(value = "伞级用户")
@Data
public class SubordinateMemberVo implements Serializable {

    @ApiModelProperty(value = "自己的账号")
    private String myselfNumber;

    @ApiModelProperty(value = "上级账号")
    private String superiorNumber;

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "充值数量")
    private BigDecimal rechargeFee;

    @ApiModelProperty(value = "提现数量")
    private BigDecimal extractFee;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "时间")
    private Date createTime;
}
