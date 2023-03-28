package cn.xa87.data.service.impl;

import cn.xa87.data.mapper.PledgeOrderDetailMapper;
import cn.xa87.data.mapper.PledgeOrderMapper;
import cn.xa87.data.service.PledgeOrderDetailService;
import cn.xa87.model.PledgeOrder;
import cn.xa87.model.PledgeOrderDetail;
import cn.xa87.vo.PledgeOrderDetailVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PledgeOrderDetailServiceImpl extends ServiceImpl<PledgeOrderDetailMapper, PledgeOrderDetail> implements PledgeOrderDetailService {
    @Autowired
    private PledgeOrderMapper pledgeOrderMapper;
    @Override
    public Boolean setPledgeOrderDetail(PledgeOrderDetailVo pledgeOrderDetailVo) {
        PledgeOrder p= pledgeOrderMapper.selectById(pledgeOrderDetailVo.getOrderId());

        PledgeOrderDetail detail=new PledgeOrderDetail();
        detail.setOrderId(pledgeOrderDetailVo.getOrderId());
        detail.setCreationTime(new Date());
        detail.setMoney(pledgeOrderDetailVo.getMoney());
        detail.setPledgeType("币");
        detail.setPledgePrice(pledgeOrderDetailVo.getPledgePrice());
        detail.setPledgeName(pledgeOrderDetailVo.getPledgeName());
        detail.setType(pledgeOrderDetailVo.getType());
        this.baseMapper.insert(detail);
        return true;
    }
}
