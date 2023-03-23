package cn.xa87.model;

import cn.xa87.constant.DataConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_notice")
public class Notice extends Model<Notice> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 链接地址
     */
    private String linkUrl;
    /**
     * 公告类型
     */
    private DataConstant.Notice_Type noticeType;
    /**
     * 收藏状态
     */
    private String isFavorite;
    /**
     * 浏览量
     */
    private int readCount;
    /**
     * 语言 类型
     */
    private DataConstant.Global_Type global;
    /**
     * 备用文本
     */
    private String bakText;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
