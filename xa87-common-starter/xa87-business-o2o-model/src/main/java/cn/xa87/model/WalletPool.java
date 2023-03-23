package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_wallet_pool")
public class WalletPool extends Model<WalletPool> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * coin
     */
    private String address;

    private String password;
    /**
     * member
     */
    private String member;
    /**
     * coin
     */
    private String coin;
    /**
     * balance
     */
    @TableField("usdt_balance")
    private BigDecimal usdtBalance;

    @TableField("eth_balance")
    private BigDecimal ETHBalance;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    public WalletPool() {

    }
}
