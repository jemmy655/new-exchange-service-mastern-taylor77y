package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 字典数据表 sys_dict_data
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_config")
public class SysConfig extends Model<RobotConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    private String paramName;

    private String paramKey;

    private String paramValue;

    private String commit;

    private Date updateTime;

    private Date create_time;

}
