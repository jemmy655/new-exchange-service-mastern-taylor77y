package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_brokerage_record")
public class BrokerageRecord implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消费者userId
     */
    private String consumeUserId;
    /**
     * 回扣的userId
     */
    private String brokerageUserId;
    /**
     * 回扣者的电话
     */
    private String brokeragePhone;
    /**
     * 消费币种
     */
    private String currency;
    /**
     * 返佣数量
     */
    private BigDecimal brokerageNumber;
    /**
     * 消费数量
     */
    private BigDecimal number;
    /**
     * 待计算分佣(0)已计算分佣(1)
     */
    private Byte status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
