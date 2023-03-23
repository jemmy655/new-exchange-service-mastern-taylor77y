package cn.xa87.rabbit.mapper;

import cn.xa87.model.Pairs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PairsMapper extends BaseMapper<Pairs> {

    List<String> getCurrencys();

    List<String> getMainCurs();
}
