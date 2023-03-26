package cn.xa87.data.service;

import cn.xa87.model.ContractMul;
import cn.xa87.model.FundOrder;
import cn.xa87.vo.FundOrderVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FundOrderService extends IService<FundOrder> {
    List<FundOrder> getFundOrderByUserId(String userId, String status);

    Boolean setFundOrderPurchase(FundOrderVo fundOrderVo);

    Boolean setFundOrderRedeem(FundOrderVo fundOrderVo);

    /**
     * 正常赎回 到期赎回  后端调用
     * @param fundOrderVo
     * @return
     */
    Boolean setRedeem(FundOrderVo fundOrderVo);
}
