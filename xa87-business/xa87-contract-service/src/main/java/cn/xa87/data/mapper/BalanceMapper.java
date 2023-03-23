package cn.xa87.data.mapper;

import cn.xa87.model.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

public interface BalanceMapper extends BaseMapper<Balance> {

    Map<String, BigDecimal> getSumBalance(@Param("member") String member);

    BigDecimal getSumTokenPrice(@Param("member") String member);

}