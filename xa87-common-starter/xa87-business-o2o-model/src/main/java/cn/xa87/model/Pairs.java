package cn.xa87.model;

import cn.xa87.constant.CoinConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pairs")
public class Pairs extends Model<Pairs> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 交易对名称
     */
    private String pairsName;
    /**
     * 主币
     */
    private String mainCur;
    /**
     * 代币
     */
    private String tokenCur;
    /**
     * 状态
     */
    private CoinConstant.Coin_State state;
    /**
     * 类型
     */
    private CoinConstant.Coin_Type type;
    /**
     * 项目方合约地址
     */
    private String contract;
    /**
     * logo
     */
    private String image;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 序号
     */
    private int sort;
    /**
     * 当前价
     */
    private BigDecimal price;
    /**
     * 当前人民币价格
     */
    private BigDecimal chPrice;
    /**
     * 成交量
     */
    private BigDecimal volume;
    /**
     * 开始价
     */
    private BigDecimal open;
    /**
     * 最低价
     */
    private BigDecimal lowPrice;
    /**
     * 最高价
     */
    private BigDecimal higPrice;
    /**
     * 涨跌幅
     */
    private BigDecimal updown;
    /**
     * 最大成交数量
     */
    private BigDecimal tradeMax;
    /**
     * 最小成交数量
     */
    private BigDecimal tradeMin;
    /**
     * 手续费
     */
    private BigDecimal tradeRate;
    /**
     * 是否作为主板显示
     */
    private boolean isTop;

    private Integer point;

    private String isDw;
    /**
     * ETH BTC
     */
    private String mainFrom;

    private BigDecimal withdrawMin;

    private BigDecimal withdrawFee;

    /**
     * 币类型：1 通用币，2 自发币
     */
    private Integer pairsType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
