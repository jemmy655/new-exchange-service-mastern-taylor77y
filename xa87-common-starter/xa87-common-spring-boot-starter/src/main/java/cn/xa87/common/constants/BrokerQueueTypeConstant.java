package cn.xa87.common.constants;

import lombok.NoArgsConstructor;

/**
 * 经纪人后台管理队列类型字段
 *
 * @author ZYQ
 * @date 2020/2/11 23:12
 */
@NoArgsConstructor
public final class BrokerQueueTypeConstant {
    /**
     * 队列区分类型字段
     */
    public static final String BROKER_QUEUE_TYPE = "TYPE";
    /**
     * 用户注册
     */
    public static final String USER_REGISTER = "USER_REGISTER";
    /**
     * 用户增加账号(手机号/邮箱)
     */
    public static final String USER_ADD_NUMBER = "USER_ADD_NUMBER";
    /**
     * 划转
     */
    public static final String TRANSFER = "TRANSFER";
    /**
     * 充值
     */
    public static final String RECHARGE = "RECHARGE";
    /**
     * 订单
     */
    public static final String CONTRACT_ORDER = "CONTRACT_ORDER";
}
