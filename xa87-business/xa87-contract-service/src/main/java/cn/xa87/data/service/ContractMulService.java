package cn.xa87.data.service;

import cn.xa87.model.ContractMul;
import cn.xa87.model.Lever;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ContractMulService extends IService<ContractMul> {

    List<ContractMul> getContractMul(String pairsName);

    List<Lever> getLevers(String pairsName);

    String getIndexPrice(String pairsName);

}