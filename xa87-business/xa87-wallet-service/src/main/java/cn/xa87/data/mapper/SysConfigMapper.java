package cn.xa87.data.mapper;

import cn.xa87.model.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysConfigMapper extends BaseMapper<SysConfig> {

    public String selectByParamKey(String paramKey);

}