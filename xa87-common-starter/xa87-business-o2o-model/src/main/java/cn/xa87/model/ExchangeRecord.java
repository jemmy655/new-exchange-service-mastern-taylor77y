package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

// 币币兑换记录
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_exchange_record")
public class ExchangeRecord extends Model<ExchangeRecord> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 用户ID
    private String memberId;
    // 原始币种
    private String currency;
    // 目标币种
    private String currencyTarget;
    // 资产变更前
    private BigDecimal quantity;
    // 资产变更后
    private BigDecimal afterExchangeQuantity;
    // 创建时间
    private Date createTime;
}
