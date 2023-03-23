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
@TableName("t_member_currency_config")
public class MemberCurrencyConfig extends Model<MemberCurrencyConfig> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 币种名称
    private String currencyName;

    // 汇率
    private BigDecimal rate;

    // 货币符号
    private String mark;

    // 排序
    private Integer sort;

    // 创建时间
    private Date createTime;

}
