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
@TableName("t_robot_config")
public class RobotConfig extends Model<RobotConfig> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 交易对名称
     */
    @TableField("pair_name")
    private String pairName;
    /**
     * 是否开启K线
     */
    @TableField("open_kine")
    private String openKine;
    /**
     * 是否开启
     */
    @TableField("is_open")
    private String isOpen;
    /**
     * 是否开始盘口
     */
    @TableField("open_tape")
    private String openTape;
    /**
     * 预计价格
     */
    @TableField("will_price")
    private BigDecimal willPrice;
    /**
     * 随机起始数量
     */
    @TableField("start_num")
    private BigDecimal startNum;
    /**
     * 随机结束数量
     */
    @TableField("end_num")
    private BigDecimal endNum;
    /**
     * 随机起始价格
     */
    @TableField("start_price")
    private BigDecimal startPrice;

    /**
     * 随机结束价格
     */
    @TableField("end_price")
    private BigDecimal endPrice;
    /**
     * 预设时间
     */
    @TableField("will_time")
    private Date willTime;
    /**
     * 绑定用户
     */
    @TableField("bind_user")
    private String bindUser;

    /**
     * 预设时间
     */
    @TableField("create_time")
    private Date createTime;


    /**
     * 随机起始数量
     */
    @TableField("mstart_num")
    private BigDecimal mstartNum;
    /**
     * 随机结束数量
     */
    @TableField("mend_num")
    private BigDecimal mendNum;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getParams() {
        return pairName + "-" + startPrice.toPlainString() + "-" + endPrice.toPlainString() + "-" + startNum.toPlainString() + "-" + endNum.toPlainString() + "-" + mstartNum.toPlainString() + "-" + mendNum.toPlainString();
    }
}
