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
@TableName("t_user_blance_collect")
public class UserBlanceCollect extends Model<UserBlanceCollect> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 用户Id
     */
    private String member;
    /**
     * 币种
     */
    private String currency;
    /**
     * 冻结余额
     */
    private BigDecimal sumChong;
    /**
     * 余额
     */
    private BigDecimal sumTi;
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
