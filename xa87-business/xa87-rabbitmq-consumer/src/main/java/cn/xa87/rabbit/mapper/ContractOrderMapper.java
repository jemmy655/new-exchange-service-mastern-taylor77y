package cn.xa87.rabbit.mapper;

import cn.xa87.model.ContractOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface ContractOrderMapper extends BaseMapper<ContractOrder> {
    int updateOrderState(@Param("member") String member);

}