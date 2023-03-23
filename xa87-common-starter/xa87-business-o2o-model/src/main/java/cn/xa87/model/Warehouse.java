package cn.xa87.model;

import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_warehouse")
public class Warehouse extends Model<Warehouse> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 交易对
     */
    @ApiModelProperty(value = "交易对名称")
    private String pairsName;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种名称")
    private String coinName;
    /**
     * 主币名称
     */
    @ApiModelProperty(value = "主币名称")
    private String mainCur;
    /**
     * 保证金
     */
    @ApiModelProperty(value = "保证金--不用传")
    private BigDecimal margin;
    /**
     * 未实现盈亏
     */
    @ApiModelProperty(value = "未实现盈亏--不用传")
    private BigDecimal unProfitLoss;
    /**
     * 收益
     */
    @ApiModelProperty(value = "收益--不用传")
    private BigDecimal profit;
    /**
     * 收益率
     */
    @ApiModelProperty(value = "收益率--不用传")
    private BigDecimal profitUp;
    /**
     * 仓位总价
     */
    @ApiModelProperty(value = "仓位价格--不用传")
    private BigDecimal tokenPrice;
    /**
     * 仓位总数
     */
    @ApiModelProperty(value = "仓位总数--不用传")
    private BigDecimal tokenNum;
    /**
     * 仓位可用数
     */
    @ApiModelProperty(value = "仓位可用数--不用传")
    private BigDecimal isTokenNum;
    /**
     * 杠杆倍数
     */
    @ApiModelProperty(value = "平均杠杆数--不用传")
    private BigDecimal avgLevel;
    /**
     * 总手数
     */
    @ApiModelProperty(value = "总手数--不用传")
    private BigDecimal hands;
    /**
     * 总单数
     */
    @ApiModelProperty(value = "总单数数--不用传")
    private BigDecimal orders;
    /**
     * 仓位均价
     */
    @ApiModelProperty(value = "仓位价格--不用传")
    private BigDecimal avePrice;

    /**
     * 强平价格
     */
    @ApiModelProperty(value = "强平价格--不用传")
    private BigDecimal forcePrice;

    /**
     * 预估强平价格
     */
    @ApiModelProperty(value = "预估强平价格--不用传")
    private BigDecimal closePrice;
    /**
     * 止盈价格
     */
    @ApiModelProperty(value = "止盈价格--不用传")
    private BigDecimal triggerPrice;
    /**
     * 止损价格
     */
    @ApiModelProperty(value = "止损价格--不用传")
    private BigDecimal ordPrice;
    /**
     * 用户
     */
    @ApiModelProperty(value = "用户ID")
    private String member;
    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    private ContractConstant.Trade_Type tradeType;
    /**
     * 交易类型
     */
    @ApiModelProperty(value = "状态")
    private TokenOrderConstant.Order_State state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * 0不控制,1赢,2输
     */
    @TableField("win")
    private Integer win;

    /**
     * 记录控盘数
     */
    @TableField("control_price")
    private BigDecimal controlPrice;

}