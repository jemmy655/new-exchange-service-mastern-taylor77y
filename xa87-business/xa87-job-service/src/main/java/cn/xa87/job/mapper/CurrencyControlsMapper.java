package cn.xa87.job.mapper;

import cn.xa87.model.CurrencyControls;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CurrencyControlsMapper extends BaseMapper<CurrencyControls> {

    @Select("select * from t_currency_controls where status = 1")
    List<CurrencyControls> findCurrencyControlsList();
}
