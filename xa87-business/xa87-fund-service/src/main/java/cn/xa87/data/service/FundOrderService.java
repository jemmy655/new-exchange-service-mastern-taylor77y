package cn.xa87.data.service;

import cn.xa87.model.FundOrder;
import cn.xa87.vo.OrderCheck;
import cn.xa87.vo.FundOrderVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface FundOrderService extends IService<FundOrder> {
    List<Map<String,Object>> getFundOrderByUserId(String userId, String status);

    Boolean setFundOrderPurchase(FundOrderVo fundOrderVo);

    Boolean setFundOrderRedeem(FundOrderVo fundOrderVo);

    /**
     * 正常赎回 到期赎回  后端调用
     * @param fundOrderVo
     * @return
     */
    Boolean setRedeem(FundOrderVo fundOrderVo);

    OrderCheck getCheckFundOrder(String productId);

    Map<String,Object> getCountFundOrderByUserId(String userId);
}
