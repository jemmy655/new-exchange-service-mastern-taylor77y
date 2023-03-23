package cn.xa87.data.mapper;

import cn.xa87.model.KlineData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface KlineDataMapper extends BaseMapper<KlineData> {

    @Select("SELECT * from t_kline_data where status = 1 and token_cur = #{tokenCur} and create_time >= now() order by create_time asc limit 1")
    List<KlineData> findKlineDataList(String tokenCur);

    @Select("select count(0) from t_kline_data where token_cur = #{tokenCur} and DATE_SUB(CURDATE(), INTERVAL 1 DAY) <= date(create_time)")
    int getKlineDateCount(String tokenCur);

    @Select("SELECT * from t_kline_data where status = 1 and token_cur = #{tokenCur} and create_time >= now() order by create_time asc limit 1")
    KlineData findKlineData(String tokenCur);

    @Select("select create_time from t_kline_data where token_cur = #{tokenCur} order by create_time desc limit 1")
    LocalDateTime selectLastTime(String tokenCur);

    @Delete("delete from  t_kline_data where token_cur=#{token}")
    int clearAll(String token);
}
