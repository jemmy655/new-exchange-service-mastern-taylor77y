package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_currency_controls")
public class CurrencyControls extends Model<CurrencyControls> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 币名称
     */
    private String tokenCur;
    /**
     * 起始价格
     */
    private BigDecimal startingPrice;
    /**
     * 结束价格
     */
    private BigDecimal finalPrice;
    /**
     * 涨跌状态：1 涨，2 跌
     */
    private Integer amplitudeStatus;
    /**
     * 状态：1 启用，2 停用
     */
    private Integer status;
    /**
     * 最大交易量
     */
    private BigDecimal tradingVolumeMax;
    /**
     *最小交易量
     */
    private BigDecimal tradingVolumeMin;



    private Date createTime;
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
