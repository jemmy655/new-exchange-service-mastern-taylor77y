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
@TableName("t_bibi_day_record")
public class BiBiDayRecord extends Model<BiBiDayRecord> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 币种
     */
    private String currency;

    private BigDecimal sumBibiBuy;

    private BigDecimal sumBibiSell;

    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
