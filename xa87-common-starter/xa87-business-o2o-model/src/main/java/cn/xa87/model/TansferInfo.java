package cn.xa87.model;

import cn.xa87.constant.BalanceConstant;
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
@TableName("t_transfer_info")
public class TansferInfo extends Model<TansferInfo> {
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
     * 类型
     */
    private BalanceConstant.Move_Type type;
    /**
     * 币种名称
     */
    private String currency;
    /**
     * 数量
     */
    private BigDecimal fee;
    /**
     * 余额
     */
    private BigDecimal balance;
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

    public TansferInfo(String member, BalanceConstant.Move_Type type, String currency, BigDecimal fee, BigDecimal balance) {
        this.member = member;
        this.type = type;
        this.currency = currency;
        this.fee = fee;
        this.balance = balance;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
