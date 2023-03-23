package cn.xa87.member.service.impl;


import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.SYSconfig;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.AliyunClient;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.MemConstant;
import cn.xa87.constant.OtcConstant;
import cn.xa87.member.mapper.*;
import cn.xa87.member.service.FbOrderService;
import cn.xa87.model.*;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FbOrderServiceImpl extends ServiceImpl<FbOrderMapper, OtcOrder> implements FbOrderService {

    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private OtcAppealTypeMapper otcAppealTypeMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private FbOrderMapper fbOrderMapper;
    @Autowired
    private OtcCurrencyConfigMapper otcCurrencyConfigMapper;
    @Autowired
    private OtcPriceOrderMapper otcPriceOrderMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private OtcTimeMapper otcTimeMapper;
    @Autowired
    private OtcOrderAppealMapper otcOrderAppealMapper;

    @Override
    public Boolean orderReleasing(String memberId, String slidingScales, String currency, String num,
                                  String minQuota, String payType, String tradeComment, String autoStatus,
                                  String direction, String extremum, String autoMsg) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(slidingScales)) {
            throw new BusinessException(AjaxResultEnum.THE_PRICE_FLOATING_RATIO_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(currency)) {
            throw new BusinessException(AjaxResultEnum.CURRENCY_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(autoStatus)) {
            throw new BusinessException(AjaxResultEnum.AUTOREPLY_STATUS_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(num)) {
            throw new BusinessException(AjaxResultEnum.QUANTITY_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(direction)) {
            throw new BusinessException(AjaxResultEnum.DIRECTION_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(minQuota)) {
            throw new BusinessException(AjaxResultEnum.MINIMUM_TRANSACTION_PRICE_IS_EMPTY.getMessage());
        }
        Member member = fatherFilter(memberId);
        if (member.getCardState() == null || !MemConstant.Card_Sate.PASS.toString().equals(member.getCardState().toString())) {
            throw new BusinessException(AjaxResultEnum.PLEASE_PASS_REAL_NAME_AUTHENTICATION_FIRST.getMessage());
        }
        if (member.getStoreState() == null || !(member.getStoreState().toString().equals(MemConstant.Card_Sate.PASS.toString()))) {
            throw new BusinessException(AjaxResultEnum.PLEASE_APPLY_TO_BECOME_A_MERCHANT_FIRST.getMessage());
        }
        if (payType == null || payType.length() == 0) {
            throw new BusinessException(AjaxResultEnum.PAYMENT_METHOD_IS_EMPTY.getMessage());
        } else {
            String[] split = payType.split("-");
            Arrays.stream(split).forEach(str -> {
                int count = 0;
//                if (OtcConstant.PAY_TYPE.ALIAY.toString().equals(str)) {
//                    Object payAliay = member.getPayAliay();
//                    if (payAliay == null || payAliay.toString().length() == 0) {
//                        throw new BusinessException("请先添加支付宝收款方式，再进行选择");
//                    }
//                    count += 1;
//                }
//                if (OtcConstant.PAY_TYPE.WECHAT.toString().equals(str)) {
//                    Object payWechat = member.getPayWechat();
//                    if (payWechat == null || payWechat.toString().length() == 0) {
//                        throw new BusinessException("请先添加微信收款方式，再进行选择");
//                    }
//                    count += 1;
//                }
                if (OtcConstant.PAY_TYPE.BANKCARD.toString().equals(str)) {
                    Object getBankCard = member.getBankCard();
                    if (getBankCard == null || getBankCard.toString().length() == 0) {
                        throw new BusinessException(AjaxResultEnum.PLEASE_ADD_A_PAYMENT_METHOD_BEFORE_SELECTING.getMessage());
                    }
                    count += 1;
                }
                if (count == 0) {
                    throw new BusinessException(AjaxResultEnum.PLEASE_SELECT_THE_CORRECT_PAYMENT_METHOD.getMessage());
                }
            });
        }
        if (Strings.isBlank(autoStatus)) {
            throw new BusinessException(AjaxResultEnum.AUTOREPLY_STATUS_IS_EMPTY.getMessage());
        }

        if (direction.equals(OtcConstant.DIRECTION.SELL.toString())) {
            LambdaQueryWrapper<Balance> balanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            balanceLambdaQueryWrapper.eq(Balance::getUserId, memberId).eq(Balance::getCurrency, currency.toUpperCase());
            Balance balance = balanceMapper.selectOne(balanceLambdaQueryWrapper);
            BigDecimal fbBalance = balance.getAssetsBalance();
            BigDecimal numBig = new BigDecimal(num);
            if (fbBalance.compareTo(numBig) < 0) {
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
            }
            //冻结资产
            Map<String, Object> map = new HashMap<>();
            map.put("userId", memberId);
            map.put("balance", numBig);
            map.put("currency", currency);
            //加冻结
            int i1 = addDJBalance(map);
            if (i1 < 1) {
                throw new BusinessException(AjaxResultEnum.ASSET_UPDATE_FAILED_PLEASE_CONTACT_CUSTOMER_SERVICE.getMessage());
            }
            //减少可用
            int i = subBalance(map);
            if (i < 1) {
                throw new BusinessException(AjaxResultEnum.DEDUCTION_OF_ASSETS_FAILED.getMessage());
            }
        }

        //发布订单
        OtcOrder otcOrder = new OtcOrder();
        otcOrder.setMember(memberId);
        otcOrder.setMemberFbName(member.getStoreName());
        otcOrder.setCurrency(currency.toUpperCase());
        otcOrder.setNum(new BigDecimal(num));
        otcOrder.setAutoStatus(autoStatus);
        otcOrder.setAutoMsg(autoMsg);
        otcOrder.setUpDownNumber(new BigDecimal(slidingScales));
        otcOrder.setMinPrice(new BigDecimal(minQuota));
        otcOrder.setExtremum(new BigDecimal(extremum));
        otcOrder.setRemarks(tradeComment);
        otcOrder.setDirection(direction);
        otcOrder.setPayType(String.join("-", payType));
        otcOrder.setStatus(OtcConstant.ORDER_STATUS.NORMAL.toString());
        otcOrder.setCreateTime(new Date());
        otcOrder.setUpdateTime(new Date());
        fbOrderMapper.insert(otcOrder);
        return true;

    }

    @Override
    public Object orderList(String page, String size, String currency, String direction,Integer mccId) {
        if (Strings.isBlank(page)) {
            throw new BusinessException(AjaxResultEnum.PAGE_NUMBER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(size)) {
            throw new BusinessException(AjaxResultEnum.NUMBER_OF_ENTRIES_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(currency)) {
            throw new BusinessException(AjaxResultEnum.CURRENCY_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(direction)) {
            throw new BusinessException(AjaxResultEnum.BUY_AND_SELL_DIRECTION_IS_EMPTY.getMessage());
        }
        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.ne(OtcOrder::getDirection, direction).eq(OtcOrder::getCurrency, currency).eq(OtcOrder::getStatus, OtcConstant.ORDER_STATUS.NORMAL);
        if(null != mccId){
            otcOrderLambdaQueryWrapper.eq(OtcOrder::getMccId, mccId);
        }
        //由于后面重新计算价格，所以后面排序
//            if (OtcConstant.DIRECTION.BUY.toString().equals(direction)) {
//                otcOrderLambdaQueryWrapper.orderByAsc(OtcOrder::getUpDownNumber);
//            } else {
//                otcOrderLambdaQueryWrapper.orderByDesc(OtcOrder::getUpDownNumber);
//            }
        Page<OtcOrder> p = new Page<>(Long.valueOf(page), Long.valueOf(size));

        IPage<OtcOrder> otcOrderIPage = fbOrderMapper.selectPage(p, otcOrderLambdaQueryWrapper);
        long total = otcOrderIPage.getTotal();
        List<OtcOrder> otcOrders = otcOrderIPage.getRecords();
        BigDecimal price = getPrice(currency);
        if (OtcConstant.DIRECTION.BUY.toString().equals(direction)) {
            otcOrders.stream()
//                       .sorted(Comparator.comparing(OtcOrder::getUpDownNumber))
                    .forEach(x -> {
                        BigDecimal upDownNumPrice = x.getUpDownNumber().multiply(price).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(price);
                        x.setNowPrice(upDownNumPrice);
                        if (upDownNumPrice.compareTo(x.getExtremum()) < 0) {
                            x.setNowPrice(x.getExtremum());
                        }
                        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        otcPriceOrderLambdaQueryWrapper.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.FINISH.toString()).eq(OtcPriceOrder::getOrderId, x.getId());
                        Integer integer = otcPriceOrderMapper.selectCount(otcPriceOrderLambdaQueryWrapper);
                        x.setDealCount(integer);
                    });
        } else {
            otcOrders.stream()
//                       .sorted(Comparator.comparing(OtcOrder::getUpDownNumber).reversed())
                    .forEach(x -> {
                        BigDecimal upDownNumPrice = x.getUpDownNumber().multiply(price).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(price);
                        x.setNowPrice(upDownNumPrice);
                        if (upDownNumPrice.compareTo(x.getExtremum()) > 0) {
                            x.setNowPrice(x.getExtremum());
                        }
                        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        otcPriceOrderLambdaQueryWrapper.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.FINISH.toString()).eq(OtcPriceOrder::getOrderId, x.getId());
                        Integer integer = otcPriceOrderMapper.selectCount(otcPriceOrderLambdaQueryWrapper);
                        x.setDealCount(integer);
                    });
        }
        List<OtcOrder> resultList = new ArrayList<>();
        if (OtcConstant.DIRECTION.BUY.toString().equals(direction)) {
            resultList = otcOrders.stream().sorted(Comparator.comparing(OtcOrder::getNowPrice)).collect(Collectors.toList());
        } else {
            resultList = otcOrders.stream().sorted(Comparator.comparing(OtcOrder::getNowPrice).reversed()).collect(Collectors.toList());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("data", resultList);
        return resultMap;

    }

    @Override
    public Object currencyList() {
        LambdaQueryWrapper<OtcCurrencyConfig> otcCurrencyConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcCurrencyConfigLambdaQueryWrapper.orderByAsc(OtcCurrencyConfig::getSort);
        return otcCurrencyConfigMapper.selectList(otcCurrencyConfigLambdaQueryWrapper);
    }

    @Override
    public Object placeAnOrder(String orderId, String memberId, String type, String num,Integer mccId) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(orderId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }

        if (Strings.isBlank(type)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(num)) {
            throw new BusinessException(AjaxResultEnum.QUANTITY_IS_EMPTY.getMessage());
        }
        fatherFilter(memberId);
        boolean exists = redisRepository.exists(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId);
        if (exists) {

            String s1 = redisRepository.get(CacheConstants.OTC_ORDER_BACK_COUNT_KEY + memberId);
            LambdaQueryWrapper<OtcTimeConfig> countLambdaQueryWrapper = new LambdaQueryWrapper<>();
            countLambdaQueryWrapper.eq(OtcTimeConfig::getType, "backCount");
            OtcTimeConfig otcTimeConfig = otcTimeMapper.selectOne(countLambdaQueryWrapper);
            if (otcTimeConfig != null) {
                if (s1.equals(otcTimeConfig.getMin())) {
                    throw new BusinessException(AjaxResultEnum.TOO_MANY_CANCELLATIONS_TODAY.getMessage());
                }
            }
        }

        OtcOrder otcOrder = fbOrderMapper.selectById(orderId);
        if (otcOrder == null || otcOrder.getId().toString().length() == 0) {
            throw new BusinessException(AjaxResultEnum.ORDER_QUERY_FAILED.getMessage());
        }
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.OTC_ORDER_KEY + orderId,
                5000, 50, 100);
        if (lock_coin) {
            Member user = fatherFilter(memberId);


            //判断用户和商家是否有相同的收款方式
//            String payType = otcOrder.getPayType();
//            String[] split = payType.split("-");
//            int conut = 0;
//            for (String s : split) {
//                if (OtcConstant.PAY_TYPE.ALIAY.toString().equals(s)) {
//                    if (!(user.getPayAliay() == null || user.getPayAliay().length() == 0)) {
//                        conut += 1;
//                        break;
//                    }
//                }
//                if (OtcConstant.PAY_TYPE.WECHAT.toString().equals(s)) {
//                    if (!(user.getPayWechat() == null || user.getPayWechat().length() == 0)) {
//                        conut += 1;
//                        break;
//                    }
//                }
//                if (OtcConstant.PAY_TYPE.BANKCARD.toString().equals(s)) {
//                    if (!(user.getBankCard() == null || user.getBankCard().length() == 0)) {
//                        conut += 1;
//                        break;
//                    }
//                }
//            }
//            if (conut == 0) {
//                throw new BusinessException("缺少与商家相同的付款方式，请先绑定");
//            }
            if (user.getId().equals(otcOrder.getMember())) {
                throw new BusinessException(AjaxResultEnum.DO_NOT_BUY_YOUR_OWN_ORDER.getMessage());
            }
            if (!OtcConstant.ORDER_STATUS.NORMAL.toString().equals(otcOrder.getStatus())) {
                throw new BusinessException(AjaxResultEnum.THE_ORDER_STATUS_HAS_CHANGED.getMessage());
            }
            //按价格购买 总金额=num
            BigDecimal sumNum = new BigDecimal(num);
            BigDecimal sumOrderPrice = new BigDecimal(num);
            BigDecimal price = getPrice(otcOrder.getCurrency());
            BigDecimal divide = price.multiply(otcOrder.getUpDownNumber()).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR);
            //订单单价
            BigDecimal orderPric = price.add(divide);
            if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.SELL.toString())) {
                //如果是卖单，当前价格应该大于最低价格
                if (orderPric.compareTo(otcOrder.getExtremum()) < 0) {
                    orderPric = otcOrder.getExtremum();
                }
            } else {
                if (orderPric.compareTo(otcOrder.getExtremum()) > 0) {
                    orderPric = otcOrder.getExtremum();
                }
            }
            if (OtcConstant.PLACEANORDER.NUM.toString().equals(type)) {
                //按数量购买  总金额=num*price
                sumOrderPrice = orderPric.multiply(new BigDecimal(num));
            } else {
                sumNum = sumNum.divide(orderPric, 8, BigDecimal.ROUND_FLOOR);
            }
            if (sumOrderPrice.compareTo(otcOrder.getMinPrice()) < 0 || sumOrderPrice.compareTo(otcOrder.getNum().multiply(orderPric)) > 0) {
                throw new BusinessException(AjaxResultEnum.EXCEED_THE_ORDER_LIMIT.getMessage());
            }
            //如果是用户卖的话，需要冻结资产
            if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
                //减少可用
                Map<String, Object> map = new HashMap<>();
                map.put("userId", memberId);
                map.put("balance", sumNum);
                map.put("currency", otcOrder.getCurrency());
                int i = subBalance(map);
                if (i < 1) {
                    throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                }
                //增加冻结
                int i1 = addDJBalance(map);
                if (i1 < 1) {
                    throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                }
            }
            //扣除库存
            otcOrder.setNum(otcOrder.getNum().subtract(sumNum));
            otcOrder.setFreeze(otcOrder.getFreeze().add(sumNum));
            if (otcOrder.getNum().compareTo(new BigDecimal("0")) == 0) {
                otcOrder.setStatus(OtcConstant.ORDER_STATUS.FINISH.toString());
            }
            //生成订单
            OtcPriceOrder otcPriceOrder = new OtcPriceOrder();
            otcPriceOrder.setUserId(memberId);
            otcPriceOrder.setStoreId(otcOrder.getMember());
            otcPriceOrder.setOrderId(otcOrder.getId());
            if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.SELL.toString())) {
                otcPriceOrder.setStoreDirection(OtcConstant.DIRECTION.SELL.toString());
                otcPriceOrder.setUserDirection(OtcConstant.DIRECTION.BUY.toString());
            } else {
                otcPriceOrder.setStoreDirection(OtcConstant.DIRECTION.BUY.toString());
                otcPriceOrder.setUserDirection(OtcConstant.DIRECTION.SELL.toString());
            }
            otcPriceOrder.setNum(sumNum);
            otcPriceOrder.setPrice(orderPric);
            otcPriceOrder.setTotalPrice(sumOrderPrice);
            otcPriceOrder.setStatus(OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString());
            otcPriceOrder.setCurrency(otcOrder.getCurrency());
            otcPriceOrder.setAppealStatus(OtcConstant.APPEAL_STATUS.NORMAL.toString());
            otcPriceOrder.setCreateTime(new Date());
            otcPriceOrder.setUpdateTime(new Date());

            fbOrderMapper.updateById(otcOrder);
            otcPriceOrderMapper.insert(otcPriceOrder);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("orderPriceId", otcPriceOrder.getId());
            resultMap.put("status", OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString());
            resultMap.put("name", user.getUname());
            resultMap.put("phone", user.getPhone());
            resultMap.put("sumPirce", sumOrderPrice);
            resultMap.put("num", sumNum);
            resultMap.put("price", orderPric);
            Map<String, Object> payMsg = new HashMap<>();
            Member shangJiaMember = memberMapper.selectById(otcOrder.getMember());
            if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.SELL)) {
                //如果是商家卖，返回商家收款方式
                payMsg = getPayMsg(shangJiaMember, otcOrder.getPayType());
            } else {
                //返回用户收款方式
                payMsg = getPayMsg(user, otcOrder.getPayType());
            }
            resultMap.put("payType", payMsg);
            LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
            memberLambdaQueryWrapper.eq(Member::getId, otcPriceOrder.getUserId());
            Member member = memberMapper.selectOne(memberLambdaQueryWrapper);
            resultMap.put("userName", member.getUname());
            resultMap.put("userPhone", member.getPhone());
            LambdaQueryWrapper<Member> memberLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            memberLambdaQueryWrapper1.eq(Member::getId, otcPriceOrder.getStoreId());
            Member member1 = memberMapper.selectOne(memberLambdaQueryWrapper1);
            resultMap.put("storeName", member1.getUname());

            resultMap.put("storePhone", member1.getPhone());
            resultMap.put("userId", otcPriceOrder.getUserId());
            resultMap.put("storeId", otcPriceOrder.getStoreId());
            resultMap.put("userDirection", otcPriceOrder.getUserDirection());
            resultMap.put("storeDirection", otcPriceOrder.getStoreDirection());

            //少存redis过期key
            LambdaQueryWrapper<OtcTimeConfig> otcTimeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
            otcTimeConfigLambdaQueryWrapper.eq(OtcTimeConfig::getType, "orderTime");
            OtcTimeConfig otcTimeConfig = otcTimeMapper.selectOne(otcTimeConfigLambdaQueryWrapper);
            Long time = Long.valueOf(otcTimeConfig.getMin()) * 60L;
            redisRepository.setExpire(CacheConstants.OTC_ORDER_BACK_KEY + otcPriceOrder.getId(), "1", time);
            redisDistributedLock.releaseLock(CacheConstants.OTC_ORDER_KEY + orderId);
            //sendMsg(shangJiaMember.getPhone(),"用户"+user.getUuid()+"对您发布的广告发起下单，请尽快处理");
            sendMsg(shangJiaMember.getPhone(), user.getUuid());
            return resultMap;
        } else {
            placeAnOrder(orderId, memberId, type, num, mccId);
        }
        return null;
    }

    @Override
    public Object orderDetails(String priceOrderId, String memberId) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(priceOrderId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }
        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcPriceOrderLambdaQueryWrapper.or(x -> x.eq(OtcPriceOrder::getUserId, memberId).or(x1 -> x1.eq(OtcPriceOrder::getStoreId, memberId))).eq(OtcPriceOrder::getId, priceOrderId);
        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectOne(otcPriceOrderLambdaQueryWrapper);
        if (otcPriceOrder == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_QUERY_FAILED.getMessage());
        }
        Map<String, Object> payMsg = new HashMap<>();
        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.SELL.toString())) {
            //如果用户卖  给用户收款方式
            payMsg = getPayMsg(memberMapper.selectById(otcPriceOrder.getUserId()), fbOrderMapper.selectById(otcPriceOrder.getOrderId()).getPayType());
        } else {
            //给商家收款方式
            payMsg = getPayMsg(memberMapper.selectById(otcPriceOrder.getStoreId()), fbOrderMapper.selectById(otcPriceOrder.getOrderId()).getPayType());
        }
        otcPriceOrder.setPayMap(payMsg);
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper.eq(Member::getId, otcPriceOrder.getUserId());
        Member member = memberMapper.selectOne(memberLambdaQueryWrapper);
        otcPriceOrder.setUserName(member.getUname());
        otcPriceOrder.setUserPhone(member.getPhone());
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper1.eq(Member::getId, otcPriceOrder.getStoreId());
        Member member1 = memberMapper.selectOne(memberLambdaQueryWrapper1);
        otcPriceOrder.setStoreName(member1.getUname());
        otcPriceOrder.setStorePhone(member1.getPhone());

        return otcPriceOrder;
    }

    @Override
    public Object ownOrderList(String priceOrderStatus, String memberId, String page, String size) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(priceOrderStatus)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(page)) {
            throw new BusinessException(AjaxResultEnum.PAGE_NUMBER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(size)) {
            throw new BusinessException(AjaxResultEnum.NUMBER_OF_ENTRIES_IS_EMPTY.getMessage());
        }
        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (priceOrderStatus.equals(OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString())) {
            otcPriceOrderLambdaQueryWrapper.or(x -> x.eq(OtcPriceOrder::getUserId, memberId).or(x1 -> x1.eq(OtcPriceOrder::getStoreId, memberId))).and(x -> x.eq(OtcPriceOrder::getStatus, priceOrderStatus).or(x1 -> x1.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.PAYMENT.toString())));

        } else {
            otcPriceOrderLambdaQueryWrapper.or(x -> x.eq(OtcPriceOrder::getUserId, memberId).or(x1 -> x1.eq(OtcPriceOrder::getStoreId, memberId))).eq(OtcPriceOrder::getStatus, priceOrderStatus);
        }

        Page<OtcPriceOrder> p = new Page<>(Long.valueOf(page), Long.valueOf(size));

        IPage<OtcPriceOrder> otcPriceOrderIPage = otcPriceOrderMapper.selectPage(p, otcPriceOrderLambdaQueryWrapper);
        List<OtcPriceOrder> records = otcPriceOrderIPage.getRecords();
        if (records.size() > 0) {
            records = records.stream().sorted(Comparator.comparing(OtcPriceOrder::getCreateTime).reversed()).collect(Collectors.toList());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", otcPriceOrderIPage.getTotal());
        resultMap.put("data", records);

        return resultMap;
    }

    @Override
    public Boolean payment(String priceOrderId, String memberId, String payType) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(payType)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(priceOrderId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }
        Integer count = 0;
        if (payType.equals(OtcConstant.PAY_TYPE.ALIAY.toString())) {
            count += 1;
        }
        if (payType.equals(OtcConstant.PAY_TYPE.WECHAT.toString())) {
            count += 1;
        }
        if (payType.equals(OtcConstant.PAY_TYPE.BANKCARD.toString())) {
            count += 1;
        }
        if (count == 0) {
            throw new BusinessException(AjaxResultEnum.WRONG_PAYMENT_METHOD.getMessage());
        }
        fatherFilter(memberId);
        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectById(priceOrderId);
        if (!otcPriceOrder.getStatus().equals(OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT.toString())) {
            throw new BusinessException(AjaxResultEnum.ABNORMAL_ORDER_STATUS.getMessage());
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
        }
        //修改订单状态
        otcPriceOrder.setPayType(payType);
        otcPriceOrder.setStatus(OtcConstant.PRICE_ORDER_STATUS.PAYMENT.toString());
        int i = otcPriceOrderMapper.updateById(otcPriceOrder);
        if (i < 1) {
            return false;
        }
        Member storeMember = memberMapper.selectById(otcPriceOrder.getStoreId());
        Member userMember = memberMapper.selectById(otcPriceOrder.getUserId());
        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
            //sendMsg(storeMember.getPhone(),"用户"+userMember.getUuid()+"对订单"+otcPriceOrder.getId()+"发起确认付款，请尽快放币，若没有收到付款，请发起申诉");
            sendMsg(storeMember.getPhone(), userMember.getUuid());
        } else {
            //sendMsg(userMember.getPhone(),"用户"+storeMember.getUuid()+"对订单"+otcPriceOrder.getId()+"发起确认付款，请尽快放币，若没有收到付款，请发起申诉");
            sendMsg(storeMember.getPhone(), userMember.getUuid());
        }

        return true;
    }

    @Override
    public Boolean deliverGoods(String priceOrderId, String memberId) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(priceOrderId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }
        fatherFilter(memberId);
        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectById(priceOrderId);
        if (!otcPriceOrder.getStatus().equals(OtcConstant.PRICE_ORDER_STATUS.PAYMENT.toString())) {
            throw new BusinessException(AjaxResultEnum.ABNORMAL_ORDER_STATUS.getMessage());
        }
        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
            //如果用户是买，那么只允许商家进行这个操作
            if (otcPriceOrder.getUserId().equals(memberId)) {
                throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
            }
            //给用户放币
            Map<String, Object> map = new HashMap<>();
            map.put("userId", otcPriceOrder.getStoreId());
            map.put("balance", otcPriceOrder.getNum());
            map.put("currency", otcPriceOrder.getCurrency());
            int i = subDJBalance(map);
            if (i == 0) {
                throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
            }
            map.put("userId", otcPriceOrder.getUserId());
            int i1 = addBalance(map);
            if (i1 == 0) {
                throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
            }
        } else {
            //用户是卖，只允许用户进行这个操作
            if (otcPriceOrder.getStoreId().equals(memberId)) {
                throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
            }
            //给商家放币
            Map<String, Object> map = new HashMap<>();
            map.put("userId", otcPriceOrder.getUserId());
            map.put("balance", otcPriceOrder.getNum());
            map.put("currency", otcPriceOrder.getCurrency());
            int i = subDJBalance(map);
            if (i == 0) {
                throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
            }
            map.put("userId", otcPriceOrder.getStoreId());
            int i1 = addBalance(map);
            if (i1 == 0) {
                throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
            }
        }


        //修改订单状态
        otcPriceOrder.setStatus(OtcConstant.PRICE_ORDER_STATUS.FINISH.toString());
        //修改总单数据
        OtcOrder otcOrder = fbOrderMapper.selectById(otcPriceOrder.getOrderId());
        otcOrder.setDealNum(otcOrder.getDealNum().add(otcPriceOrder.getNum()));
        otcOrder.setFreeze(otcOrder.getFreeze().subtract(otcPriceOrder.getNum()));
        otcPriceOrderMapper.updateById(otcPriceOrder);
        fbOrderMapper.updateById(otcOrder);
        Member storeMember = memberMapper.selectById(otcPriceOrder.getStoreId());
        Member userMember = memberMapper.selectById(otcPriceOrder.getUserId());
        if (otcPriceOrder.getUserDirection().equals(OtcConstant.DIRECTION.BUY.toString())) {
            //sendMsg(userMember.getPhone(),"用户"+storeMember.getUuid()+"对订单"+otcPriceOrder.getId()+"发起放币操作，请稍后查看余额，如果有疑问，请发起申诉");
            sendMsg(storeMember.getPhone(), userMember.getUuid());
        } else {
            //sendMsg(storeMember.getPhone(),"用户"+userMember.getUuid()+"对订单"+otcPriceOrder.getId()+"发起放币操作，请稍后查看余额，如果有疑问，请发起申诉");
            sendMsg(storeMember.getPhone(), userMember.getUuid());
        }
        return true;
    }

    @Override
    public Boolean backOrder(String priceOrderId, String memberId) {
        if (Strings.isBlank(memberId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(priceOrderId)) {
            throw new BusinessException(AjaxResultEnum.ORDER_ID_IS_EMPTY.getMessage());
        }
        fatherFilter(memberId);
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

    @Override
    public Object orderEndTime(String priceOrderId) {

        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectById(priceOrderId);
        if (otcPriceOrder == null) {
            throw new BusinessException(AjaxResultEnum.QUERY_FAILED.getMessage());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("nowTime", System.currentTimeMillis());
        LambdaQueryWrapper<OtcTimeConfig> otcTimeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcTimeConfigLambdaQueryWrapper.eq(OtcTimeConfig::getType, "orderTime");
        OtcTimeConfig otcTimeConfig = otcTimeMapper.selectOne(otcTimeConfigLambdaQueryWrapper);
        Long time = otcPriceOrder.getCreateTime().getTime();
        if (otcTimeConfig == null) {
            resultMap.put("orderEndTime", time + 30L * 60L * 1000L);
        } else {
            Long min = Long.valueOf(otcTimeConfig.getMin());
            resultMap.put("orderEndTime", time + min * 60L * 1000L);
        }

        return resultMap;
    }

    @Override
    public Object appeal(String priceOrderId, String userId, String msg, String file, String file1) {
        fatherFilter(userId);
        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectById(priceOrderId);
        if (otcPriceOrder == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_QUERY_FAILED.getMessage());
        }

        if (otcPriceOrder.getUserId().equals(userId) || otcPriceOrder.getStoreId().equals(userId)) {
            if (otcPriceOrder.getAppealStatus().equals(OtcConstant.APPEAL_STATUS.NORMAL.toString())) {
                //开始申诉
                otcPriceOrder.setAppealStatus(OtcConstant.APPEAL_STATUS.UNDERWAY.toString());
                otcPriceOrder.setAppealUserId(userId);
                OtcOrderAppeal otcOrderAppeal = new OtcOrderAppeal();
                otcOrderAppeal.setMemberId(userId);
                otcOrderAppeal.setMsg(msg);
//                try {
//                    otcOrderAppeal.setPic(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename()));
//                    otcOrderAppeal.setPic1(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(file1.getBytes()), file1.getOriginalFilename()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                otcOrderAppeal.setPic(file);
                otcOrderAppeal.setPic1(file1);
                otcOrderAppeal.setPriceOrderId(priceOrderId);
                otcOrderAppeal.setStatus(OtcConstant.APPEAL_STATUS.UNDERWAY.toString());
                otcPriceOrderMapper.updateById(otcPriceOrder);
                otcOrderAppealMapper.insert(otcOrderAppeal);
                return true;
            } else {
                throw new BusinessException(AjaxResultEnum.ORDER_APPEALED.getMessage());
            }
        } else {
            throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
        }
    }

    @Override
    public Object appealBack(String priceOrderId, String userId) {
        fatherFilter(userId);
        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcPriceOrderLambdaQueryWrapper.eq(OtcPriceOrder::getId, priceOrderId).eq(OtcPriceOrder::getAppealUserId, userId).eq(OtcPriceOrder::getAppealStatus, OtcConstant.APPEAL_STATUS.UNDERWAY.toString());
        OtcPriceOrder otcPriceOrder = otcPriceOrderMapper.selectOne(otcPriceOrderLambdaQueryWrapper);
        if (otcPriceOrder == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_QUERY_FAILED_OR_ORDER_APPEAL_STATUS_CHANGED.getMessage());
        }
        otcPriceOrder.setAppealStatus(OtcConstant.APPEAL_STATUS.NORMAL.toString());
        LambdaQueryWrapper<OtcOrderAppeal> otcOrderAppealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderAppealLambdaQueryWrapper.eq(OtcOrderAppeal::getPriceOrderId, priceOrderId);
        OtcOrderAppeal otcOrderAppeal = otcOrderAppealMapper.selectOne(otcOrderAppealLambdaQueryWrapper);
        if (otcOrderAppeal == null) {
            throw new BusinessException(AjaxResultEnum.QUERY_FAILED.getMessage());
        }
        otcOrderAppeal.setStatus(OtcConstant.APPEAL_STATUS.BACK.toString());
        otcPriceOrderMapper.updateById(otcPriceOrder);
        otcOrderAppealMapper.updateById(otcOrderAppeal);
        return true;
    }

    @Override
    public Object merchantsOrders(String userId, String page, String size) {
        Page<OtcOrder> p = new Page<>(Long.valueOf(page), Long.valueOf(size));
        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.eq(OtcOrder::getMember, userId);
        IPage<OtcOrder> otcOrderIPage = fbOrderMapper.selectPage(p, otcOrderLambdaQueryWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", otcOrderIPage.getTotal());
        List<OtcOrder> records = otcOrderIPage.getRecords();
        if (records.size() > 0) {
            records.stream()
//                       .sorted(Comparator.comparing(OtcOrder::getUpDownNumber))
                    .forEach(x -> {

                        BigDecimal price = getPrice(x.getCurrency());
                        BigDecimal upDownNumPrice = x.getUpDownNumber().multiply(price).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(price);
                        x.setNowPrice(upDownNumPrice);
                        if (OtcConstant.DIRECTION.BUY.toString().equals(x.getDirection())) {
                            if (upDownNumPrice.compareTo(x.getExtremum()) > 0) {
                                x.setNowPrice(x.getExtremum());
                            }
                        } else {
                            if (upDownNumPrice.compareTo(x.getExtremum()) < 0) {
                                x.setNowPrice(x.getExtremum());
                            }
                        }

                    });

        }
        records = records.stream().sorted(Comparator.comparing(OtcOrder::getCreateTime).reversed()).collect(Collectors.toList());

        resultMap.put("data", records);
        return resultMap;
    }

    @Override
    public Object getNowPrice(String currency) {
        BigDecimal price = getPrice(currency);

        return price;
    }

    @Override
    public Object ratioConfig() {
        LambdaQueryWrapper<OtcTimeConfig> otcTimeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcTimeConfigLambdaQueryWrapper.or(x -> x.eq(OtcTimeConfig::getType, "minRatio")).or(x1 -> x1.eq(OtcTimeConfig::getType, "maxRatio"));
        List<OtcTimeConfig> otcTimeConfigs = otcTimeMapper.selectList(otcTimeConfigLambdaQueryWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        for (OtcTimeConfig otcTimeConfig : otcTimeConfigs) {
            resultMap.put(otcTimeConfig.getType(), otcTimeConfig.getMin());
        }
        return resultMap;
    }

    @Override
    public Object updataOrder(String orderId, String userId, String upDownNumber, String extremum, String num, String minPrice, String payType,
                              String remarks, String autoStatus, String autoMsg) {
        if (Strings.isBlank(userId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(payType)) {
            throw new BusinessException(AjaxResultEnum.WRONG_PAYMENT_METHOD.getMessage());
        }

        if (Strings.isBlank(upDownNumber)) {
            throw new BusinessException(AjaxResultEnum.THE_PRICE_FLOATING_RATIO_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(extremum)) {
            throw new BusinessException(AjaxResultEnum.EXTREME_PRICE_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(autoStatus)) {
            throw new BusinessException(AjaxResultEnum.AUTOREPLY_STATUS_IS_EMPTY.getMessage());
        }

        if (Strings.isBlank(num)) {
            throw new BusinessException(AjaxResultEnum.QUANTITY_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(minPrice)) {
            throw new BusinessException(AjaxResultEnum.MINIMUM_TURNOVER_IS_EMPTY.getMessage());
        }

        LambdaQueryWrapper<OtcPriceOrder> otcPriceOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcPriceOrderLambdaQueryWrapper.or(x -> x.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.NONPAYMENT).or(x1 -> x1.eq(OtcPriceOrder::getStatus, OtcConstant.PRICE_ORDER_STATUS.PAYMENT))).eq(OtcPriceOrder::getOrderId, orderId);
        List<OtcPriceOrder> otcPriceOrders = otcPriceOrderMapper.selectList(otcPriceOrderLambdaQueryWrapper);
        if (otcPriceOrders.size() > 0) {
            throw new BusinessException(AjaxResultEnum.THERE_ARE_PENDING_TRANSACTIONS.getMessage());
        }
        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.eq(OtcOrder::getId, orderId).eq(OtcOrder::getMember, userId);
        OtcOrder otcOrder = fbOrderMapper.selectOne(otcOrderLambdaQueryWrapper);
        if (otcOrder == null) {
            throw new BusinessException(AjaxResultEnum.QUERY_FAILED.getMessage());
        }
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.OTC_ORDER_KEY + orderId,
                5000, 50, 100);
        if (lock_coin) {


            if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.SELL.toString())) {
                BigDecimal afterNum = new BigDecimal(otcOrder.getNum().toString());
                if (afterNum.compareTo(new BigDecimal(num)) > 0) {
                    //退冻结
                    BigDecimal subtract = afterNum.subtract(new BigDecimal(num));
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", userId);
                    map.put("balance", subtract);
                    map.put("currency", otcOrder.getCurrency());
                    int i = subDJBalance(map);
                    if (i < 1) {
                        throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                    }
                    int i1 = addBalance(map);
                    if (i1 < 1) {
                        throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                    }
                } else {
                    //加冻结
                    BigDecimal subtract = new BigDecimal(num).subtract(afterNum);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", userId);
                    map.put("balance", subtract);
                    map.put("currency", otcOrder.getCurrency());
                    int i = subBalance(map);
                    if (i < 1) {
                        throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                    }
                    int i1 = addDJBalance(map);
                    if (i1 < 1) {
                        throw new BusinessException(AjaxResultEnum.FAILED_TO_UPDATE_ASSETS.getMessage());
                    }
                }
            }
            otcOrder.setUpDownNumber(new BigDecimal(upDownNumber));
            otcOrder.setExtremum(new BigDecimal(extremum));
            otcOrder.setNum(new BigDecimal(num));
            otcOrder.setMinPrice(new BigDecimal(minPrice));
            otcOrder.setPayType(payType);
            otcOrder.setRemarks(remarks);
            otcOrder.setAutoStatus(autoStatus);
            otcOrder.setAutoMsg(autoMsg);
            fbOrderMapper.updateById(otcOrder);
            redisDistributedLock.releaseLock(CacheConstants.OTC_ORDER_KEY + orderId);
        } else {
            updataOrder(orderId, userId, upDownNumber, extremum, num, minPrice, payType,
                    remarks, autoStatus, autoMsg);
        }
        return true;
    }

    @Override
    public Object getAppealList() {
        List<OtcAppealType> otcAppealTypes = otcAppealTypeMapper.selectList(null);
        return otcAppealTypes;
    }

    @Override
    public Object orderBack(String orderId, String userId) {
        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.eq(OtcOrder::getId, orderId).eq(OtcOrder::getMember, userId);
        OtcOrder otcOrder = fbOrderMapper.selectOne(otcOrderLambdaQueryWrapper);
        if (otcOrder == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_QUERY_FAILED.getMessage());
        }
        if (!otcOrder.getStatus().equals(OtcConstant.ORDER_STATUS.NORMAL.toString())) {
            throw new BusinessException(AjaxResultEnum.PLEASE_UPDATE_YOUR_ORDER_STATUS_AND_TRY_AGAIN.getMessage());
        }
        if (otcOrder.getFreeze().compareTo(new BigDecimal("0")) > 0) {
            throw new BusinessException(AjaxResultEnum.UNFULFILLED_ORDERS_CANNOT_BE_CANCELLED.getMessage());
        }
        //如果为卖单，需要取消冻结
        if (otcOrder.getDirection().equals(OtcConstant.DIRECTION.SELL.toString())) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("balance", otcOrder.getNum());
            map.put("currency", otcOrder.getCurrency());
            int i = subDJBalance(map);
            if (i < 1) {
                throw new BusinessException(AjaxResultEnum.ASSET_UPDATE_FAILED_PLEASE_CONTACT_CUSTOMER_SERVICE.getMessage());
            }
            int i1 = addBalance(map);
            if (i1 < 1) {
                throw new BusinessException(AjaxResultEnum.ASSET_UPDATE_FAILED_PLEASE_CONTACT_CUSTOMER_SERVICE.getMessage());
            }
        }
        otcOrder.setStatus(OtcConstant.ORDER_STATUS.BACKOUT.toString());
        fbOrderMapper.updateById(otcOrder);
        return true;
    }

    @Override
    public Object fastPlaceAnOrder(String userId, String price, String direction, String currency, String payType,Integer mccId) {
        if (Strings.isBlank(userId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(price)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(direction)) {
            throw new BusinessException(AjaxResultEnum.DIRECTION_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(currency)) {
            throw new BusinessException(AjaxResultEnum.CURRENCY_IS_EMPTY.getMessage());
        }
        BigDecimal price1 = getPrice(currency.toUpperCase());

        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.ne(OtcOrder::getDirection, direction).eq(OtcOrder::getCurrency, currency).like(OtcOrder::getPayType, payType).ne(OtcOrder::getMember, userId).eq(OtcOrder::getStatus, OtcConstant.ORDER_STATUS.NORMAL.toString());
        List<OtcOrder> otcOrders = fbOrderMapper.selectList(otcOrderLambdaQueryWrapper);
        Object result = null;
        if (otcOrders.size() > 0) {
            otcOrders = otcOrders.stream().filter(x -> {
                BigDecimal otcPrice = price1.multiply(x.getUpDownNumber()).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(price1);
                BigDecimal i = new BigDecimal(price).divide(otcPrice, 8, BigDecimal.ROUND_FLOOR);
                if (x.getNum().compareTo(i) < 0) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            if (otcOrders.size() == 0) {
                throw new BusinessException(AjaxResultEnum.THERE_ARE_CURRENTLY_NO_SUITABLE_ORDERS.getMessage());
            }
            if (direction.equals(OtcConstant.DIRECTION.SELL)) {
                Optional<OtcOrder> max = otcOrders.stream().max(Comparator.comparing(OtcOrder::getUpDownNumber));
                OtcOrder otcOrder = max.get();
                //取价格最高
                result = placeAnOrder(otcOrder.getId(), userId, "PRICE", price, mccId);
            } else {
                //取价格最低
                Optional<OtcOrder> min = otcOrders.stream().min(Comparator.comparing(OtcOrder::getUpDownNumber));
                OtcOrder otcOrder = min.get();
                //取价格最高
                result = placeAnOrder(otcOrder.getId(), userId, "PRICE", price, mccId);
            }
        } else {
            throw new BusinessException(AjaxResultEnum.THERE_ARE_CURRENTLY_NO_SUITABLE_ORDERS.getMessage());
        }

        return result;
    }

    @Override
    public Object fastPlaceAnOrderGetPrice(String userId, String type, String price, String direction, String currency, String payType) {
        if (Strings.isBlank(price)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(userId)) {
            throw new BusinessException(AjaxResultEnum.MEMBER_ID_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(type)) {
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(direction)) {
            throw new BusinessException(AjaxResultEnum.DIRECTION_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(payType)) {
            throw new BusinessException(AjaxResultEnum.WRONG_PAYMENT_METHOD.getMessage());
        }
        if (Strings.isBlank(currency)) {
            throw new BusinessException(AjaxResultEnum.CURRENCY_IS_EMPTY.getMessage());
        }
        BigDecimal num = new BigDecimal(price);
        Map<String, Object> resultMap = new HashMap<>();
        BigDecimal nowPrice = getPrice(currency.toUpperCase());

        LambdaQueryWrapper<OtcOrder> otcOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        otcOrderLambdaQueryWrapper.ne(OtcOrder::getDirection, direction).eq(OtcOrder::getCurrency, currency).like(OtcOrder::getPayType, payType).ne(OtcOrder::getMember, userId).eq(OtcOrder::getStatus, OtcConstant.ORDER_STATUS.NORMAL.toString());
        List<OtcOrder> otcOrders = fbOrderMapper.selectList(otcOrderLambdaQueryWrapper);
        BigDecimal endPrice = new BigDecimal("0");
        if (otcOrders.size() > 0) {
            if (OtcConstant.PLACEANORDER.PRICE.toString().equals(type)) {
                otcOrders = otcOrders.stream().filter(x -> {
                    BigDecimal otcPrice = nowPrice.multiply(x.getUpDownNumber()).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(nowPrice);
                    BigDecimal i = new BigDecimal(price).divide(otcPrice, 8, BigDecimal.ROUND_FLOOR);
                    if (x.getNum().compareTo(i) < 0) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
            }
            if (otcOrders.size() == 0) {
                throw new BusinessException(AjaxResultEnum.THERE_ARE_CURRENTLY_NO_SUITABLE_ORDERS.getMessage());
            }
            if (direction.equals(OtcConstant.DIRECTION.SELL.toString())) {
                Optional<OtcOrder> max = otcOrders.stream().max(Comparator.comparing(OtcOrder::getUpDownNumber));
                OtcOrder otcOrder = max.get();
                endPrice = nowPrice.multiply(otcOrder.getUpDownNumber()).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(nowPrice);
                if (endPrice.compareTo(otcOrder.getExtremum()) > 0) {
                    endPrice = otcOrder.getExtremum();
                }
                resultMap.put("price", endPrice);

            } else {
                //取价格最低
                Optional<OtcOrder> min = otcOrders.stream().min(Comparator.comparing(OtcOrder::getUpDownNumber));
                OtcOrder otcOrder = min.get();
                //取价格最高
                endPrice = nowPrice.multiply(otcOrder.getUpDownNumber()).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_FLOOR).add(nowPrice);
                if (endPrice.compareTo(otcOrder.getExtremum()) < 0) {
                    endPrice = otcOrder.getExtremum();
                }
                resultMap.put("price", endPrice);

            }
        } else {
            throw new BusinessException(AjaxResultEnum.THERE_ARE_CURRENTLY_NO_SUITABLE_ORDERS.getMessage());
        }
        if (OtcConstant.PLACEANORDER.PRICE.toString().equals(type)) {
            resultMap.put("priceOrNum", new BigDecimal(price).divide(endPrice, 8, BigDecimal.ROUND_FLOOR));

        } else {
            resultMap.put("priceOrNum", num.multiply(endPrice));
        }
        return resultMap;
    }


    /**
     * 通用的拦截效验
     *
     * @param userId
     * @return
     */
    public Member fatherFilter(String userId) {
        //法币状态是否开通
        Member member = memberMapper.selectById(userId);
        if (member == null) {
            throw new BusinessException(AjaxResultEnum.USER_DOES_NOT_EXIST.getMessage());
        }
        //法币状态是否开通
        if (!member.getFbStatus().equals(MemConstant.Fb_Status.NORMAL.toString())) {
            throw new BusinessException(AjaxResultEnum.OTC_ACCOUNT_HAS_BEEN_FROZEN.getMessage());
        }
        //判断是否实名
        if (member.getCardNo() == null || member.getCardNo().length() == 0) {
            throw new BusinessException(AjaxResultEnum.PLEASE_PASS_REAL_NAME_AUTHENTICATION_FIRST.getMessage());
        }
        return member;
    }


    public BigDecimal getPrice(String cur) {

        String s = "";
        cur = cur.toUpperCase();
        //if (cur.equals("USDT")) {
        if ("USDT".equals(cur)) {
            s = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + cur.toUpperCase());
            JSONObject parse = (JSONObject) JSONObject.parse(s);
            Object price = parse.get("close");
            if (price != null) {
                return new BigDecimal(price.toString());
            } else {
                throw new BusinessException(AjaxResultEnum.QUERY_FAILED.getMessage());
            }
        } else {
            String curPriceStr = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + cur.toUpperCase() + "/USDT");
            String usdtPriceStr = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + "USDT");
            JSONObject curPriceObj = (JSONObject) JSONObject.parse(curPriceStr);
            JSONObject usdtPriceObj = (JSONObject) JSONObject.parse(usdtPriceStr);
            Object usdtPrice = usdtPriceObj.get("close");
            Object curPrice = curPriceObj.get("nowPrice");
            if (usdtPrice != null && curPrice != null) {
                return new BigDecimal(usdtPrice.toString()).multiply(new BigDecimal(curPrice.toString()));
            } else {
                throw new BusinessException(AjaxResultEnum.QUERY_FAILED.getMessage());
            }
        }
    }

    /**
     * 获取收款方式
     *
     * @param member  用户
     * @param payType 支持的付款方式
     * @return
     */
    public Map<String, Object> getPayMsg(Member member, String payType) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> aliay = new HashMap<>();
        Map<String, Object> wechat = new HashMap<>();
        Map<String, Object> bankCard = new HashMap<>();

        String[] split = payType.split("-");
        for (String s : split) {
            if (OtcConstant.PAY_TYPE.ALIAY.toString().equals(s)) {
                aliay.put("name", member.getAliayName());
                aliay.put("url", member.getPayAliay());
                continue;
            }
            if (OtcConstant.PAY_TYPE.WECHAT.toString().equals(s)) {
                wechat.put("name", member.getWechatName());
                wechat.put("url", member.getPayWechat());
                continue;
            }
            if (OtcConstant.PAY_TYPE.BANKCARD.toString().equals(s)) {
                bankCard.put("name", member.getBankUserName());
                bankCard.put("bankName", member.getBankName());
                bankCard.put("bankAddress", member.getBankAddress());
                bankCard.put("bankCard", member.getBankCard());
                continue;
            }
        }

        map.put(OtcConstant.PAY_TYPE.ALIAY.toString(), aliay);
        map.put(OtcConstant.PAY_TYPE.WECHAT.toString(), wechat);
        map.put(OtcConstant.PAY_TYPE.BANKCARD.toString(), bankCard);
        return map;
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

    public int subBalance(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean exists = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (exists) {
            balanceMapper.subBalance(map);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
            return 1;
        } else {
            subBalance(map);
        }
        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        return 0;
    }

    public int addDJBalance(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean exists = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (exists) {
            balanceMapper.addDJBalance(map);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
            return 1;
        } else {
            addDJBalance(map);
        }
        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        return 0;
    }

    public int addBalance(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());

        boolean exists = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId, 5000, 50, 100);
        if (exists) {
            balanceMapper.addBalance(map);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
            return 1;
        } else {
            addBalance(map);
        }
        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        return 0;
    }

    public boolean sendMsg(String phone, String param) {
        /*final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String NoticeSignName = sysConfigMapper.selectByParamKey(SYSconfig.NoticeSignName);
        final String OTCTemplateCode = sysConfigMapper.selectByParamKey(SYSconfig.OTCTemplateCode);

        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;
        DefaultProfile profile = DefaultProfile.getProfile(regionid, access_key_id, access_key_secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionid);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", NoticeSignName);
        request.putQueryParameter("TemplateCode", OTCTemplateCode);
        request.putQueryParameter("TemplateParam", "{\"name\":" + param + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return Boolean.TRUE;
    }

    public AliyunClient getAliyunClient() {
        final String folder = sysConfigMapper.selectByParamKey(SYSconfig.FOLDER);
        final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String bucket = sysConfigMapper.selectByParamKey(SYSconfig.Bucket);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String endpoint = sysConfigMapper.selectByParamKey(SYSconfig.ENDPOINT);
        return new AliyunClient(endpoint, access_key_id, access_key_secret, bucket, folder);
    }
}