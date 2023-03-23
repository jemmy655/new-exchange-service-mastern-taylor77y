package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_billing_address")
public class BillingAddress extends Model<BillingAddress> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 用户ID
    private String memberId;
    // 币种
    private String currency;
    // 币种子类型
    private String currencyType;
    // 收款地址
    private String address;
    // 创建时间
    private Date createTime;

}
