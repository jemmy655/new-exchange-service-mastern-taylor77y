package cn.xa87.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "充值信息")
@Data
public class MemberRechargeVo implements Serializable {
    /**
     * 用户Id
     */
    private String memberId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 充值余额
     */
    private BigDecimal amount;
}
