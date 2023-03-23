package cn.xa87.data.mapper;

import cn.xa87.model.StagePrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StagePriceMapper extends BaseMapper<StagePrice> {

    @Select("select * from t_stage_price where status = 1 and time_type = #{type} order by create_time asc limit 1")
    List<StagePrice> findStagePriceByStatusList(@Param("type") String type);


    @Select("select * from t_stage_price where status = 1 and time_type = '1d' and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time)")
    List<StagePrice> findStagePriceByWeekStatusList();

}
