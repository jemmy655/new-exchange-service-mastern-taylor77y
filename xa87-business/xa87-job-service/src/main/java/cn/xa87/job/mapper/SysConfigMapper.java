package cn.xa87.job.mapper;

import cn.xa87.model.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysConfigMapper extends BaseMapper<SysConfig> {

    public String selectByParamKey(String paramKey);

}