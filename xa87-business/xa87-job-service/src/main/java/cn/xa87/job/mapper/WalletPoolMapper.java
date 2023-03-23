package cn.xa87.job.mapper;

import cn.xa87.model.WalletPool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface WalletPoolMapper extends BaseMapper<WalletPool> {


    BigDecimal getSumDeposit(@Param("member") String member, @Param("currency") String currency);


    BigDecimal getSumWithdraw(@Param("member") String member, @Param("currency") String currency);
}