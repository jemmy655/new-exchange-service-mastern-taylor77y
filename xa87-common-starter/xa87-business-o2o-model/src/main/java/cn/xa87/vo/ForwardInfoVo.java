package cn.xa87.vo;

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
 * @date 2020/2/5 14:08
 */
@ApiModel(value = "充值记录")
@Data
public class ForwardInfoVo implements Serializable {

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "费")
    private BigDecimal fee;

    @ApiModelProperty(value = "钱包地址")
    private String wallet;

    @ApiModelProperty(value = "类型")
    private String type = "链上充币";

    @ApiModelProperty(value = "充值时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
