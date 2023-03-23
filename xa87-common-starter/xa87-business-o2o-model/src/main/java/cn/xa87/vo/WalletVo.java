package cn.xa87.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_wallet")
public class WalletVo extends Model<WalletVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户
     */
    private String member;
    /**
     * ETH钱包地址
     */
    private String ethWallet;
    /**
     * BTC钱包地址
     */
    private String btcWallet;
    /**
     * USDT钱包地址
     */
    private String usdtWallet;
    /**
     * 钱包地址
     */
    private String password;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
