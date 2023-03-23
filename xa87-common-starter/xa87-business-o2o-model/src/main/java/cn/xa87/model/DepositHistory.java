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
@TableName("t_deposit_history")
public class DepositHistory extends Model<DepositHistory> {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * member
     */
    private String member;
    /**
     * coin
     */
    @TableField("to_address")
    private String to;

    @TableField("tx_hash")
    private String hash;
    /**
     * token
     */
    @TableField("from_address")
    private String from;
    /**
     * coin
     */
    private String coin;
    /**
     * contract
     */
    private String contract;
    /**
     * contract
     */
    @TableField("block_number")
    private String blockNumber;
    /**
     * balance
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    public DepositHistory() {

    }
}
