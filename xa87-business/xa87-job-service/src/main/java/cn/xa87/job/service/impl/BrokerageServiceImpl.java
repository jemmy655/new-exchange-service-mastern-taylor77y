package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.DateUtil;
import cn.xa87.common.utils.HideDataUtil;
import cn.xa87.job.mapper.BrokerageRecordMapper;
import cn.xa87.job.mapper.DictionariesMapper;
import cn.xa87.job.mapper.MemberMapper;
import cn.xa87.job.service.BrokerageService;
import cn.xa87.model.BrokerageRecord;
import cn.xa87.model.Dictionaries;
import cn.xa87.model.Member;
import cn.xa87.vo.BrokerageVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.xa87.common.constants.DictionariesConstant.TRANSACTION_BROKERAGE_NUMBER;

@Slf4j
@Service
public class BrokerageServiceImpl implements BrokerageService {

    @Autowired
    private BrokerageRecordMapper brokerageRecordMapper;
    @Autowired
    private DictionariesMapper dictionariesMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    /**
     * 计算回扣,并赋值,统计前一天
     */
    @Override
    @Transactional
    public void calculateBrokerage() {
        //查询字典表分佣上限值
        QueryWrapper<Dictionaries> dictionariesWrapper = new QueryWrapper<>();
        dictionariesWrapper.eq("ukey", TRANSACTION_BROKERAGE_NUMBER);
        BigDecimal brokerageTop = new BigDecimal(dictionariesMapper.selectOne(dictionariesWrapper).getUvalue());
        //查询表BrokerageRecord. 前一天数据
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        long lastDayBegin = DateUtil.lastDayBegin(date);
        long lastDayEnd = DateUtil.lastDayEnd(date);
        QueryWrapper<BrokerageRecord> brokerageRecordQueryWrapper = new QueryWrapper<>();
        brokerageRecordQueryWrapper
                .eq("status", (byte) 0)
                .ge("create_time", format.format(lastDayBegin))
                .le("create_time", format.format(lastDayEnd));
        List<BrokerageRecord> recordList = brokerageRecordMapper.selectList(brokerageRecordQueryWrapper);
        for (BrokerageRecord br : recordList) {
            String brokerageUserId = br.getBrokerageUserId();
            BigDecimal brokerage = memberMapper.selectById(brokerageUserId).getBrokerage();
            //判断当前返佣小于返佣上限值
            if (brokerage.compareTo(brokerageTop) < 0) {
                //要增加的返佣数量
                BigDecimal brokerageNumber;
                if (brokerage.add(br.getNumber()).compareTo(brokerageTop) < 1) {
                    brokerageNumber = br.getNumber();
                } else {
                    brokerageNumber = brokerageTop.subtract(brokerage);
                }
                br.setBrokerageNumber(brokerageNumber);
                br.setStatus((byte) 1);
                //修改t_member、t_balance、t_brokerage_record
                brokerageRecordMapper.updateById(br);
                //会有前一天未分配的记录数据，因为已超出分拥上限，如果有需要可以删除
            }
        }
    }

    /**
     * 统计月度排行榜
     */
    @Override
    public void statisticsBrokerageTopMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        long lastMonthBegin = DateUtil.lastMonthBegin(date);
        long lastMonthEnd = DateUtil.lastMonthEnd(date);
        QueryWrapper<BrokerageRecord> brokerageRecordWrapper = new QueryWrapper<>();
        brokerageRecordWrapper
                .eq("status", (byte) 1)
                .ge("create_time", format.format(lastMonthBegin))
                .le("create_time", format.format(lastMonthEnd))
                .groupBy("brokerage_user_id")
                .select("brokerage_user_id", "sum(brokerage_number) as brokerage_number");
        List<BrokerageRecord> recordList = brokerageRecordMapper.selectList(brokerageRecordWrapper);
        List<BrokerageVo> brokerageVoList = new ArrayList<>();
        for (BrokerageRecord record : recordList) {
            BrokerageVo vo = new BrokerageVo();
            vo.setNumber(record.getBrokerageNumber());
            Member member = memberMapper.selectById(record.getBrokerageUserId());
            String param;
            if (member.getPhone() != null) {
                param = HideDataUtil.mobile(member.getPhone());
            } else {
                param = HideDataUtil.email(member.getMail());
            }
            vo.setBrokeragePhone(param);
            brokerageVoList.add(vo);
        }
        //存储月度榜单,覆盖之前的榜单
        String key = CacheConstants.MONTH_BROKERAGE_KEY;
        if (brokerageVoList.size() > 0) {
            redisRepository.set(key, JSONObject.toJSONString(brokerageVoList));
        } else {
            redisRepository.del(key);
        }
    }
}
