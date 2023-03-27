package cn.xa87.data.service.impl;

import cn.xa87.data.mapper.FundOrderMapper;
import cn.xa87.data.mapper.SmartPoolProductMapper;
import cn.xa87.data.service.SmartPoolProductService;
import cn.xa87.model.FundOrder;
import cn.xa87.model.FundProduct;
import cn.xa87.model.SmartPoolOrder;
import cn.xa87.model.SmartPoolProduct;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.SmartPoolOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmartPoolProductServiceImpl  extends ServiceImpl<SmartPoolProductMapper, SmartPoolProduct>  implements SmartPoolProductService {
    @Override
    public List<SmartPoolProduct> getSmartPoolProduct() {
        QueryWrapper<SmartPoolProduct> wrapperMain = new QueryWrapper<SmartPoolProduct>();
        wrapperMain.eq("enabled", 1);
        wrapperMain.orderByDesc("create_time");
        List<SmartPoolProduct> products=this.baseMapper.selectList(wrapperMain);
        return products;
    }
}
