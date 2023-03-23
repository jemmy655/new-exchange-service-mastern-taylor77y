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
 * @date 2020/2/10 12:43
 */
@ApiModel(value = "合约账户出入金")
@Data
public class DealVo implements Serializable {

    @ApiModelProperty(value = "手机号或邮箱")
    private String number;

    @ApiModelProperty(value = "用户id")
    private String memberId;

    @ApiModelProperty(value = "USDT余额")
    private BigDecimal usdtBalance;

    @ApiModelProperty(value = "划转类型(币币到合(BALANCE_MOVE_TOKEN)合约到比比(TOKEN_MOVE_BALANCE))")
    private String transferType;

    @ApiModelProperty(value = "划转数量")
    private BigDecimal fee;

    @ApiModelProperty(value = "划转时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
