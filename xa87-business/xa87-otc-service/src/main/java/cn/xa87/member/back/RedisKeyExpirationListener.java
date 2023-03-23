//package cn.xa87.member.back;
//
//
//import cn.xa87.common.constants.CacheConstants;
//import cn.xa87.common.redis.lock.RedisDistributedLock;
//import cn.xa87.common.redis.template.Xa87RedisRepository;
//import cn.xa87.constant.OtcConstant;
//import cn.xa87.member.mapper.OtcPriceOrderMapper;
//import cn.xa87.member.mapper.OtcTimeMapper;
//import cn.xa87.member.service.FbOrderService;
//import cn.xa87.model.OtcPriceOrder;
//import cn.xa87.model.OtcTimeConfig;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
//
//@Slf4j
//@Component
//@EnableScheduling
//public class RedisKeyExpirationListener {
//    @Autowired
//    private Xa87RedisRepository redisRepository;
//
//    @Autowired
//    private OtcPriceOrderMapper otcPriceOrderMapper;
//    @Autowired
//    private FbOrderService fbOrderService;
//    @Autowired
//    private OtcTimeMapper otcTimeMapper;
//
//    @Scheduled(fixedRate = 20000L)
//    public void updata() {
//        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        otcPriceOrderLambdaQueryWrapper.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString()).ne(OtcPriceOrder::getAppealStatus, OtcConstant.APPEAL_STATUS.UNDERWAY.toString()).ne(OtcPriceOrder::getAppealStatus, OtcConstant.APPEAL_STATUS.BACK.toString());
//        List<OtcPriceOrder> otcPriceOrders = otcPriceOrderMapper.selectList(otcPriceOrderLambdaQueryWrapper);
//        Long min = 30L;
//        if (otcPriceOrders.size() > 0) {
//            LambdaQueryWrapper<OtcTimeConfig> otcTimeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            otcTimeConfigLambdaQueryWrapper.eq(OtcTimeConfig::getType, "orderTime");
//            OtcTimeConfig otcTimeConfig = otcTimeMapper.selectOne(otcTimeConfigLambdaQueryWrapper);
//            if (otcTimeConfig != null) {
//                min = Long.valueOf(otcTimeConfig.getMin());
//            }
//            for (OtcPriceOrder otcPriceOrder : otcPriceOrders) {
//                long time = otcPriceOrder.getCreateTime().getTime();
//                long endTime = time + min * 1000L * 60L;
//                long newTime = new Date().getTime();
//                if (endTime <= newTime) {
//                    RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
//                    boolean lock_coin = redisDistributedLock.lock(CacheConstants.OTC_ORDER_KEY + otcPriceOrder.getId(),
//                            5000, 50, 100);
//                    if (lock_coin) {
//                        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
//                            fbOrderService.backOrder(otcPriceOrder.getId(), otcPriceOrder.getUserId());
//                        } else {
//                            fbOrderService.backOrder(otcPriceOrder.getId(), otcPriceOrder.getStoreId());
//                        }
//                    }
//                    redisDistributedLock.releaseLock(CacheConstants.OTC_ORDER_KEY + otcPriceOrder.getId());
//                }
//            }
//        }
//    }
//}