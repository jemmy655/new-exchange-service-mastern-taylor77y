package cn.xa87.member.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.member.service.BrokerageService;
import cn.xa87.vo.BrokerageVo;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class BrokerageServiceImpl implements BrokerageService {

    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public Response selectMonth() {
        String key = CacheConstants.MONTH_BROKERAGE_KEY;
        String monthBrokerage = redisRepository.get(key);
        log.info("月度榜单. [{}]", monthBrokerage);
        if (monthBrokerage == null) {
            return Response.success(new ArrayList<>());
        } else {
            List<BrokerageVo> results = JSONArray.parseArray(monthBrokerage, BrokerageVo.class);
            results.sort(Comparator.comparing(BrokerageVo::getNumber).reversed());
            if (results.size() > 20) {
                List<BrokerageVo> voList = results.subList(0, 20);
                return Response.success(voList);
            } else {
                return Response.success(results);
            }
        }
    }
}
