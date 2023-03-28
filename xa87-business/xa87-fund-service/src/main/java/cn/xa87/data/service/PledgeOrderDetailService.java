package cn.xa87.data.service;

import cn.xa87.model.FundOrder;
import cn.xa87.model.PledgeOrderDetail;
import cn.xa87.vo.PledgeOrderDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PledgeOrderDetailService extends IService<PledgeOrderDetail> {
    Boolean setPledgeOrderDetail(PledgeOrderDetailVo pledgeOrderDetailVo);
}
