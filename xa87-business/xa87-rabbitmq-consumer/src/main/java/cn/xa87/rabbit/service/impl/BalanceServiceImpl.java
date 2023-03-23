package cn.xa87.rabbit.service.impl;

import cn.xa87.model.Balance;
import cn.xa87.rabbit.mapper.BalanceMapper;
import cn.xa87.rabbit.service.BalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {
}