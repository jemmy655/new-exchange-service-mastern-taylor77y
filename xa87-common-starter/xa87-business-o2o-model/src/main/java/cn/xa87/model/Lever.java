package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_lever")
public class Lever extends Model<Lever> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 倍数
     */
    private BigDecimal lever;
    /**
     * 描述
     */
    private String leverDesc;
    /**
     * 交易对名称
     */
    private String pairsName;
    /**
     * 手数
     */
    private int handsMax;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
