package cn.xa87.model;

import cn.xa87.constant.DataConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_banner")
public class Banner extends Model<Banner> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * banner 地址
     */
    private String bannerUrl;
    /**
     * 链接地址
     */
    private String linkUrl;
    /**
     * banner 类型
     */
    private DataConstant.Banner_Type bannerType;
    /**
     * 语言 类型
     */
    private DataConstant.Global_Type global;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
