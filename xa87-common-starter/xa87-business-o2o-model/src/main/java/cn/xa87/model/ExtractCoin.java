package cn.xa87.model;

import cn.xa87.constant.BalanceConstant;
import com.alibaba.fastjson.annotation.JSONField;
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
@TableName("t_extract_coin")
public class ExtractCoin extends Model<ExtractCoin> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 标题
     */
    private String member;
    /**
     * 数量
     */
    private BigDecimal balance;
    /**
     * 手续费
     */
    private BigDecimal handlingFee;
    /**
     * 币种
     */
    private String currency;
    /**
     * 提币地址
     */
    private String wallet;
    /**
     * 状态
     */
    private BalanceConstant.Extract_State state;

    /**
     * 提币地址
     */
    private String hex;


    private String viewInExplorer;

    private String fromAddress;
    private String toAddress;
    private String tokenNumber;
    private Date extractTime;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;


    private Integer type;

    // 链名
    @TableField("chain_name")
    private String chainName;

    /**
     * 银行卡姓名
     */
    @TableField("bank_user_name")
    private String bankUserName;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 开户行
     */
    @TableField("bank_address")
    private String bankAddress;
    /**
     * 银行卡号
     */
    @TableField("bank_card")
    private String bankCard;
    /**
     * 汇率
     */
    @TableField("exchange_rate")
    private BigDecimal exchangeRate;
    /**
     * 汇率换算后金额
     */
    @TableField("amount")
    private BigDecimal amount;
    /**
     * 货币符号
     */
    @TableField("currency_symbol")
    private String currencySymbol;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
