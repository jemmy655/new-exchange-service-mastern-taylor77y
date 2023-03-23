package cn.xa87.job.service.impl;

import cn.xa87.job.mapper.*;
import cn.xa87.job.service.UserBlancehanlerService;
import cn.xa87.model.UserBlanceCollect;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserBlancehanlerServiceImpl implements UserBlancehanlerService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    PairsMapper pairsMapper;
    @Autowired
    WalletPoolMapper walletPoolMapper;
    @Autowired
    UserBlanceCollectMapper userBlanceCollectMapper;
    @Autowired
    ExtractCoinMapper extractCoinMapper;

    @Override
    public void start() {
        List<String> currencys = pairsMapper.getCurrencys();
        List<String> ids = memberMapper.getIds();
        currencys.add("USDT");
        for (String currency : currencys) {
            for (String userId : ids) {
                //总充值  总提币
                BigDecimal sumChong = sumChong(userId, currency);
                BigDecimal sumTi = sumTi(userId, currency);
                LambdaQueryWrapper<UserBlanceCollect> userBlanceCollectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userBlanceCollectLambdaQueryWrapper.eq(UserBlanceCollect::getMember, userId).eq(UserBlanceCollect::getCurrency, currency);
                UserBlanceCollect userBlanceCollect = userBlanceCollectMapper.selectOne(userBlanceCollectLambdaQueryWrapper);
                if (userBlanceCollect == null) {
                    UserBlanceCollect userBlanceCollect1 = new UserBlanceCollect();
                    userBlanceCollect1.setCurrency(currency);
                    userBlanceCollect1.setMember(userId);
                    userBlanceCollect1.setSumChong(sumChong);
                    userBlanceCollect1.setSumTi(sumTi);
                    userBlanceCollect1.setUpdateTime(new Date());
                    userBlanceCollectMapper.insert(userBlanceCollect1);
                } else {
                    userBlanceCollect.setSumChong(sumChong);
                    userBlanceCollect.setSumTi(sumTi);
                    userBlanceCollect.setUpdateTime(new Date());
                    userBlanceCollectMapper.updateById(userBlanceCollect);
                }
            }
        }
    }


    public BigDecimal sumChong(String member, String currency) {
        return walletPoolMapper.getSumDeposit(member, currency.toLowerCase());
    }

    public BigDecimal sumTi(String member, String currency) {
        return walletPoolMapper.getSumWithdraw(member, currency.toLowerCase());

    }
}
