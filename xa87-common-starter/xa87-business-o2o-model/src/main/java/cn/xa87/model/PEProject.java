package cn.xa87.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_pe_project")
public class PEProject extends Model<PEProject> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField(value = "order_index")
    private Integer orderIndex;

    @TableField(value = "project_name")
    private String projectName;

    @TableField(value = "project_url")
    private String projectUrl;

    @TableField(value = "project_img")
    private String projectImg;

    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "end_time")
    private Date endTime;

    @TableField(value = "now_num")
    private BigDecimal nowNum;

    @TableField(value = "sum_num")
    private BigDecimal sumNum;

    @TableField(value = "min_num")
    private BigDecimal minNum;

    @TableField(value = "max_num")
    private BigDecimal maxNum;

    @TableField(value = "coin_pice")
    private BigDecimal coinPice;

    @TableField(value = "coin_name")
    private String coinName;

    @TableField(value = "status")
    private String status;
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