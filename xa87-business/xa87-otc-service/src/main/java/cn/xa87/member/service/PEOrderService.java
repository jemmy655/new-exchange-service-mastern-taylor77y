package cn.xa87.member.service;

import cn.xa87.model.PEOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.HashMap;

public interface PEOrderService extends IService<PEOrder> {
    Object order(String projectId, String member, BigDecimal num);
}
