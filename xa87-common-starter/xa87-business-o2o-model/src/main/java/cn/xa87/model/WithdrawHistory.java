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
@TableName("t_withdraw_history")
public class WithdrawHistory extends Model<WithdrawHistory> {

    private static final long serialVersionUID = 1L;
    public static String WITHDRAW_CHAIN_ING = "WITHDRAW_CHAIN_ING";
    public static String WITHDRAW_SUCCESS = "WITHDRAW_SUCCESS";
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
    private String toAddress;
    @TableField("tx_hash")
    private String txHash;
    /**
     * token
     */
    @TableField("from_address")
    private String fromAddress;
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
    private String status;
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

    public WithdrawHistory() {

    }
}
