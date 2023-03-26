package cn.xa87.data.service;

import cn.xa87.model.FundOrder;
import cn.xa87.model.FundProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 */
public interface FundProductService extends IService<FundProduct> {

    List<FundProduct> getFundProduct();
}
