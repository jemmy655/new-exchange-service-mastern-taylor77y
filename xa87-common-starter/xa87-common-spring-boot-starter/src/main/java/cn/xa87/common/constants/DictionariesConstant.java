package cn.xa87.common.constants;

import lombok.NoArgsConstructor;

/**
 * 字典表常量类
 *
 * @author ZYQ
 * @date 2020/2/3 19:56
 */
@NoArgsConstructor
public class DictionariesConstant {

    /**
     * 交易回扣数量上限
     */
    public static final String TRANSACTION_BROKERAGE_NUMBER = "transaction_brokerage_number";
    /**
     * 交易手续费回扣比例(%)
     */
    public static final String TRANSACTION_BROKERAGE_RATIO = "transaction_brokerage_ratio";
    /**
     * USDT/omni提取最小限额
     */
    public static final String USDT_EXTRACT_MIN_LIMIT = "usdt_extract_min_limit";
    /**
     * ETH提取最小限额
     */
    public static final String ETH_EXTRACT_MIN_LIMIT = "eth_extract_min_limit";
    /**
     * USDT/ERC20提现每笔手续费
     */
    public static final String USDT_EXTRACT_HANDLING = "usdt_extract_handling";
    /**
     * ETH提现每笔手续费
     */
    public static final String ETH_EXTRACT_HANDLING = "eth_extract_handling";

}
