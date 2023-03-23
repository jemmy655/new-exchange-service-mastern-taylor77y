package cn.xa87.wallet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_member_wallet")
public class MemberWallet extends Model<MemberWallet> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 地址
     */
    private String address;
    /**
     * 私有key
     */
    private String privateKey;
    /**
     * 共有key
     */
    private String publicKey;
    /**
     * wif
     */
    private String wif;
    /**
     * 类型
     */
    private String webhook;
    /**
     * 类型
     */
    private String type;
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

    public MemberWallet(String name, String password, String address, String privateKey, String publicKey, String wif, String type, String webhook) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.wif = wif;
        this.type = type;
        this.webhook = webhook;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}