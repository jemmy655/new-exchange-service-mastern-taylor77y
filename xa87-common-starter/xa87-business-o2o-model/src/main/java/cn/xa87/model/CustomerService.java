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
@TableName("t_customer_service")
public class CustomerService extends Model<CustomerService> {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客服链接
     */
    private String contactLink;
    /**
     * 状态：1 启用，2停用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 类型: 1平台客服 ，2充值客服
     */
    private Integer type;
}
