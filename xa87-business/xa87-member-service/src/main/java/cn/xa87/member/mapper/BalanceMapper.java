package cn.xa87.member.mapper;

import cn.xa87.model.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceMapper extends BaseMapper<Balance> {
    /**
     * 增加余额
     *
     * @param coinName coinName
     * @param addNum   addNum
     * @param memberId memberId
     * @return
     */
    int addBalance(@Param("coinName") String coinName,
                   @Param("addNum") BigDecimal addNum,
                   @Param("memberId") String memberId);

    /**
     * 减少余额
     *
     * @param currency currency
     * @param lessNum  lessNum
     * @param memberId memberId
     * @return
     */
    int lessBalance(@Param("currency") String currency,
                    @Param("lessNum") BigDecimal lessNum,
                    @Param("memberId") String memberId);

    List<Balance> getBalanceList(@Param("userId") String member, @Param("currency") String currency, @Param("coinType") String coinType);

    List<Balance> getBalanceMainList(@Param("userId") String member, @Param("currency") String currency);
}