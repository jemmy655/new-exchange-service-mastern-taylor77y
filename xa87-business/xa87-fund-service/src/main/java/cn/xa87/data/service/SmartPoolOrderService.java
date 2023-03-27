package cn.xa87.data.service;

import cn.xa87.model.SmartPoolOrder;
import cn.xa87.model.SmartPoolProduct;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.SmartPoolOrderVo;

import java.util.List;
import java.util.Map;

public interface SmartPoolOrderService {


    List<Map<String,Object>> getSmartPoolOrderByUserId(String userId, String status);

    OrderCheck getCheckSmartPoolOrder(String productId);

    Map<String,Object> getCountSmartPoolOrderByUserId(String userId);

    Boolean setSmartPoolOrderPurchase(SmartPoolOrderVo fundOrderVo);

    Boolean setSmartPoolOrderRedeem(SmartPoolOrderVo fundOrderVo);
}
