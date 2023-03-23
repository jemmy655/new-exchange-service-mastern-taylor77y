package cn.xa87.job.mapper;

import cn.xa87.model.KlineData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface KlineDataMapper extends BaseMapper<KlineData> {

    @Select("SELECT * from t_kline_data where status = 1 and token_cur = #{tokenCur} and create_time >= now() order by create_time asc limit 1")
    List<KlineData> findKlineDataList(String tokenCur);

    @Select("select count(0) from t_kline_data where token_cur = #{tokenCur} and TO_DAYS(create_time) - TO_DAYS(NOW()) = 1  ")
    int getKlineDateCount(String tokenCur);

    @Select("SELECT * from t_kline_data where status = 1 and token_cur = #{tokenCur} and create_time >= now() order by create_time asc limit 1")
    KlineData findKlineData(String tokenCur);
}
