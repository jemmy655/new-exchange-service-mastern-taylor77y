package cn.xa87.job.mapper;

import cn.xa87.model.Pairs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface PairsMapper extends BaseMapper<Pairs> {

    List<String> getMainCursByCur(@Param("mainCur") String mainCur);

    List<String> getCurrencys();

    List<String> getMainCurs();

    @Select("SELECT pairs_name from t_pairs ")
    List<String> getPairName();

    @Select("SELECT price from t_pairs where token_cur=#{ckType}")
    BigDecimal getCurrentPrice(String ckType);
}
