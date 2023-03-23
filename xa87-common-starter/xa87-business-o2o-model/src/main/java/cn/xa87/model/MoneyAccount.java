package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("money_account")
public class MoneyAccount extends Model<MoneyAccount> {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 收款账号
     */
    private String account;
    /**
     * 收款名称
     */
    private String username;
    /**
     * 二维码
     */
    private String qrcode;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 0 下架 1上架
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 银行卡名称
     */
    private String bankname;

    /**
     * '1、usdt2、银行卡'
     */
    private Integer type;

}
