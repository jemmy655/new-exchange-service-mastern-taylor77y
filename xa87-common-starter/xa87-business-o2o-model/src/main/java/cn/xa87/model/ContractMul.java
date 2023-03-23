package cn.xa87.model;

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
@TableName("t_contract_mul")
public class ContractMul extends Model<ContractMul> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 交易对
     */
    private String pairsName;
    /**
     * 合约乘数
     */
    private BigDecimal contractMul;
    /**
     * 强平价格
     */
    private BigDecimal forcePrice;
    /**
     * 开仓手续费
     */
    private BigDecimal makerFee;
    /**
     * 平仓手续费
     */
    private BigDecimal takerFee;
    /**
     * 强平手续费
     */
    private BigDecimal forceFee;
    /**
     * 交易手续费
     */
    private BigDecimal tradeFee;
    /**
     * 提现手续费
     */
    private BigDecimal withdrawFee;
    /**
     * 维持保证金率
     */
    private BigDecimal ensure;

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