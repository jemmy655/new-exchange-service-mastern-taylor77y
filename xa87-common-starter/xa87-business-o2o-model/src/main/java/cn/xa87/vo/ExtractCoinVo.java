package cn.xa87.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2020/2/6 12:43
 */
@ApiModel(value = "提币记录")
@Data
public class ExtractCoinVo implements Serializable {

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "费")
    private BigDecimal balance;

    @ApiModelProperty(value = "交易id")
    private String hex;

    @ApiModelProperty(value = "提币地址")
    private String wallet;

    @ApiModelProperty(value = "手续费")
    private BigDecimal handlingFee;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "类型: 1 虚拟币提现，2 银行卡提现")
    private Integer type;

    //
    /**
     * 链名
     */
    private String chainName;
    /**
     * 银行卡姓名
     */
    private String bankUserName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 开户行
     */
    private String bankAddress;
    /**
     * 银行卡号
     */
    private String bankCard;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 汇率换算后金额
     */
    private BigDecimal amount;
    /**
     * 货币符号
     */
    private String currencySymbol;

    @ApiModelProperty(value = "时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date extractTime;
}
