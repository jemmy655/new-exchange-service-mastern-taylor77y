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
@TableName("t_perpetual_contract_order")
public class PerpetualContractOrder extends Model<PerpetualContractOrder>{
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 用户名称
     */
    @TableId(value = "member_id")
    private String memberId;

    /**
     * 交易对名称
     */
    @TableId(value = "pairs_name")
    private String pairsName;

    /**
     * 开始价
     */
    @TableId(value = "k_price")
    private BigDecimal kPrice;

    /**
     * 结束价
     */
    @TableId(value = "b_price")
    private BigDecimal bPrice;

    /**
     * 合约金额
     */
    @TableId(value = "amount")
    private BigDecimal amount;

    /**
     * 保证金
     */
    @TableId(value = "margin")
    private BigDecimal margin;


    /**
     * 手续费
     */
    @TableId(value = "match_fee")
    private BigDecimal matchFee;

    /**
     * 收益
     */
    @TableId(value = "profit")
    private BigDecimal profit;

    /**
     * 是否输赢
     */
    @TableId(value = "is_win")
    private int isWin;

    /**
     * 是否控盘
     */
    @TableId(value = "is_control")
    private int isControl;

    /**
     * 控盘价
     */
    @TableId(value = "control_price")
    private BigDecimal controlPrice;

    /**
     * 杠杆ID
     */
    @TableId(value = "lever_id")
    private String leverId;

    /**
     * 杠杆数
     */
    @TableId(value = "lever_num")
    private BigDecimal leverNum;

    /**
     * 杠杆说明
     */
    @TableId(value = "lever_desc")
    private String leverDesc;

    /**
     * 结算状态（持仓，已平仓）
     */
    @TableId(value = "order_state")
    private String orderState;

    /**
     * 交易类型（开多，开空，平多，平空）
     */
    @TableId(value = "trade_type")
    private String tradeType;

    /**
     * 手数
     */
    @TableId(value = "contract_hands")
    private BigDecimal contractHands;

    /**
     * 开始时间
     */
    @TableId(value = "create_time")
    private Date createTime;

    /**
     * 结束时间
     */
    @TableId(value = "settle_time")
    private Date settleTime;

    public PerpetualContractOrder(String memberId, String pairsName, BigDecimal kPrice, BigDecimal bPrice, BigDecimal amount, BigDecimal margin, BigDecimal matchFee, BigDecimal profit, int isWin, int isControl, BigDecimal controlPrice, String leverId, BigDecimal leverNum, String leverDesc, String orderState, String tradeType, BigDecimal contractHands, Date createTime, Date settleTime) {
        this.memberId = memberId;
        this.pairsName = pairsName;
        this.kPrice = kPrice;
        this.bPrice = bPrice;
        this.amount = amount;
        this.margin = margin;
        this.matchFee = matchFee;
        this.profit = profit;
        this.isWin = isWin;
        this.isControl = isControl;
        this.controlPrice = controlPrice;
        this.leverId = leverId;
        this.leverNum = leverNum;
        this.leverDesc = leverDesc;
        this.orderState = orderState;
        this.tradeType = tradeType;
        this.contractHands = contractHands;
        this.createTime = createTime;
        this.settleTime = settleTime;
    }
}
