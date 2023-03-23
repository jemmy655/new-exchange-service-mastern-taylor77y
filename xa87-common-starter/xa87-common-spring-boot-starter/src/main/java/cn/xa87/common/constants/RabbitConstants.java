package cn.xa87.common.constants;

public interface RabbitConstants {
    /**
     * 注册
     */
    public static final String REGISTER_PUT = "register_put";//注册

    public static final String REGISTER_QUEUE_PUT = "register_queue_put";

    public static final String REGISTER_ROUTINGKEY_PUT = "register_routingKey_put";
    /**
     * 合约撮合
     */
    public static final String MATCH_PUT = "match_put";//撮合

    public static final String MATCH_QUEUE_PUT = "match_queue_put";

    public static final String MATCH_ROUTINGKEY_PUT = "match_routingKey_put";
    /**
     * 合约资产更改
     */
    public static final String BALANCE_PUT = "balance_put";//资产

    public static final String BALANCE_QUEUE_PUT = "balance_queue_put";

    public static final String BALANCE_ROUTINGKEY_PUT = "balance_routingKey_put";
    /**
     * 修改合约组合订单
     */
    public static final String MATCH_TO_CONTRACT_PUT = "match_to_contract_put";//订单

    public static final String MATCH_TO_CONTRACT_QUEUE_PUT = "match_to_contract_queue_put";

    public static final String MATCH_TO_CONTRACT_ROUTINGKEY_PUT = "match_to_contract_routingKey_put";
    /**
     * 币币主流币撮合
     */
    public static final String ENTRUST_MAIN_MATCH_PUT = "entrust_main_match_put";

    public static final String ENTRUST_MAIN_MATCH_QUEUE_PUT = "entrust_main_match_queue_put";

    public static final String ENTRUST_MAIN_MATCH_ROUTINGKEY_PUT = "entrust_main_match_routingKey_put";
    /**
     * 币币项目方撮合
     */
    public static final String ENTRUST_PROJECT_MATCH_PUT = "entrust_project_match_put";

    public static final String ENTRUST_PROJECT_MATCH_QUEUE_PUT = "entrust_project_match_queue_put";

    public static final String ENTRUST_PROJECT_MATCH_ROUTINGKEY_PUT = "entrust_project_match_routingKey_put";
    /**
     * 币币资产更改
     */
    public static final String ENTRUST_BALANCE_PUT = "entrust_balance_put";//资产

    public static final String ENTRUST_BALANCE_QUEUE_PUT = "entrust_balance_queue_put";

    public static final String ENTRUST_BALANCE_ROUTINGKEY_PUT = "entrust_balance_routingKey_put";
    /**
     * 币币市价资产更改
     */
    public static final String ENTRUST_MARKET_BALANCE_PUT = "entrust_market_balance_put";//资产

    public static final String ENTRUST_MARKET_BALANCE_QUEUE_PUT = "entrust_market_balance_queue_put";

    public static final String ENTRUST_MARKET_BALANCE_ROUTINGKEY_PUT = "entrust_market_balance_routingKey_put";
    /**
     * 止盈止损更改
     */
    public static final String TOKEN_PRICE_PUT = "token_price_put";//资产

    public static final String TOKEN_PRICE_QUEUE_PUT = "token_price_queue_put";

    public static final String TOKEN_PRICE_ROUTINGKEY_PUT = "token_price_routingKey_put";
    /**
     * 盈亏更改
     */
    public static final String PROFITLOSS_PRICE_PUT = "profitLoss_price_put";//资产

    public static final String PROFITLOSS_PRICE_QUEUE_PUT = "profitLoss_price_queue_put";

    public static final String PROFITLOSS_PRICE_ROUTINGKEY_PUT = "profitLoss_price_routingKey_put";
    /**
     * 修改限价到持仓
     */
    public static final String CONTRACT_PRICE_PUT = "contract_price_put";//资产

    public static final String CONTRACT_PRICE_QUEUE_PUT = "contract_price_queue_put";

    public static final String CONTRACT_PRICE_ROUTINGKEY_PUT = "contract_price_routingKey_put";
    /**
     * 修改委托订单进行成交
     */
    public static final String ENTRUST_PRICE_PUT = "entrust_price_put";//资产

    public static final String ENTRUST_PRICE_QUEUE_PUT = "entrust_price_queue_put";

    public static final String ENTRUST_PRICE_ROUTINGKEY_PUT = "entrust_price_routingKey_put";
    /**
     * 消费记录(计算返佣使用)
     */
    String CONSUME_CURRENCY_PUT = "consume_currency_put";

    String CONSUME_CURRENCY_QUEUE_PUT = "consume_currency_queue_put";

    String CONSUME_CURRENCY_ROUTINGKEY_PUT = "consume_currency_routingKey_put";

    /**
     * 经纪人管理后台 (php高露要用)
     */
    String BROKER_MANAGE_PUT = "broker_manage_put";

    String BROKER_MANAGE_QUEUE_PUT = "broker_manage_queue_put";

    String BROKER_MANAGE_ROUTINGKEY_PUT = "broker_manage_routingKey_put";
    /**
     * 钱包余额回调
     */
    public static final String WALLET_PUT = "wallet_put";//注册

    public static final String WALLET_QUEUE_PUT = "wallet_queue_put";

    public static final String WALLET_ROUTINGKEY_PUT = "wallet_routingKey_put";
    /**
     * 钱包转账回调
     */
    public static final String PAYMENTS_PUT = "payments_put";//注册

    public static final String PAYMENTS_QUEUE_PUT = "payments_queue_put";

    public static final String PAYMENT_ROUTINGKEY_PUT = "payments_routingKey_put";

    /**
     * 火币K线
     */
    String HUOBI_MANAGE_PUT = "huobi_manage_put";

    String HUOBI_MANAGE_QUEUE_PUT = "huobi_manage_queue_put";

    String HUOBI_MANAGE_ROUTINGKEY_PUT = "huobi_manage_routingKey_put";

    /**
     * 增加删除新币种
     */
    public static final String COINPAIRS_PUT = "coinpairs_put";//资产

    public static final String COINPAIRS_QUEUE_PUT = "coinpairs_queue_put";

    public static final String COINPAIRS_ROUTINGKEY_PUT = "coinpairs_routingkey_put";
}
