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
@TableName("t_member_recharge")
public class MemberRecharge extends Model<MemberRecharge> {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户Id
     */
    private String memberId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 充值余额
     */
    private BigDecimal amount;

    // 状态 1 待审核, 2 审核通过 , 3 审核拒绝
    private Integer status;
    // 审核信息
    private String remark;
    // 创建时间
    private Date createTime;

    // 链名
    private String chainName;

    // 支付凭证
    private String paymentVoucher;
}
