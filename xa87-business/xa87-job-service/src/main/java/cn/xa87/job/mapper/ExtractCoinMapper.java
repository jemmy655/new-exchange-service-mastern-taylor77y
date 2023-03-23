package cn.xa87.job.mapper;

import cn.xa87.model.ExtractCoin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface ExtractCoinMapper extends BaseMapper<ExtractCoin> {
    BigDecimal getSumTi(@Param("member") String member, @Param("currency") String currency, @Param("currency1") String currency1);
}