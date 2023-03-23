package cn.xa87.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AjaxResultEnum implements BaseEnum {

    OK(200, "成功", "sucesso"),
    PARAMETER_IS_EMPTY(500, "参数为空!", "parameter is empty!"),
    FREQUENT_OPERATIONS(500, "操作频繁!", "Frequent operations!"),
    ILLEGAL_OPERATION(500, "非法操作！", "illegal operation"),
    USER_DOES_NOT_EXIST(500, "用户不存在", "User does not exist"),
    VERIFICATION_CODE_IS_EMPTY(500, "验证码为空", "Verification code is empty"),
    VERIFICATION_CODE_HAS_EXPIRED(500, "验证码已失效", "Verification code has expired"),
    VERIFICATION_CODE_ERROR(500, "验证码错误", "Verification code error"),
    LOGIN_ACCOUNT_WRONG(500, "登录账号有误", "Login account is wrong"),
    INCORRECT_LOGIN_PASSWORD(500, "登录密码有误", "Incorrect login password"),
    ACCOUNT_EXCEPTION(500, "账号异常，请联系管理员", "The account is abnormal, please contact the administrator"),
    WRONG_PASSWORD(500, "密码有误", "wrong password"),
    WRONG_ACCOUNT(500, "账号有误", "wrong account"),
    THIS_PHONE_REGISTERED(500, "此电话已注册", "This phone is registered"),
    EMAIL_FORMAT_INCORRECT(500, "邮箱格式有误", "Email format is incorrect"),
    THIS_EMAIL_ALREADY_REGISTERED(500, "此邮箱已注册", "This email is already registered"),

    THIS_ACCOUNT_ALREADY_REGISTERED(500, "此账号已注册", "This account is already registered"),
    ACCOUNT_INITIALIZATION_FAILED(500, "账户初始化失败，请稍后再试！", "Account initialization failed, please try again later!"),
    NICKNAME_CAN_ONLY_BE_SET_ONCE(500, "昵称只可设置一次", "Nickname can only be set once"),
    PLEASE_AUTHENTICATE_FIRST(500, "请先进行身份认证", "Please authenticate first"),
    STORE_NAME_ALLOWED_EMPTY(500, "店铺名称不允许为空", "Store name is not allowed to be empty"),
    EMAIL_NOT_ALLOWED_EMPTY(500, "邮箱不允许为空", "Email is not allowed to be empty"),
    ADVANCED_AUTHENTICATION_NOT_COMPLETED(500, "高级身份认证未完成", "Advanced authentication not completed"),
    INSUFFICIENT_BALANCE(500, "余额不足", "Insufficient balance"),
    THIS_ID_IS_ALREADY_REGISTERED(500, "该身份证已注册", "This ID is already registered"),
    NAME_DOES_NOT_MATCH_ID_NUMBER(500, "姓名与身份证号码不一致", "Name does not match ID number"),
    INVITATION_CODE_ERROR(500, "邀请码错误", "Invitation code error"),
    PLEASE_SET_A_PAYMENT_PASSWORD(500, "请设置支付密码", "Please set a payment password"),
    WRONG_PAYMENT_PASSWORD(500, "支付密码错误", "wrong payment password"),
    USER_INFORMATION_IS_EMPTY(500, "用户不允许为空", "User information is empty"),
    LESS_THAN_THE_MINIMUM_BET_AMOUNT(500, "小于最低下注金额", "Less than the minimum bet amount"),
    INSUFFICIENT_MARGIN_BALANCE(500, "保证金余额不足", "Insufficient margin balance"),
    TRADING_PAIR_IS_NOT_ALLOWED_EMPTY(500, "交易对不允许为空", "Trading pair is not allowed to be empty"),
    THIS_ORDER_IS_ALREADY_OPEN_AND_IRREVOCABLE(500, "此订单已持仓,不可撤销", "This order is already open and irrevocable"),
    ORDER_DOES_NOT_EXIST(500, "订单不存在", "order does not exist"),
    TRADING_PAIR_ERROR(500, "交易对错误", "Trading pair error"),
    LEVERAGE_MUST_BE_GREATER(500, "杠杆手数必须大于0", "Leverage must be greater than 0"),
    EMAIL_VERIFICATION_CODE_FAILED_TO_SEND(500, "邮箱验证码发送失败", "Email verification code failed to send"),
    TRADING_PAIR_DOES_NOT_EXIST(500, "交易对不存在", "trading pair does not exist"),
    THE_PURCHASE_QUANTITY_IS_TOO_SMALL(500, "购买数量过少,请充值", "The purchase quantity is too small, please recharge"),
    THE_QUANTITY_SOLD_IS_TOO_SMALL(500, "出售数量过少,请充值", "The quantity sold is too small, please recharge"),
    QUANTITY_CANNOT_BE_LESS(500, "数量不能小于等于0", "Quantity cannot be less than or equal to 0"),
    PRICE_CANNOT_BE_LESS_THAN(500, "价格不能小于等于0", "Price cannot be less than or equal to 0"),
    ORDER_HAS_BEEN_FILLED_OR_DOES_NOT_EXIST(500, "订单已成交或不存在", "The order has been filled or does not exist"),
    CAN_ONLY_CANCEL_UNPAID_ORDERS(500, "只能取消未付款状态订单", "Can only cancel unpaid orders"),
    FAILED_TO_UPDATE_ASSETS(500, "更新资产失败", "Failed to update assets"),
    PLEASE_CLOSE_THE_CONTRACT_ORDER_FIRST(500, "请先平仓合约订单", "Please close the contract order first"),
    INTERNAL_ACCOUNTS_ARE_NOT_ALLOWED(500, "内部账户不允许提币", "Internal accounts are not allowed to withdraw coins"),
    WITHDRAWAL_CHANNEL_IS_TEMPORARILY_CLOSED(500, "该币种提币通道暂时关闭", "The currency withdrawal channel is temporarily closed"),
    WITHDRAWAL_AMOUNT_IS_LESS_THAN_THE_MINIMUM_AMOUNT(500, "提币数量小于最小额度", "Withdrawal amount is less than the minimum amount"),
    NOT_ENOUGH_COINS(500, "币种数量不足", "Not enough coins"),
    TRANSFER_MONEY_TO_MYSELF(500, "不能给自己转账", "Can't transfer money to myself"),
    THERE_ARE_CURRENTLY_NO_SUITABLE_ORDERS(500, "当前没有合适的订单", "There are currently no suitable orders"),
    MEMBER_ID_IS_EMPTY(500, "会员id为空", "member id is empty"),
    THE_PRICE_FLOATING_RATIO_IS_EMPTY(500, "价格浮动比例为空", "The price floating ratio is empty"),
    CURRENCY_IS_EMPTY(500, "币种为空", "Currency is empty"),
    AUTOREPLY_STATUS_IS_EMPTY(500, "自动回复状态为空", "Autoreply status is empty"),
    QUANTITY_IS_EMPTY(500, "数量为空", "Quantity is empty"),
    ORDER_APPEALED(500, "订单已申诉", "Order appealed"),
    ORDER_QUERY_FAILED_OR_ORDER_APPEAL_STATUS_CHANGED(500, "订单查询失败或订单申诉状态已改变", "Order query failed or order appeal status changed"),
    QUERY_FAILED(500, "查询失败", "Query failed"),
    ORDER_QUERY_FAILED(500, "订单查询失败", "Order query failed"),
    DIRECTION_IS_EMPTY(500, "方向为空", "direction is empty"),
    MINIMUM_TRANSACTION_PRICE_IS_EMPTY(500, "最小成交价格为空", "Minimum transaction price is empty"),
    PLEASE_PASS_REAL_NAME_AUTHENTICATION_FIRST(500, "请先通过实名认证", "Please pass real-name authentication first"),
    PLEASE_APPLY_TO_BECOME_A_MERCHANT_FIRST(500, "请先申请成为商家", "Please apply to become a merchant first"),
    PAYMENT_METHOD_IS_EMPTY(500, "收款方式为空", "Payment method is empty"),
    PLEASE_ADD_A_PAYMENT_METHOD_BEFORE_SELECTING(500, "请先添加收款方式，再进行选择", "Please add a payment method before selecting"),
    ASSET_UPDATE_FAILED_PLEASE_CONTACT_CUSTOMER_SERVICE(500, "资产更新失败，请联系客服", "Asset update failed, please contact customer service"),
    DEDUCTION_OF_ASSETS_FAILED(500, "扣除资产失败", "Deduction of assets failed"),
    PAGE_NUMBER_IS_EMPTY(500, "页码为空", "Page number is empty"),
    NUMBER_OF_ENTRIES_IS_EMPTY(500, "条数为空", "number of entries is empty"),
    BUY_AND_SELL_DIRECTION_IS_EMPTY(500, "买卖方向为空", "Buy and sell direction is empty"),
    ORDER_ID_IS_EMPTY(500, "订单id为空", "order id is empty"),
    TOO_MANY_CANCELLATIONS_TODAY(500, "今日取消订单次数过多，暂不允许下单", "Too many cancellations today, orders are temporarily not allowed"),
    DO_NOT_BUY_YOUR_OWN_ORDER(500, "请勿购买自己的订单", "Do not buy your own order"),
    THE_ORDER_STATUS_HAS_CHANGED(500, "订单状态已改变，请刷新后重新下单", "The order status has changed, please refresh and place the order again"),
    EXCEED_THE_ORDER_LIMIT(500, "超出订单限额范围，请刷新后重新下单", "Exceed the order limit, please refresh and re-order"),
    WRONG_PAYMENT_METHOD(500, "付款方式有误", "wrong payment method"),
    ABNORMAL_ORDER_STATUS(500, "订单状态异常", "abnormal order status"),
    EXTREME_PRICE_IS_EMPTY(500, "极端价格为空", "Extreme price is empty"),
    MINIMUM_TURNOVER_IS_EMPTY(500, "最小成交额为空", "Minimum turnover is empty"),
    THERE_ARE_PENDING_TRANSACTIONS(500, "存在未完成的交易,请先完成", "There are pending transactions, please complete first"),
    PLEASE_UPDATE_YOUR_ORDER_STATUS_AND_TRY_AGAIN(500, "请更新订单状态后重试", "Please update your order status and try again"),
    UNFULFILLED_ORDERS_CANNOT_BE_CANCELLED(500, "有未完成订单，无法撤单", "Unfulfilled orders cannot be cancelled"),
    OTC_ACCOUNT_HAS_BEEN_FROZEN(500, "OTC账户已冻结，请联系管理员", "OTC account has been frozen, please contact the administrator"),
    PLEASE_BIND_YOUR_PHONE_FIRST(500, "请先绑定手机", "Please bind your phone first"),
    PLEASE_SELECT_THE_PAYMENT_METHOD(500, "请选择支付方式", "Please select the payment method"),
    PLEASE_SELECT_AN_ORDER_TYPE(500, "请选择订单类型", "Please select an order type"),
    PLEASE_SELECT_THE_CORRECT_PAYMENT_METHOD(500, "请选择正确的收款方式", "Please select the correct payment method"),
    T_M_U_O_P_C_C_S_F_P(500, "未处理订单过多，请联系客服处理！", "Too many unprocessed orders, please contact customer service for processing!"),
    W_A_C_P_T_L(500, "提现拥挤，请稍后尝试！", "Withdrawals are crowded, please try later!"),
    B_P_T_L(500, "操作繁忙，请稍后尝试！", "Busy, please try later!"),
    T_L_O_I_U_R_P_T_L(500, "上一笔订单正在审核中，请稍后尝试！", "The last order is under review, please try later!"),
    T_L_O_H_N_B_S(500, "上一笔订单还未结算，请稍后尝试！", "The last order has not been settled, please try later!"),
    FILE_EMPTY(200, "文件为空", "o arquivo está vazio");


    private Integer code;
    private String remark;
    private String message;

    @Override
    public Integer code() {
        return getCode();
    }

    @Override
    public String message() {
        return getMessage();
    }

    @Override
    public void message(String message) {
        this.message = message;
    }
}
