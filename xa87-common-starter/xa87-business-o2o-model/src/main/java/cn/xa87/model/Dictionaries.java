package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dictionaries")
public class Dictionaries implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 键
     */
    private String ukey;
    /**
     * 值
     */
    private String uvalue;
    /**
     * 描述信息
     */
    private String info;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
