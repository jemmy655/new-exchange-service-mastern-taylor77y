package cn.xa87.member.mapper;

import cn.xa87.model.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysConfigMapper extends BaseMapper<SysConfig> {

    public static final String ONLY_ONE_DEPOSIT_ADDRESS = "ONLY_ONE_DEPOSIT_ADDRESS";

    public String selectByParamKey(String paramKey);

}