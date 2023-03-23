package cn.xa87.model;

import cn.xa87.constant.MemConstant;
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
@TableName("t_member")
public class Member extends Model<Member> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 身份类型
     */
    private String type;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 支付密码
     */
    @TableField("pay_password")
    private String payPassword;
    /**
     * 注册类型
     */
    @TableField(exist = false)
    private MemConstant.Register_Type regType;
    /**
     * 佣金
     */
    private BigDecimal brokerage;
    /**
     * 二维码
     */
    private String quickMark;
    /**
     * 邀请码
     */
    @TableField("wel_code")
    private String welCode;
    /**
     * 邀请人ID
     */
    @TableField("wel_member")
    private String welMember;
    /**
     * 姓名
     */
    private String uname;
    /**
     * 身份证号
     */
    private String cardNo;
    /**
     * 生日
     */
    private String birth;
    /**
     * 性别
     */
    private String sex;
    /**
     * 地址
     */
    private String address;
    /**
     * 经纪人
     */
    private Byte broker;
    /**
     * 经纪人等级一
     */
    private String brokerGradeOne;
    /**
     * 经纪人等级二
     */
    private String brokerGradeTwo;
    /**
     * 经纪人等级三
     */
    private String brokerGradeThree;
    /**
     * 邀请节点路径
     */
    private String nodePath;
    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 微信二维码
     */
    @TableField("pay_wechat")
    private String payWechat;
    /**
     * 微信用户名
     */
    @TableField("wechat_name")
    private String wechatName;
    /**
     * 支付宝二维码
     */
    @TableField("pay_aliay")
    private String payAliay;
    /**
     * 支付宝用户名
     */
    @TableField("aliay_name")
    private String aliayName;
    /**
     * 身份证正面地址
     */
    @TableField("positive_link")
    private String positiveLink;
    /**
     * 身份证反面地址
     */
    @TableField("side_link")
    private String sideLink;
    /**
     * 身份证审核状态
     */
    @TableField("card_state")
    private MemConstant.Card_Sate cardState;
    /**
     * 商家昵称
     */
    @TableField("store_name")
    private String storeName;
    /**
     * 银行卡姓名
     */
    @TableField("bank_user_name")
    private String bankUserName;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 开户行
     */
    @TableField("bank_address")
    private String bankAddress;
    /**
     * 银行卡号
     */
    @TableField("bank_card")
    private String bankCard;
    /**
     * 商家申请装唉
     */
    @TableField("store_state")
    private MemConstant.Card_Sate storeState;
    /**
     * 法币状态
     */
    @TableField("fb_status")
    private String fbStatus;
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
    @TableField("salt")
    private String salt;
    @TableField("area_code")
    private String areaCode;
    @TableField("hand_link")
    private String handLink;
    @TableField("user_status")
    private String userStatus;

    @TableField("is_control")
    private Integer isControl;
    @TableField("is_bok")
    private Integer isBok;

    // 商家联系方式
    @TableField("connect_link")
    private String connectLink;

    // 系统ID
    @TableField("sys_id")
    private String sysId;

    // 会员类型 1 正式用户
    @TableField("use_type")
    private String useType;

    // 最后登入IP
    @TableField("last_login_ip")
    private String lastLoginIp;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
