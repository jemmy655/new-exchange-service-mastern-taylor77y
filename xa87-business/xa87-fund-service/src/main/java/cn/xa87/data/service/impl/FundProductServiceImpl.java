package cn.xa87.data.service.impl;

import cn.xa87.data.mapper.FundProductMapper;
import cn.xa87.data.service.FundProductService;
import cn.xa87.model.FundProduct;
import cn.xa87.model.PerpetualContractOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class FundProductServiceImpl  extends ServiceImpl<FundProductMapper, FundProduct> implements FundProductService {
    @Override
    public List<FundProduct> getFundProduct() {
        QueryWrapper<FundProduct> wrapperMain = new QueryWrapper<FundProduct>();
        wrapperMain.eq("enabled", 1);
        wrapperMain.orderByDesc("create_time");
        List<FundProduct> products=this.baseMapper.selectList(wrapperMain);
        return products;
    }
}
