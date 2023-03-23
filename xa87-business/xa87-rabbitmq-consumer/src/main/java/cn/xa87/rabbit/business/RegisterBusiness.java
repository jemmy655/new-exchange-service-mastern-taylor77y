package cn.xa87.rabbit.business;

import cn.xa87.model.Balance;
import cn.xa87.rabbit.mapper.PairsMapper;
import cn.xa87.rabbit.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RegisterBusiness {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private PairsMapper pairsMapper;

    @Async
    public void execute(String msg) {
        try {
            Set<String> set = new HashSet<String>();
            set.addAll(pairsMapper.getCurrencys());
            set.addAll(pairsMapper.getMainCurs());
            List<Balance> balances = new ArrayList<Balance>();
            for (String str : set) {
                Balance balance = new Balance();
                balance.setUserId(msg);
                balance.setCurrency(str);
                balances.add(balance);
            }
            balanceService.saveBatch(balances);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
