package cn.xa87.data.service;

import cn.xa87.common.web.Response;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.PerpetualContractOrder;
import cn.xa87.vo.PerpetualContractOrderVO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PerpetualContractOrderService extends IService<PerpetualContractOrder> {

    boolean setContractOrder(PerpetualContractOrderVO perpetualContractOrder);

    Map<Object,Object> getContractOrder(String id);

    boolean setOrderMatch(String id);

    boolean setAllContractMatch(String member, String pairsName);

    List<Object> getWarehouses(String member, String pairsName, String price);

    Response getHistoryOrders(String member, String pairsName, TokenOrderConstant.Order_State orderState, Integer pageNum, Integer pageSize);

    boolean setContractOrderSell(PerpetualContractOrderVO perpetualContractOrder);
}