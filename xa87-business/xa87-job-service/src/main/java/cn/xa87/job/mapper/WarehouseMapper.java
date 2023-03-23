package cn.xa87.job.mapper;

import cn.xa87.model.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarehouseMapper extends BaseMapper<Warehouse> {

    int updateOrder1(@Param("price") String peice, @Param("pairsName") String pairsName);

    int updateOrder2(@Param("price") String peice, @Param("pairsName") String pairsName);

    List<String> getDisMember();
}