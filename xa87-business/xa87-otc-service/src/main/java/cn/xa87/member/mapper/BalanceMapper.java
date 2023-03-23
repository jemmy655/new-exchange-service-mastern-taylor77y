package cn.xa87.member.mapper;

import cn.xa87.model.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface BalanceMapper extends BaseMapper<Balance> {
    /**
     * 增加冻结余额
     *
     * @return
     */
    int addDJBalance(Map<String, Object> map);

    /**
     * 增加余额
     *
     * @return
     */
    int addBalance(Map<String, Object> map);

    /**
     * 减少余额
     *
     * @return
     */
    int subBalance(Map<String, Object> map);

    /**
     * 减少冻结余额
     *
     * @return
     */
    int subDJBalance(Map<String, Object> map);


}