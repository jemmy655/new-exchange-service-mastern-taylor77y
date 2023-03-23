package cn.xa87.common.constants;

public interface CacheConstants {
    /**
     * 缓存分隔符
     */
    String SPLIT = ":";
    /**
     * XA87缓存公共前缀
     */
    String PREFIX = "xa87" + SPLIT;
    /**
     * 路由信息Redis保存的key
     */
    String ROUTE_KEY = PREFIX + "routes";
    /**
     * 验证码
     */
    String DEFAULT_CODE_KEY = PREFIX + "sms" + SPLIT;
    /**
     * K线缓存key
     */
    String KLINE_KEY = PREFIX + "kline" + SPLIT;
    /**
     * 币种涨跌幅、最新价、最高价、最低价、成交量等JSON
     */
    String PRICE_HIG_LOW_KEY = PREFIX + "price" + SPLIT;
    /**
     * 用户登录 token 缓存
     */
    String MEMBER_TOKEN_KEY = PREFIX + "token" + SPLIT;
    /**
     * 币种交易对价格
     */
    String PRICE_MAIN_CUR_KEY = PREFIX + "maincur" + SPLIT;
    /**
     * 普通委托单
     */
    String TOKEN_ORDER_CUSTOM_KEY = PREFIX + "custom" + SPLIT;
    /**
     * 撮合单
     */
    String TOKEN_ORDER_MATCH_KEY = PREFIX + "match" + SPLIT;
    /**
     * 撮合实时成交单
     */
    String TOKEN_ORDER_END_KEY = PREFIX + "match_end" + SPLIT;
    /**
     * 用户资产锁
     */
    String MEMBER_BALANCE_COIN_KEY = PREFIX + "balance" + SPLIT;
    /**
     * 用户挂单交易对锁
     */
    String MEMBER_ENTRUST_PAIRS_KEY = PREFIX + "entrust_pairs" + SPLIT;
    /**
     * 用户合约交易对锁
     */
    String MEMBER_PAIRS_KEY = PREFIX + "pairs" + SPLIT;
    /**
     * 风险度
     */
    String MEMBER_RISK_KEY = PREFIX + "risk" + SPLIT;
    /**
     * 风险度短信90%
     */
    String MEMBER_RISK_SMS_KEY_90 = PREFIX + "risk-sms-90" + SPLIT;
    /**
     * 风险度短信100%
     */
    String MEMBER_RISK_SMS_KEY_100 = PREFIX + "risk-sms-100" + SPLIT;
    /**
     * 指数价格
     */
    String INDEX_PRICE_KEY = PREFIX + "indexprice" + SPLIT;
    /**
     * 币币撮合单
     */
    String ENTRUST_ORDER_MATCH_KEY = PREFIX + "entrust" + SPLIT;
    /**
     * 币币撮合实时成交单
     */
    String ENTRUST_ORDER_END_KEY = PREFIX + "entrust_end" + SPLIT;
    /**
     * 分润榜单
     */
    String BROKERAGE_KEY = PREFIX + "brokerage" + SPLIT;
    /**
     * 分润月度榜单
     */
    String MONTH_BROKERAGE_KEY = BROKERAGE_KEY + "month";
    /**
     * 盈亏资产记录
     */
    String MEMBER_PROFIT_KEY = PREFIX + "profit" + SPLIT;
    /**
     * otc订单
     */
    String OTC_ORDER_KEY = PREFIX + "otc" + SPLIT;
    /**
     * otc订单回调
     */
    String OTC_ORDER_BACK_KEY = PREFIX + "otcBack" + SPLIT;
    /**
     * otc订单取消次数
     */
    String OTC_ORDER_BACK_COUNT_KEY = PREFIX + "otcCount" + SPLIT;
    /**
     * webHook回调ID
     */
    String WEBHOOK_ID_KEY = PREFIX + "webhook" + SPLIT;
}
