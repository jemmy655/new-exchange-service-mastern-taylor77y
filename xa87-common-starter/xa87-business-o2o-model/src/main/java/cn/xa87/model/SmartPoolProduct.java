package cn.xa87.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 t_smart_pool_product
 * 
 * @author ruoyi
 * @date 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_smart_pool_product")
public class SmartPoolProduct extends Model<SmartPoolProduct>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /** 简体名称 */
     @TableField(  "zh_name")
    
    private String zhName;

    /** 繁体名称 */
     @TableField(  "zh_tw_name")
    private String zhTwName;

    /** 英文名称 */
     @TableField(  "en_name")
    private String enName;

    /** 韩文名称 */
     @TableField(  "ko_name")
    private String koName;

    /** 日文名称 */
     @TableField(  "ja_name")
    private String jaName;

    /** 矿机购买币种 */
     @TableField(  "buy_pairs_name")
    private String buyPairsName;

    /** 矿机产出币种 */
     @TableField(  "out_pairs_name")
    private String outPairsName;

    /** 周期（天） */
     @TableField(  "period_day")
    private Integer periodDay;

    /** 可解锁周期(天) */
     @TableField(  "period_day_unlock")
    private Integer periodDayUnlock;

    /** 日利率% */
     @TableField(  "day_rate")
    private BigDecimal dayRate;

    /** 今日利率% */
     @TableField(  "today_rate")
    private BigDecimal todayRate;

    /** 投资金额USDT（小） */
     @TableField(  "investment_amount_front")
    private BigDecimal investmentAmountFront;

    /** 投资金额USDT（大） */
     @TableField(  "investment_amount_behind")
    private BigDecimal investmentAmountBehind;

    /** 数量限制 */
     @TableField(  "num_astrict")
    private Integer numAstrict;

    /** 状态(0停用，1启用) */
     @TableField(  "enabled")
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
    /**
     * 产品信息
     */
    @TableField("detail")
    private String detail;
}
