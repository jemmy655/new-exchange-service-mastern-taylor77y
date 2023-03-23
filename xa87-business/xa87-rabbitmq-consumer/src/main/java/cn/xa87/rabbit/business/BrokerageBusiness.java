package cn.xa87.rabbit.business;

import cn.xa87.model.BrokerageRecord;
import cn.xa87.model.Dictionaries;
import cn.xa87.model.Member;
import cn.xa87.po.BrokerageRecordPo;
import cn.xa87.rabbit.mapper.BrokerageRecordMapper;
import cn.xa87.rabbit.mapper.DictionariesMapper;
import cn.xa87.rabbit.mapper.MemberMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static cn.xa87.common.constants.DictionariesConstant.TRANSACTION_BROKERAGE_NUMBER;
import static cn.xa87.common.constants.DictionariesConstant.TRANSACTION_BROKERAGE_RATIO;

/**
 * 插入返佣记录表
 */
@Slf4j
@Component
public class BrokerageBusiness {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BrokerageRecordMapper brokerageRecordMapper;
    @Autowired
    private DictionariesMapper dictionariesMapper;

    public void execute(String msg) {
        try {
            BrokerageRecordPo brokerageRecordPo = JSONObject.parseObject(msg, BrokerageRecordPo.class);
            setWarehouse(brokerageRecordPo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWarehouse(BrokerageRecordPo brokerageRecordPo) {
        Member member = memberMapper.selectById(brokerageRecordPo.getConsumeUserId());
        String brokerageUserId = member.getWelMember();
        String phone = member.getPhone();
        if (brokerageUserId != null) {
            // 父id现在的分佣数量
            BigDecimal userBrokerage = memberMapper.selectById(brokerageUserId).getBrokerage();
            // 查询表dictionaries. 分佣上限/分佣比例
            BigDecimal brokerageTop = new BigDecimal(0);
            BigDecimal brokerageRatio = new BigDecimal(0);
            List<String> keyList = new ArrayList<>();
            keyList.add(TRANSACTION_BROKERAGE_NUMBER);
            keyList.add(TRANSACTION_BROKERAGE_RATIO);
            List<Dictionaries> dictionariesList = dictionariesMapper
                    .selectList(new QueryWrapper<Dictionaries>().lambda().in(Dictionaries::getUkey, keyList));
            for (Dictionaries dictionaries : dictionariesList) {
                String key = dictionaries.getUkey();
                String value = dictionaries.getUvalue();
                if (TRANSACTION_BROKERAGE_NUMBER.equals(key)) {
                    brokerageTop = new BigDecimal(value);
                } else {
                    brokerageRatio = new BigDecimal(value).multiply(new BigDecimal("0.01"));
                }
            }
            if (userBrokerage.compareTo(brokerageTop) < 0) {
                BrokerageRecord brokerageRecord = new BrokerageRecord();
                BeanUtils.copyProperties(brokerageRecordPo, brokerageRecord);
                brokerageRecord.setBrokerageUserId(brokerageUserId);
                brokerageRecord.setBrokeragePhone(phone);
                brokerageRecord.setStatus((byte) 0);
                brokerageRecord.setNumber(brokerageRecordPo.getNumber().multiply(brokerageRatio));
                brokerageRecordMapper.insert(brokerageRecord);
            }
        }
    }
}
