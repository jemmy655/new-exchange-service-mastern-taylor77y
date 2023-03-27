package cn.xa87.vo;

import cn.xa87.constant.TokenOrderConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "永续合约实体类")
public class PerpetualContractOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private String id;

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private String memberId;

    /**
     * 交易对名称
     */
    @NotBlank(message = "交易对名称不能为空")
    @ApiModelProperty("交易对名称")
    private String pairsName;

    /**
     * 币名称
     */
    @NotBlank(message = "币名称不能为空")
    @ApiModelProperty("币名称 (USDT)")
    private String coinName;

    /**
     * 开始价
     */
    @NotBlank(message = "交易对价格不能为空")
    @ApiModelProperty("交易对价格")
    private BigDecimal kPrice;

    /**
     * 结束价
     */
    private BigDecimal bPrice;

    /**
     * 合约金额
     */
    @NotBlank(message = "合约金额不能为空")
    @ApiModelProperty("合约金额")
    private BigDecimal amount;

    /**
     * 保证金
     */
    @NotBlank(message = "保证金额不能为空")
    @ApiModelProperty("保证金")
    private BigDecimal margin;


    /**
     * 手续费
     */
    @NotBlank(message = "手续费额不能为空")
    @ApiModelProperty("手续费")
    private BigDecimal matchFee;

    /**
     * 收益
     */
    private BigDecimal profit;

    /**
     * 收益率
     */
    private String profitUp;

    /**
     * 是否输赢
     */
    private int isWin;

    /**
     * 是否控盘
     */
    private int isControl;

    /**
     * 控盘价
     */
    private BigDecimal controlPrice;

    /**
     * 杠杆ID
     */
    @NotBlank(message = "杠杆ID不能为空")
    @ApiModelProperty("杠杆ID")
    private String leverId;

    /**
     * 杠杆数
     */
    private BigDecimal leverNum;

    /**
     * 杠杆说明
     */
    private String leverDesc;

    /**
     * 结算状态（委托完成 ENTRUSTCOMPLETE,持仓 POSITIONS  已平仓 CLOSEOUT）
     */
    private String orderState;

    /**
     * 交易类型（开多，开空，平多，平空）
     */
    @NotBlank(message = "交易类型不能为空")
    @ApiModelProperty("交易类型")
    private String tradeType;



    /**
     * 手数
     */
    @NotBlank(message = "购买张数不能为空")
    @ApiModelProperty("购买张数")
    private BigDecimal contractHands;

    /**
     * 开始时间
     */
    private Data createTime;

    /**
     * 结束时间
     */
    private Data settleTime;
}
