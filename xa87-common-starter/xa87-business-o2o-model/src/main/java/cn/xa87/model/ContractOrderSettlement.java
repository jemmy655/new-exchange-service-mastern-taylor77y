package cn.xa87.model;

import cn.xa87.constant.TokenOrderConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/11 9:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_contract_order_settlement")
public class ContractOrderSettlement implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 用户id
     */
    private String memberId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 订单状态
     */
    private TokenOrderConstant.Order_State orderState;
    /**
     * 变化的钱
     */
    private BigDecimal changeFee;
    /**
     * 盈亏状态:盈(0)亏(1)
     */
    private Byte state;
    /**
     * 交易后(变化后)的余额（与币种对应）
     */
    private BigDecimal balance;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
