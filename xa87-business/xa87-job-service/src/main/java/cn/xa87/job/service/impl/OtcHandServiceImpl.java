package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.OtcConstant;
import cn.xa87.job.mapper.BalanceMapper;
import cn.xa87.job.mapper.FbOrderMapper;
import cn.xa87.job.mapper.OtcPriceOrderMapper;
import cn.xa87.job.mapper.OtcTimeMapper;
import cn.xa87.job.service.OtchandService;
import cn.xa87.model.OtcOrder;
import cn.xa87.model.OtcPriceOrder;
import cn.xa87.model.OtcTimeConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OtcHandServiceImpl implements OtchandService {
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Autowired
    private OtcPriceOrderMapper otcPriceOrderMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private OtcTimeMapper otcTimeMapper;
    @Autowired
    private FbOrderMapper fbOrderMapper;

    @Override
    public void startHandler() {
        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcPriceOrderLambdaQueryWrapper.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString()).ne(OtcPriceOrder::getAppealStatus, OtcConstant.APPEAL_STATUS.UNDERWAY.toString()).ne(OtcPriceOrder::getAppealStatus, OtcConstant.APPEAL_STATUS.BACK.toString());
        List<OtcPriceOrder> otcPriceOrders = otcPriceOrderMapper.selectList(otcPriceOrderLambdaQueryWrapper);
        Long min = 30L;
        if (otcPriceOrders.size() > 0) {
            LambdaQueryWrapper<OtcTimeConfig> otcTimeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
            otcTimeConfigLambdaQueryWrapper.eq(OtcTimeConfig::getType, "orderTime");
            OtcTimeConfig otcTimeConfig = otcTimeMapper.selectOne(otcTimeConfigLambdaQueryWrapper);
            if (otcTimeConfig != null) {
                min = Long.valueOf(otcTimeConfig.getMin());
            }
            for (OtcPriceOrder otcPriceOrder : otcPriceOrders) {
                long time = otcPriceOrder.getCreateTime().getTime();
                long endTime = time + min * 1000L * 60L;
                long newTime = new Date().getTime();
                if (endTime <= newTime) {
                    RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
                    boolean lock_coin = redisDistributedLock.lock(CacheConstants.OTC_ORDER_KEY + otcPriceOrder.getId(), 5000, 50, 100);
                    if (lock_coin) {
                        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
                            backOrder(otcPriceOrder.getId(), otcPriceOrder.getUserId());
                        } else {
                            backOrder(otcPriceOrder.getId(), otcPriceOrder.getStoreId());
                        }
                    }
                    redisDistributedLock.releaseLock(CacheConstants.OTC_ORDER_KEY + otcPriceOrder.getId());
                }
            }
        }
    }


    public Boolean backOrder(String priceOrderId, String memberId) {

        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectById(priceOrderId);
        if (!otcPriceOrder.getStatus().equals(OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString())) {
            throw new BusinessException(AjaxResultEnum.CAN_ONLY_CANCEL_UNPAID_ORDERS.getMessage());
        }
        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
            //如果用户是买，那么只允许用户进行这个操作
            if (!otcPriceOrder.getUserId().equals(memberId)) {
                throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
            }
        } else {
            //用户是卖，只允许商家进行这个操作
            if (!otcPriceOrder.getStoreId().equals(memberId)) {
                throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
            }
            //取消用户冻结
            Map<String, Object> map = new HashMap<>();
            map.put("userId", otcPriceOrder.getUserId());
            map.put("balance", otcPriceOrder.getNum());
            map.put("currency", otcPriceOrder.getCurrency());
            int i = subDJBalance(map);
            if (i < 1) {
                throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
            }
        }
        //修改订单状态
        otcPriceOrder.setStatus(OtcConstant.PRICE_ORDER_STATUS.CALLOFF.toString());
        //修改总单数量
        OtcOrder otcOrder = fbOrderMapper.selectById(otcPriceOrder.getOrderId());
        otcOrder.setFreeze(otcOrder.getFreeze().subtract(otcPriceOrder.getNum()));
        otcOrder.setNum(otcOrder.getNum().add(otcPriceOrder.getNum()));
        otcPriceOrderMapper.updateById(otcPriceOrder);
        fbOrderMapper.updateById(otcOrder);
        boolean exists = redisRepository.exists(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        Long time = now.getTimeInMillis() - System.currentTimeMillis();
        Long stime = time / 1000L;
        if (exists) {
            String s = redisRepository.get(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId);

            Integer i = Integer.valueOf(s) + 1;
            redisRepository.setExpire(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId, i.toString(), stime);
        } else {
            redisRepository.setExpire(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId, "1", stime);
        }
        return true;
    }

    public int subDJBalance(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean exists = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (exists) {
            balanceMapper.subDJBalance(map);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
            return 1;
        } else {
            subDJBalance(map);
        }
        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        return 0;
    }
}
