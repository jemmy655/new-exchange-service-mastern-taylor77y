package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_kline_data")
public class KlineData extends Model<KlineData> {
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
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 收盘价（当K线为最晚的一根时，是最新成交价）
     */
    private BigDecimal close;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 成交量
     */
    private BigDecimal volume;
    /**
     * 成交额, 即 sum(每一笔成交价 * 该笔的成交量)
     */
    private BigDecimal vol;
    /**
     * 成交笔数
     */
    private Integer count;
    /**
     * 当前价格
     */
    private BigDecimal price;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 状态：1 未用，2 已使用
     */
    private Integer status;

}
