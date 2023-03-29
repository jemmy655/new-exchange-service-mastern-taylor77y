package cn.xa87.data.service;

import cn.xa87.model.PledgeOrder;
import cn.xa87.vo.PledgeOrderVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PledgeOrderService extends IService<PledgeOrder> {
    /**
     * 获取用户资产列表
     * @param userId
     * @return
     */
    List<Map<String,Object>> getMemberCionName(String userId);

    /**
     * 列表
     * @param userId
     * @return
     */
    List<Map<String,Object>> getPledgeOrderList(String userId);

    /**
     * 借款
     * @param pledgeOrderVo
     * @return
     */
    Boolean setPledgeOrderBorrow(PledgeOrderVo pledgeOrderVo);

    /**
     * 还款
     * @param pledgeOrderVo
     * @return
     */
    Boolean setPledgeOrderRepayment(PledgeOrderVo pledgeOrderVo);

    /**
     *
     * @param userId 用户id
     * @param loanCycle 周期
     * @param pledge_name   质押币种
     * @param borrow_price  借款金额
     * @param pledge_price  质押金额
     * @return
     */
    PledgeOrderVo getLoanMoney(String userId, BigDecimal loanCycle, String pledgeName, BigDecimal borrowPrice, BigDecimal pledgePrice);
}
