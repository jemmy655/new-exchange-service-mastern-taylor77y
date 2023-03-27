package cn.xa87.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SmartPoolOrderVo implements Serializable {
    /** ID */
    private Long id;

    /** 订单号（时间+6位随机数） */
    private String orderNumber;

    /** 基金产品Id */
    private Long productId;

    /** 会员id */
    private String memberId;

    /** 起息日 */
    private Date valueDate;

    /** 结束起息日 */
    private Date finishValueDate;

    /** 周期（天） */
    private Integer periodDay;

    /** 剩余（天） */
    private Integer residueDay;

    /** 金额 */
    private BigDecimal price;

    /** 累计收益额 */
    private BigDecimal accumulatedIncome;

    /** 违约金（发生强赎产生） */
    private BigDecimal penalPrice;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 状态(0托管中，1已赎回，2强赎) */
    private Integer enabled;

    private String productName;

    private String productNameEn;

    private String UID;

    private String UName;
}
