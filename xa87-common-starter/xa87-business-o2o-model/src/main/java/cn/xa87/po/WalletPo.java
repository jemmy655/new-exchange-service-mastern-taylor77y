package cn.xa87.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询余额响应类
 */
@Data
public class WalletPo implements Serializable {
    /**
     * t_member -> id
     */
    private String memberId;
    /**
     * eth币
     */
    private String eth;
    /**
     * btc币
     */
    private String btc;
    /**
     * usdt币
     */
    private String usdt;
    /**
     * 钱包密码
     */
    private String passWord;
}
