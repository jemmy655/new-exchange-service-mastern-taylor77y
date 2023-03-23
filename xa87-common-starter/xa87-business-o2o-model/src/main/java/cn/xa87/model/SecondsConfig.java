package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_seconds_config")
public class SecondsConfig extends Model<SecondsConfig> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 秒数
    private Integer seconds;
    // 赔率
    private BigDecimal odds;
    // 最低下注金额
    private BigDecimal minimum;
    // 时分（s秒，m分钟，h小时）
    private String secondsTime;
    // 创建时间
    private Date createTime;
    //排序
    private Integer sort;
}
