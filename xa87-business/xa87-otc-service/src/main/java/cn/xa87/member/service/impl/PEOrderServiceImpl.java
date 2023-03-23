package cn.xa87.member.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.member.mapper.*;
import cn.xa87.member.service.PEOrderService;
import cn.xa87.model.*;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class PEOrderServiceImpl extends ServiceImpl<PEOrderMapper, PEOrder> implements PEOrderService {

    @Autowired
    private PEOrderMapper peOrderMapper;

    @Autowired
    private PEProjectMapper peProjectMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private Xa87RedisRepository redisRepository;



    @Override
    public Object order(String projectId, String member, BigDecimal num) {
        if (Strings.isBlank(member)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(projectId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }

        PEProject peProject = peProjectMapper.selectById(projectId);

        String  coinName = peProject.getCoinName();

        String status = peProject.getStatus();
        if (status.equalsIgnoreCase("ING")) {
            BigDecimal minNum = peProject.getMinNum();
            BigDecimal maxNum = peProject.getMaxNum();
            BigDecimal sumMax = peProject.getSumNum();
            BigDecimal nowNum = peProject.getNowNum();
            if (num.compareTo(minNum) < 0 || num.compareTo(maxNum) > 0){
                throw new BusinessException("输入正确的数量");
            }
            if (num.compareTo(minNum) >= 0 & num.compareTo(maxNum) < 0) {
                BigDecimal add = nowNum.add(num);
                if (add.compareTo(sumMax) <= 0) {
                    String projectName = peProject.getProjectName();
                    BigDecimal coinPice = peProject.getCoinPice();
                    //需要的USDT数量
                    BigDecimal multiply = coinPice.multiply(num);

                    QueryWrapper<Balance> queryWrapper = new QueryWrapper<Balance>();
                    queryWrapper.eq("user_id", member).eq("currency", "USDT");
                    Balance usdTbalance = balanceMapper.selectOne(queryWrapper);
                    BigDecimal usdtBalance = usdTbalance.getAssetsBalance();


                    QueryWrapper<Balance> queryWrapper1 = new QueryWrapper<Balance>();
                    queryWrapper1.eq("user_id", member).eq("currency", coinName.toUpperCase());
                    Balance pEBalance = balanceMapper.selectOne(queryWrapper1);
                    if(null == pEBalance){
                        throw new BusinessException(coinName.toUpperCase()+"账户不存在");
                    }
                    BigDecimal peBalance = pEBalance.getAssetsBlockedBalance();

                    if (usdtBalance.compareTo(multiply) >= 0) {
                        //扣除USDT
                        BigDecimal subtract = usdtBalance.subtract(multiply);
                        usdTbalance.setAssetsBalance(subtract);
                        balanceMapper.updateById(usdTbalance);

                        //增加私募币
                        peBalance = peBalance.add(num);
                        pEBalance.setAssetsBlockedBalance(peBalance);
                        balanceMapper.updateById(pEBalance);

                        PEOrder peOrder = new PEOrder();

                        peOrder.setId(UuidUtils.generateUuid());
                        peOrder.setMember(member);
                        peOrder.setPeAmount(multiply);
                        peOrder.setPeNum(num);
                        peOrder.setPeId(projectId);
                        peOrder.setPePrice(coinPice);
                        peOrder.setPeProjectName(projectName);
                        peOrder.setCreateTime(new Date());
                        peOrder.setUpdateTime(new Date());
                        peOrderMapper.insert(peOrder);

                    } else {
                        throw new BusinessException("充提账户USDT资产不足");
                    }
                }
            } else {
                throw new BusinessException("项目额度已售罄");
            }
        } else {
            throw new BusinessException("项目未进行");
        }
        return true;
    }
}
