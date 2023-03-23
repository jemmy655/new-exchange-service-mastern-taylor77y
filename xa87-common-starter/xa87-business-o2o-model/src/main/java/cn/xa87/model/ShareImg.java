package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_img")
public class ShareImg extends Model<ShareImg> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键IDid
     * image
     * type
     * create_time
     * update_time
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 交易对名称
     */
    private String image;
    /**
     * 类型，SHARE_APP,SHARE_REG
     */
    private String type;

    private Date create_time;
    private Date update_time;

}
