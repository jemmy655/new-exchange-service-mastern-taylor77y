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

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_balance")
public class Balance extends Model<Balance> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 冻结余额(币币)
     */
    //private BigDecimal blockedBalance;
    /**
     * 余额(币币)
     */
    //private BigDecimal balance;
    /**
     * 资产冻结余额(充提)
     */
    private BigDecimal assetsBlockedBalance;
    /**
     * 资产余额(充提)
     */
    private BigDecimal assetsBalance;
    /**
     * 法币冻结余额
     */
    //private BigDecimal fbBlockedBalance;
    /**
     * 法币冻结余额
     */
    //private BigDecimal fbBalance;
    /**
     * 合约余额
     */
    //private BigDecimal tokenBalance;
    /**
     * 合约冻结余额
     */
    //private BigDecimal tokenBlockedBalance;
    /**
     * 私募余额(静态释放)
     */
    private BigDecimal raiseBalance;


    private BigDecimal chainBalance;
    /**
     * 币币约等于USDT金额
     */
    @TableField(exist = false)
    private BigDecimal scaleBalanceUsdt;
    /**
     * 币币约等于CNY金额
     */
    @TableField(exist = false)
    private BigDecimal scaleBalanceCny;
    /**
     * 合约约等于USDT金额
     */
    @TableField(exist = false)
    private BigDecimal scaleTokenUsdt;
    /**
     * 合约约等于CNY金额
     */
    @TableField(exist = false)
    private BigDecimal scaleTokenCny;
    /**
     * 法币约等于USDT金额
     */
    @TableField(exist = false)
    private BigDecimal scaleFbUsdt;
    /**
     * 法币约等于CNY金额
     */
    @TableField(exist = false)
    private BigDecimal scaleFbCny;
    /**
     * 充提约等于USDT金额
     */
    @TableField(exist = false)
    private BigDecimal scaleAssetsUsdt;
    /**
     * 充提约等于CNY金额
     */
    @TableField(exist = false)
    private BigDecimal scaleAssetsCny;


    /**
     * 备注
     */
    private String remarks;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
