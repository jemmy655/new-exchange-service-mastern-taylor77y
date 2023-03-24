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
@TableName("t_contract_order")
public class ContractOrder extends Model<ContractOrder> {
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
     * 杠杆倍数
     */
    @ApiModelProperty(value = "杠杆倍数--不用传")
    private BigDecimal leverNum;
    /**
     * 杠杆倍数描述
     */
    @ApiModelProperty(value = "杠杆倍数描述--不用传")
    private String leverDesc;
    /**
     * 保证金
     */
    @ApiModelProperty(value = "保证金--不用传")
    private BigDecimal margin;
    /**
     * 杠杆手数
     */
    @ApiModelProperty(value = "杠杆手数--不用传")
    private BigDecimal contractHands;
    /**
     * 建仓价额
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 平仓价格
     */
    @ApiModelProperty(value = "平仓价格")
    private BigDecimal matchPrice;
    /**
     * 收益  建仓价额-平仓价格
     */
    @ApiModelProperty(value = "收益")
    private BigDecimal matchFee;
    /**
     * 平仓类型
     */
    @ApiModelProperty(value = "平仓类型")
    private TokenOrderConstant.Close_Type closeType;
    /**
     * 数量
     */
    @ApiModelProperty(value = "币数量--不用传")
    private BigDecimal coinNum;
    /**
     * 可用合约手数
     */
    @ApiModelProperty(value = "可用合约手数--不用传")
    private BigDecimal isContractHands;
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
     * 价格类型
     */
    @ApiModelProperty(value = "价格类型/市价/自由输入")
    private ContractConstant.Price_Type priceType;
    /**
     * 状态
     *  CREATE("CREATE"), 已提交
     *  CANCEL("CANCEL"), 已撤销
     *  FINAL("FINAL");   委托财务
     */
    @ApiModelProperty(value = "状态---不用传")
    private TokenOrderConstant.Order_State orderState;
    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费--不用传")
    private BigDecimal takeFee;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型---不用传")
    private TokenOrderConstant.Order_Type orderType;
    /**
     * 配置类型Id
     */
    @ApiModelProperty(value = "配置类型Id")
    private String contractMulId;
    /**
     * 配置类型对象
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "配置类型对象--不传")
    private ContractMul contractMul;
    /**
     * 扛杆类型Id
     */
    @ApiModelProperty(value = "扛杆类型Id")
    private String leverId;
    /**
     * 杠杆类型对象
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "配置类型对象--不传")
    private Lever lever;
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


    @TableField(exist = false)
    private BigDecimal KPrice;//开仓金额   每手乘以1000

    @TableField(exist = false)
    private BigDecimal BPrice;//可平金额   每手乘以1000

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}