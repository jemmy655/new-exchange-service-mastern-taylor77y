package cn.xa87.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@ApiModel(value = "基金订单")
@Data
public class FundOrderVo implements Serializable {

    /** ID */
    @ApiModelProperty(value = "ID")
    private String id;

    /** 订单号（时间+6位随机数） */
    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    /** 基金产品Id */
    @ApiModelProperty(value = "基金产品Id")
    private String fundProductId;

    /** 会员id */
    @ApiModelProperty(value = "用户id")
    private String memberId;

    /** 起息日 */
    @ApiModelProperty(value ="起息日")
    private Date valueDate;

    /** 结束起息日 */
    @ApiModelProperty(value ="结束起息日")
    private Date finishValueDate;

    /** 周期（天） */
    @ApiModelProperty(value ="周期（天）")
    private Integer periodDay;

    /** 剩余（天） */
    @ApiModelProperty(value ="剩余（天）")
    private Integer residueDay;

    /** 购入金额 */
    @ApiModelProperty(value ="违约金（发生强赎产生）")
    private BigDecimal price;

    /** 累计收益额 */
    @ApiModelProperty(value ="累计收益额")
    private BigDecimal accumulatedIncome;

    /** 违约金（发生强赎产生） */
    @ApiModelProperty(value ="违约金（发生强赎产生）")
    private BigDecimal penalPrice;

    /** 状态(0托管中，1已赎回，2强赎) */
    @ApiModelProperty(value ="状态(0托管中，1已赎回，2强赎)")
    private Integer enabled;

    /**
     * 创建时间
     */
    @ApiModelProperty(value ="创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value ="修改时间")
    private Date updateTime;

}
