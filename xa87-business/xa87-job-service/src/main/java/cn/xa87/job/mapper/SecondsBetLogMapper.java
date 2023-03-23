package cn.xa87.job.mapper;

import cn.xa87.model.SecondsBetLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SecondsBetLogMapper extends BaseMapper<SecondsBetLog> {

    @Select("SELECT * from t_seconds_bet_log where settle_status = 2")
    List<SecondsBetLog> findSecondsList();
}
