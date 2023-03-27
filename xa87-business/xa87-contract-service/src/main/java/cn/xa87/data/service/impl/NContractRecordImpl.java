package cn.xa87.data.service.impl;

import cn.xa87.data.mapper.BalanceMapper;
import cn.xa87.data.mapper.NContractRecordMapper;
import cn.xa87.data.service.NContractRecordService;
import cn.xa87.model.NContractRecord;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NContractRecordImpl extends ServiceImpl<NContractRecordMapper,NContractRecord> implements NContractRecordService {
    @Resource
    BalanceMapper balanceMapper;

    @Override
    public void contract(NContractRecord contract) {

    }
}
