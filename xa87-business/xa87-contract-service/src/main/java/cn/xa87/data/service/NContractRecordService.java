package cn.xa87.data.service;

import cn.xa87.model.NContractRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface NContractRecordService extends IService<NContractRecord> {
    void contract(NContractRecord contract);
}
