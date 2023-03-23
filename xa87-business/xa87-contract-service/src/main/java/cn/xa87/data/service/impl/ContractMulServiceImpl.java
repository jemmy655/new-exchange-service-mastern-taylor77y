package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.data.mapper.ContractMulMapper;
import cn.xa87.data.mapper.LeverMapper;
import cn.xa87.data.service.ContractMulService;
import cn.xa87.model.ContractMul;
import cn.xa87.model.Lever;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractMulServiceImpl extends ServiceImpl<ContractMulMapper, ContractMul> implements ContractMulService {
    @Autowired
    private ContractMulMapper contractMulMapper;

    @Autowired
    private LeverMapper leverMapper;

    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public List<ContractMul> getContractMul(String pairsName) {
        QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
        if (Strings.isNotBlank(pairsName)) {
            wrapper.eq("pairs_name", pairsName);
        }
        List<ContractMul> contractCfgs = contractMulMapper.selectList(wrapper);
        return contractCfgs;
    }

    @Override
    public List<Lever> getLevers(String pairsName) {
        QueryWrapper<Lever> wrapper = new QueryWrapper<Lever>();
        wrapper.eq("pairs_name", pairsName);
        wrapper.orderByAsc("lever");
        List<Lever> levers = leverMapper.selectList(wrapper);
        return levers;
    }

    @Override
    public String getIndexPrice(String pairsName) {
        return redisRepository.get(CacheConstants.INDEX_PRICE_KEY + pairsName);
    }

}
