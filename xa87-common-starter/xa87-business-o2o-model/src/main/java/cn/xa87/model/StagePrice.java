package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自发币时间阶段价格
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_stage_price")
public class StagePrice extends Model<StagePrice> {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 币类型
     */
    private String tokenCur;
    /**
     * 时间类型：1m,5m,15m,30m,1h,1d,1w
     */
    private String timeType;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 成交量
     */
    private BigDecimal volume;
    /**
     * 成交额
     */
    private BigDecimal vol;
    /**
     * 成交笔数
     */
    private Integer count;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 收盘价
     */
    private BigDecimal close;

    private Integer status;

    private LocalDateTime createTime;
}
