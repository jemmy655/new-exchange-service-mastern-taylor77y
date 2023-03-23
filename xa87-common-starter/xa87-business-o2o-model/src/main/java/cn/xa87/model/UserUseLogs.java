package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_use_logs")
public class UserUseLogs extends Model<UserUseLogs> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * banner 地址
     */
    private String userId;
    /**
     * 链接地址
     */
    private String methodName;
    private String methodParm;
    private String methodBody;
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
