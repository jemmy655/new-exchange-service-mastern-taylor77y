package cn.xa87.member.mapper;

import cn.xa87.model.Pairs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface PairsMapper extends BaseMapper<Pairs> {
    public Pairs selectPairByCoin(String token);
}