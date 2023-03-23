package cn.xa87.job.mapper;

import cn.xa87.model.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface BalanceMapper extends BaseMapper<Balance> {
    /**
     * 减少冻结余额
     *
     * @return
     */
    int subDJBalance(Map<String, Object> map);

}