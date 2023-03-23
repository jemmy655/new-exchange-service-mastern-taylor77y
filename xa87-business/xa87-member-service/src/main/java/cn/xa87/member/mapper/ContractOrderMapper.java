package cn.xa87.member.mapper;

import cn.xa87.model.ContractOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author ZYQ
 * @date 2020/2/7 14:19
 */
public interface ContractOrderMapper extends BaseMapper<ContractOrder> {
    /**
     * 手续费总和
     *
     * @param memberIdList memberIdList
     * @param currency     币种
     * @return
     */
    BigDecimal selectHandlingFeeTotal(@Param("list") List<String> memberIdList,
                                      @Param("currency") String currency);
}
