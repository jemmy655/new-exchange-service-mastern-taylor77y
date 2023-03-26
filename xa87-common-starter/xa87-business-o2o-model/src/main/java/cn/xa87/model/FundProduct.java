package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fund_product")
public class FundProduct extends Model<FundProduct> {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId("id")
    private Long id;

    /** 简体名称 */
    @TableField("zh_name")
    private String zhName;

    /** 繁体名称 */
    @TableField("zh_tw_name")
    private String zhTwName;

    /** 英文名称 */
    @TableField("en_name")
    private String enName;

    /** 韩文名称 */
    @TableField("ko_ame")
    private String koName;

    /** 日文名称 */
    @TableField("ja_name")
    private String jaName;

    /** 产品图片 */
    @TableField("fund_image")
    private String fundImage;

    /** 周期（天） */
    @TableField("period_day")
    private Integer periodDay;

    /** 日利率%（前） */
    @TableField("day_rate+_front")
    private BigDecimal dayRateFront;

    /** 日利率%(后) */
    @TableField("day_rate_behind")
    private BigDecimal dayRateBehind;

    /** 今日利率% */
    @TableField("today_rate")
    private BigDecimal todayRate;

    /** 违约结算比列% */
    @TableField("default_ratio")
    private BigDecimal defaultRatio;

    /** 投资金额USDT（小） */
    @TableField("investment_amount_front")
    private BigDecimal investmentAmountFront;

    /** 投资金额USDT（大） */
    @TableField("investment_amount_behind")
    private BigDecimal investmentAmountBehind;

    /** 数量限制 */
    @TableField("num_astrict")
    private Integer numAstrict;

    /** 状态(0停用，1启用) */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
}
