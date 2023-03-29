package cn.xa87.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PledgeOrderDetailVo implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 质押订单id
     */
    @ApiModelProperty(value ="质押订单id")
    private String orderId;

    /**
     * （BORROW）借款，(REFUND)还款 （RENEWAL）续借（NEWPLEDGE)新增质押
     */
    @ApiModelProperty(value ="(BORROW）借款，(REFUND)还款 （RENEWAL）续借（NEWPLEDGE)新增质押")
    private String type;

    /**
     * 借款金额，还款金额
     */
    @ApiModelProperty(value ="借款金额，还款金额")
    private BigDecimal money =new BigDecimal(0);


    /**
     * 质押类型
     */
    @ApiModelProperty(value ="质押类型 (币)")
    private String pledgeType;

    /**
     * 质押金额
     */
    @ApiModelProperty(value ="质押金额")
    private BigDecimal pledgePrice =new BigDecimal(0);

    @ApiModelProperty(value ="质押币名称")
    private String pledgeName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value ="创建时间")
    private Date creationTime;


}
