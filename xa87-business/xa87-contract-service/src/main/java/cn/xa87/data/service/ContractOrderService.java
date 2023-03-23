package cn.xa87.data.service;

import cn.xa87.common.web.Response;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.ContractOrder;
import cn.xa87.model.MemberCurrencyConfig;
import cn.xa87.model.SecondsBetLog;
import cn.xa87.model.SecondsConfig;
import cn.xa87.vo.ContractDeliveryVo;
import cn.xa87.vo.ContractOrderNewVO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ContractOrderService extends IService<ContractOrder> {

    boolean setContractOrder(ContractOrder contractOrder);

    ContractOrderNewVO getContractOrder(String id);

    boolean setOrdTriggerMatch(String id, BigDecimal price, TokenOrderConstant.Match_Type matchType);

    boolean setOrderMatch(String id, BigDecimal hands);

    boolean setAllContractMatch(String member, String pairsName);

    boolean closeContractOrder(String orderId);

    JSONObject getWarehouses(String member, String pairsName);

    Response getHistoryOrders(String member, String pairsName, TokenOrderConstant.Order_State orderState, Integer pageNum, Integer pageSize);

    Map<String, Object> getRisk(String member);

    List<ContractOrder> getEntrustOrder(String member, String pairsName);

    BigDecimal getMaxHands(String member, String pairsName, BigDecimal price, BigDecimal level);

    Object secondContractBetting(ContractDeliveryVo vo);

    List<SecondsConfig> getSecondsContract();

    List<MemberCurrencyConfig> getCurrencyConfiguration();

    Response getSecondContractRecord(String member,Integer type, Integer pageNum, Integer pageSize);

    SecondsBetLog getSecondContract(Integer secondId);
}