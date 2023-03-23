package cn.xa87.data.service.impl;

import cn.xa87.data.mapper.WarehouseMapper;
import cn.xa87.data.service.WarehouseService;
import cn.xa87.model.ContractMul;
import cn.xa87.model.Warehouse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {


    @Override
    public Integer UpdateControlPrice(String Id, BigDecimal controlPrice) {
        if (StringUtil.isNullOrEmpty(Id)){
            return 0;
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", Id);
        updateWrapper.set("control_price", controlPrice);
        this.update(null, updateWrapper);
        return 1;
    }
}
