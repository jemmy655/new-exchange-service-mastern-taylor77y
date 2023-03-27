package cn.xa87.data.service;

import cn.xa87.model.SmartPoolOrder;
import cn.xa87.model.SmartPoolProduct;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.SmartPoolOrderVo;

import java.util.List;

public interface SmartPoolProductService {
    List<SmartPoolProduct> getSmartPoolProduct();
}
