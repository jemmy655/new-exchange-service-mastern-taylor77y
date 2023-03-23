package cn.xa87.data.service;

import cn.xa87.model.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface WarehouseService extends IService<Warehouse> {

    Integer UpdateControlPrice(String Id, BigDecimal controlPrice);
}