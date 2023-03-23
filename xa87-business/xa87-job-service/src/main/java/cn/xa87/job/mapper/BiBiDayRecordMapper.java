package cn.xa87.job.mapper;


import cn.xa87.model.BiBiDayRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BiBiDayRecordMapper extends BaseMapper<BiBiDayRecord> {

    BigDecimal getSum(@Param("pairName") String pairName, @Param("type") String type);

    BigDecimal getFbSum(@Param("currency") String currency, @Param("type") String type);

    @Select("SELECT currency from t_otc_currenct_config")
    List<String> getFbCurrency();

    Integer saveFbSum(Map<String, Object> map);
}