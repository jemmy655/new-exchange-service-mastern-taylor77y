package cn.xa87.model;

import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_entrust_history")
public class EntrustHistory extends Model<EntrustHistory> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 成交价格
     */
    private BigDecimal matchPrice;
    /**
     * 交易对ID
     */
    private String pairs;
    /**
     * 交易对名称
     */
    private String pairsName;
    /**
     * 主币
     */
    private String mainCur;
    /**
     * 代币
     */
    private String tokenCur;
    /**
     * 数量
     */
    private BigDecimal count;
    /**
     * 百分比
     */
    private BigDecimal percentageCount;
    /**
     * 成交额
     */
    private BigDecimal matchFee;
    /**
     * 已成交数量
     */
    private BigDecimal matchCount;
    /**
     * 剩余数量
     */
    private BigDecimal surplusCount;
    /**
     * 用户
     */
    private String member;
    /**
     * 用户类型
     */
    private String memberType;
    /**
     * 委托类型
     */
    private EntrustConstant.Entrust_Type entrustType;
    /**
     * 成交类型
     */
    private EntrustConstant.Method_Type methodType;
    /**
     * 价格类型
     */
    private ContractConstant.Price_Type priceType;
    /**
     * 状态
     */
    private EntrustConstant.Order_State state;
    /**
     * 手续费
     */
    private BigDecimal tradeFee;
    /**
     * 手续费费率
     */
    private BigDecimal tradeRate;

    /**
     * 撮合用户
     */
    private String matchMember;
    /**
     * 排序
     */
    @TableField(exist = false)
    private long sort;
    /**
     * 操作类型
     */
    @TableField(exist = false)
    private String uld;
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
}
