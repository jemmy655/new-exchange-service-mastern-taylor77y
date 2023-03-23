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
@TableName("t_seconds_bet_log")
public class SecondsBetLog extends Model<SecondsConfig> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 会员ID
    private String memberId;
    // 秒配置ID
    private Integer sid;
    // 虚拟币ID
    private Integer ckid;
    // 虚拟币币种s
    private String ckType;
    // 虚拟币名称
    private String ckName;
    // 开始价格
    private BigDecimal startAmount;
    // 结束价格
    private BigDecimal finishsAmount;
    // 下注金额
    private BigDecimal amount;
    // 买入状态 1涨2跌getRechargeWallet
    private Integer buyStatus;
    // 结算状态 1结2未
    private Integer settleStatus;
    // 是否输赢
    private Integer isWin;
    // 是否控盘
    private Integer isControl;
    // 控盘配置
    private BigDecimal controlPecent;
    // 创建时间
    private Date createTime;
    // 结算时间
    private Date settleTime;

    // 利润
    private BigDecimal profit;

    // 返回数据使用，时分
    // 秒数
    @TableField(exist = false)
    private Integer seconds;
    @TableField(exist = false)
    private String secondsTime;
    @TableField(exist = false)
    private int timing;
}
