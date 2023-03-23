package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_balance_record")
public class BalanceRecord extends Model<BalanceRecord> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 用户ID
    private String memberId;
    // 币种
    private String currency;

    /**
     * 资金类型
     * 10、永续合约-可用减少
     * 11、永续合约-可用增加
     * 12、永续合约-冻结减少
     * 13、永续合约-冻结增加
     * 14、永续合约-收益
     * 15、永续合约-爆仓
     *
     * 21、法币购买-成功
     * 22、法币出售-冻结
     * 23、法币出售-驳回解冻
     * 31、币币买入-冻结
     * 32、币币买入-撤回订单
     * 33、币币买入-冻结转入
     * 34、币币卖出-冻结
     * 35、币币卖出-撤回订单
     * 36、币币卖出-冻结转入
     *
     * 37、币币买入-冻结减少
     * 38、币币买入-可用增加
     * 39、币币卖出-冻结减少
     * 40、币币卖出-可用增加
     *
     * 41、秒合约买涨-冻结
     * 42、秒合约买涨-解冻
     * 43、秒合约买涨-盈利
     * 44、秒合约买跌-冻结
     * 45、秒合约买跌-解冻
     * 46、秒合约买跌-盈利
     * 51、充值
     * 52、虚拟充值
     * 53、提现
     * 54、虚拟下分
     * 55、提现驳回
     * 56、资金可用减少
     * 57、资金可用增加
     */
    // 资金类型
    private Integer balanceType;
    // 交易类型：1支出，2收入
    private Integer fundsType;
    // 资产变更前
    private BigDecimal balanceBefore;
    // 资产变更后
    private BigDecimal balanceBack;
    // 资产差额
    private BigDecimal balanceDifference;
    // 创建时间
    private Date createTime;
    // 数据分类：1合约，2法币，3币币，4秒合约，5充提
    private Integer dataClassification;

}
