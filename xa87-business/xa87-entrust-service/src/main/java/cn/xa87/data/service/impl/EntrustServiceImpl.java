package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.EntrustConstant;
import cn.xa87.data.mapper.*;
import cn.xa87.data.product.MatchProducer;
import cn.xa87.data.service.EntrustService;
import cn.xa87.data.utils.RandomUtils;
import cn.xa87.model.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EntrustServiceImpl extends ServiceImpl<EntrustMapper, Entrust> implements EntrustService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final ConcurrentHashMap<String, Object> member_entrust = new ConcurrentHashMap<String, Object>();
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private EntrustMapper entrustMapper;
    @Autowired
    private EntrustHistoryMapper entrustHistoryMapper;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private MatchProducer matchProducer;
    @Resource
    private CurrencyControlsMapper currencyControlsMapper;
    @Resource
    private KlineDataMapper klineDataMapper;
    @Resource
    private StagePriceMapper stagePriceMapper;


    @Override
    public boolean setEntrust(Entrust entrust) {
        if (!member_entrust.containsKey(entrust.getMember() + entrust.getPairs())) {
            member_entrust.put(entrust.getMember() + entrust.getPairs(), 1);
        } else {
            throw new BusinessException(AjaxResultEnum.FREQUENT_OPERATIONS.getMessage());
        }
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_ENTRUST_PAIRS_KEY + entrust.getMember(), 15000, 50, 100);
        if (lock_coin) {
            member_entrust.remove(entrust.getMember() + entrust.getPairs());
            QueryWrapper<Pairs> wrapperPairs = new QueryWrapper<Pairs>();
            wrapperPairs.eq("id", entrust.getPairs());
            wrapperPairs.eq("state", CoinConstant.Coin_State.NORMAL);
            Pairs pairs = pairsMapper.selectOne(wrapperPairs);
            if (pairs == null) {
                throw new BusinessException(AjaxResultEnum.TRADING_PAIR_DOES_NOT_EXIST.getMessage());
            }
            entrust.setPairsName(pairs.getPairsName());
            entrust.setMainCur(pairs.getMainCur());
            entrust.setTokenCur(pairs.getTokenCur());
            entrust.setMemberType("people");
            Member memberInfo = memberMapper.selectById(entrust.getMember());
            if (memberInfo.getType() != null) {
                if (memberInfo.getType().equals("INTERNAL")) {
                    entrust.setTradeRate(new BigDecimal("0"));
                } else {
                    entrust.setTradeRate(pairs.getTradeRate());
                }
            } else {
                entrust.setTradeRate(pairs.getTradeRate());
            }
            entrust.setUld("UP");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            entrust.setId(uuid);
            sendEntrustNew(entrust, pairs);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_ENTRUST_PAIRS_KEY + entrust.getMember());
        } else {
            member_entrust.remove(entrust.getMember() + entrust.getPairs());
            throw new BusinessException(AjaxResultEnum.FREQUENT_OPERATIONS.getMessage());
        }
        member_entrust.remove(entrust.getMember() + entrust.getPairs());
        return true;
    }


    private void sendEntrustNew(Entrust entrust, Pairs pairs) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember(),
                15000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            // 根据委托类型查询用户钱包
            switch (entrust.getEntrustType().getType()) {
                case "BUY":
                    wrapper.eq("currency", pairs.getMainCur());
                    break;
                case "SELL":
                    wrapper.eq("currency", pairs.getTokenCur());
                    break;
            }
            wrapper.eq("user_id", entrust.getMember());
            Balance balance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = balance.getAssetsBalance().setScale(8, BigDecimal.ROUND_HALF_UP);

            // 市价
            if (entrust.getPriceType().equals(ContractConstant.Price_Type.MARKET_PRICE)) {
                BigDecimal price = entrust.getPrice();
                if (null == price) {
                    String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName());
                    JSONObject jsonInfo = JSONObject.parseObject(result);
                    price = jsonInfo.getBigDecimal("nowPrice");
                    entrust.setPrice(price);
                }

                entrust.setUld("MARKET");
                // 买
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0.1")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getMatchFee()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    // 全部交易
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
                        BigDecimal count = assetsBalance.divide(entrust.getPrice(), 8, BigDecimal.ROUND_HALF_UP);
                        entrust.setCount(count);
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        // 部分交易
                        entrust.setCount(entrust.getMatchFee().divide(price, 8, BigDecimal.ROUND_HALF_UP));
                        balance.setAssetsBalance(assetsBalance.subtract(entrust.getMatchFee()));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getMatchFee()));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), entrust.getMatchFee());
                    }

                } else {
                    // 卖
                    if (entrust.getCount().compareTo(new BigDecimal("0.00001")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_QUANTITY_SOLD_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getCount()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 2, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        balance.setAssetsBalance(assetsBalance.subtract(entrust.getCount()));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getCount()));
                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 2, assetsBalance, balance.getAssetsBalance(), entrust.getCount());
                    }
                }
                entrust.setSurplusCount(entrust.getCount());
                entrust.setState(EntrustConstant.Order_State.CREATE);
                balanceMapper.updateById(balance);
                entrustMapper.insert(entrust);
                redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
            } else {
                BigDecimal bigDecimal = new BigDecimal("0");
                if (bigDecimal.compareTo(entrust.getCount()) == 1) {
                    throw new BusinessException(AjaxResultEnum.QUANTITY_CANNOT_BE_LESS.getMessage());
                }
                if (bigDecimal.compareTo(entrust.getPrice()) == 1) {
                    throw new BusinessException(AjaxResultEnum.PRICE_CANNOT_BE_LESS_THAN.getMessage());
                }
                entrust.setSurplusCount(entrust.getCount());
//				BigDecimal lowUpPrice = new BigDecimal("0.05");
//				String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName());
//				JSONObject jsonInfo = JSONObject.parseObject(result);
//				BigDecimal price = jsonInfo.getBigDecimal("nowPrice");
                entrust.setState(EntrustConstant.Order_State.CREATE);

                // 冻结用户资产
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0.1")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (entrust.getCount().compareTo(new BigDecimal("0")) == 0) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    BigDecimal sumPrice = entrust.getCount().multiply(entrust.getPrice());
                    if (assetsBalance.compareTo(sumPrice) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
                        BigDecimal count = assetsBalance.divide(entrust.getPrice(), 8, BigDecimal.ROUND_HALF_UP);
                        entrust.setCount(count);
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        balance.setAssetsBalance(assetsBalance.subtract(sumPrice));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(sumPrice));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), sumPrice);
                    }
                } else {
                    if (entrust.getCount().compareTo(new BigDecimal("0.00001")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_QUANTITY_SOLD_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getCount()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    balance.setAssetsBalance(assetsBalance.subtract(entrust.getCount()));
                    balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getCount()));

                    // 保存流水记录
                    saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 2, assetsBalance, balance.getAssetsBalance(), entrust.getCount());
                }
                balanceMapper.updateById(balance);
                entrust.setSurplusCount(entrust.getCount());
                entrustMapper.insert(entrust);
                redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
            }
        } else {
            sendEntrustNew(entrust, pairs);
        }
    }

    private void sendEntrust(Entrust entrust, Pairs pairs) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember(),
                15000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            // 根据委托类型查询用户钱包
            switch (entrust.getEntrustType().getType()) {
                case "BUY":
                    wrapper.eq("currency", pairs.getMainCur());
                    break;
                case "SELL":
                    wrapper.eq("currency", pairs.getTokenCur());
                    break;
            }
            wrapper.eq("user_id", entrust.getMember());
            Balance balance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = balance.getAssetsBalance().setScale(8, BigDecimal.ROUND_HALF_UP);

            // 市价
            if (entrust.getPriceType().equals(ContractConstant.Price_Type.MARKET_PRICE)) {
                BigDecimal price = entrust.getPrice();
                if (null == price) {
                    String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName());
                    JSONObject jsonInfo = JSONObject.parseObject(result);
                    price = jsonInfo.getBigDecimal("nowPrice");
                    entrust.setPrice(price);
                }

                entrust.setUld("MARKET");
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0.1")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getMatchFee()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    // 全部交易
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
//                        BigDecimal count = balance.getAssetsBalance().divide(entrust.getPrice(), 8, BigDecimal.ROUND_HALF_UP);
                        BigDecimal count = assetsBalance.divide(entrust.getPrice(), 8, BigDecimal.ROUND_HALF_UP);
                        entrust.setCount(count);
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        // 部分交易
                        entrust.setCount(entrust.getMatchFee().divide(price, 8, BigDecimal.ROUND_HALF_UP));
                        balance.setAssetsBalance(assetsBalance.subtract(entrust.getMatchFee()));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getMatchFee()));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), entrust.getMatchFee());
                    }

                } else {
                    if (entrust.getCount().compareTo(new BigDecimal("0.00001")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_QUANTITY_SOLD_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getCount()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 1, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        balance.setAssetsBalance(assetsBalance.subtract(entrust.getCount()));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getCount()));
                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 1, assetsBalance, balance.getAssetsBalance(), entrust.getCount());
                    }

                }
                balanceMapper.updateById(balance);

                if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                    logger.info("进入结算1" + JSONObject.toJSONString(entrust));
                    matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
                } else {
                    matchProducer.putProjectEntrustMatch(JSONObject.toJSONString(entrust));
                }
                //redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
            } else {
                BigDecimal bigDecimal = new BigDecimal("0");
                if (bigDecimal.compareTo(entrust.getCount()) == 1) {
                    throw new BusinessException(AjaxResultEnum.QUANTITY_CANNOT_BE_LESS.getMessage());
                }
                if (bigDecimal.compareTo(entrust.getPrice()) == 1) {
                    throw new BusinessException(AjaxResultEnum.PRICE_CANNOT_BE_LESS_THAN.getMessage());
                }
                entrust.setSurplusCount(entrust.getCount());
//				BigDecimal lowUpPrice = new BigDecimal("0.05");
//				String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairs.getPairsName());
//				JSONObject jsonInfo = JSONObject.parseObject(result);
//				BigDecimal price = jsonInfo.getBigDecimal("nowPrice");
                entrust.setState(EntrustConstant.Order_State.CREATE);
//				if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
//					if (price.subtract(price.multiply(lowUpPrice)).compareTo(entrust.getPrice()) == 1) {
//						redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY  + entrust.getMember());
//						throw new BusinessException("价格下浮不能超过市价5%");
//					}
//				} else {
//					if (price.multiply(lowUpPrice).add(price).compareTo(entrust.getPrice()) == -1) {
//						redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY  + entrust.getMember());
//						throw new BusinessException("价格上浮不能超过市价5%");
//					}
//				}
                // 冻结用户资产
                if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.BUY)) {
                    // entrust.setCount(entrust.getPercentageCount().multiply(balance.getBalance()).divide(entrust.getPrice(),
                    // 8, BigDecimal.ROUND_HALF_UP));
                    if (entrust.getMatchFee().compareTo(new BigDecimal("0.1")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    if (entrust.getCount().compareTo(new BigDecimal("0")) == 0) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_PURCHASE_QUANTITY_IS_TOO_SMALL.getMessage());
                    }
                    BigDecimal sumPrice = entrust.getCount().multiply(entrust.getPrice());
                    if (assetsBalance.compareTo(sumPrice) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    if (entrust.getPercentageCount().compareTo(new BigDecimal("1")) == 0) {
                        BigDecimal count = assetsBalance.divide(entrust.getPrice(), 8, BigDecimal.ROUND_HALF_UP);
                        entrust.setCount(count);
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(assetsBalance));
                        balance.setAssetsBalance(new BigDecimal("0"));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), assetsBalance);
                    } else {
                        balance.setAssetsBalance(assetsBalance.subtract(sumPrice));
                        balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(sumPrice));

                        // 保存流水记录
                        saveBalanceRecord(entrust.getMember(), pairs.getMainCur(), 31, 1, assetsBalance, balance.getAssetsBalance(), sumPrice);
                    }
                } else {
                    if (entrust.getCount().compareTo(new BigDecimal("0.00001")) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.THE_QUANTITY_SOLD_IS_TOO_SMALL.getMessage());
                    }
                    if (assetsBalance.compareTo(entrust.getCount()) == -1) {
                        redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
                        throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                    }
                    balance.setAssetsBalance(assetsBalance.subtract(entrust.getCount()));
                    balance.setAssetsBlockedBalance(balance.getAssetsBlockedBalance().add(entrust.getCount()));

                    // 保存流水记录
                    saveBalanceRecord(entrust.getMember(), pairs.getTokenCur(), 34, 1, assetsBalance, balance.getAssetsBalance(), entrust.getCount());
                }
                balanceMapper.updateById(balance);
                entrust.setSurplusCount(entrust.getCount());
                entrustMapper.insert(entrust);
                if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                    logger.info("进入结算1-2" + JSONObject.toJSONString(entrust));
                    matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
                } else {
                    matchProducer.putProjectEntrustMatch(JSONObject.toJSONString(entrust));
                }
                redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + entrust.getMember());
            }
        } else {
            sendEntrust(entrust, pairs);
        }

    }

    // 保存资金记录
    private void saveBalanceRecord(String memberId, String currency, Integer balanceType, Integer fundsType, BigDecimal balanceBefore, BigDecimal balanceBack, BigDecimal balanceDifference) {
        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setMemberId(memberId);
        balanceRecord.setCurrency(currency);
        balanceRecord.setBalanceType(balanceType);
        balanceRecord.setFundsType(fundsType);
        balanceRecord.setBalanceBefore(balanceBefore);
        balanceRecord.setBalanceBack(balanceBack);
        balanceRecord.setBalanceDifference(balanceDifference);
        balanceRecord.setCreateTime(new Date());
        balanceRecord.setDataClassification(3);
        balanceRecordMapper.insert(balanceRecord);
    }

    @Override
    public Response getHistoryEntrust(String member, String pairsName, Integer pageNum, Integer pageSize) {
        QueryWrapper<EntrustHistory> wrapper = new QueryWrapper<EntrustHistory>();
        wrapper.eq("member", member);
        if (Strings.isNotBlank(pairsName)) {
            wrapper.eq("pairs_name", pairsName);
        }
        wrapper.orderByDesc("create_time");
        // entrustHistoryMapper.selectList(wrapper);

        IPage<EntrustHistory> page = entrustHistoryMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Response.success(page);
    }

    @Override
    public List<Entrust> getEntrustList(String member, String pairsName) {
        QueryWrapper<Entrust> wrapper = new QueryWrapper<Entrust>();
        wrapper.eq("member", member);
        if (Strings.isNotBlank(pairsName)) {
            wrapper.eq("pairs_name", pairsName);
        }
        wrapper.orderByDesc("create_time");
        return entrustMapper.selectList(wrapper);
    }

    @Override
    public boolean closeEntrust(String entrustId) {
        QueryWrapper<Entrust> wrapper = new QueryWrapper<Entrust>();
        wrapper.eq("id", entrustId);
        wrapper.in("state", EntrustConstant.Order_State.CREATE, EntrustConstant.Order_State.MATCH);
        Entrust entrust = entrustMapper.selectOne(wrapper);
        if (entrust == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_HAS_BEEN_FILLED_OR_DOES_NOT_EXIST.getMessage());
        }
        List<String> list = redisRepository.keyLikeValue(CacheConstants.ENTRUST_ORDER_MATCH_KEY
                + entrust.getEntrustType() + CacheConstants.SPLIT + entrust.getPairsName(), entrustId);
        if (list.isEmpty()) {
            throw new BusinessException(AjaxResultEnum.ORDER_HAS_BEEN_FILLED_OR_DOES_NOT_EXIST.getMessage());
        }
        entrust.setUld("DOWN");
        Pairs pairs = pairsMapper.selectById(entrust.getPairs());
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock
                .lock(CacheConstants.MEMBER_ENTRUST_PAIRS_KEY + entrust.getMember(), 5000, 50, 100);
        if (lock_coin) {
            if (pairs.getType().equals(CoinConstant.Coin_Type.MAIN_COIN)) {
                matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
            } else {
                matchProducer.putProjectEntrustMatch(JSONObject.toJSONString(entrust));
            }
            redisDistributedLock
                    .releaseLock(CacheConstants.MEMBER_ENTRUST_PAIRS_KEY + entrust.getMember());
        } else {
            throw new BusinessException(AjaxResultEnum.FREQUENT_OPERATIONS.getMessage());
        }
        return true;
    }

    @Override
    public boolean test() {
        List<String> members = new ArrayList<String>();
        members.add("df4b6e5089d6447a8640d25c2286feb2");

        for (String member : members) {
            QueryWrapper<Entrust> wrapper = new QueryWrapper<Entrust>();
            wrapper.eq("member", member);
            List<Entrust> list = entrustMapper.selectList(wrapper);
            for (Entrust entrust : list) {
                entrustMapper.deleteById(entrust.getId());

            }

//			List<String> listOpenUp = redisRepository.keyLikeValue(
//					CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + "JESUS/USDT",
//					member);
//			List<String> listDown = redisRepository.keyLikeValue(
//					CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.SELL + CacheConstants.SPLIT + "JESUS/USDT",
//					member);
//			for(String str:listOpenUp) 
//			{
//				Entrust entrust = JSONObject.parseObject(str, Entrust.class);
//				redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + "JESUS/USDT", str);
//				entrustMapper.deleteById(entrust.getId());
//			}
//			
//			
//			
//			for(String str:listDown) 
//			{
//				Entrust entrust = JSONObject.parseObject(str, Entrust.class);
//				redisRepository.zsetRemove(CacheConstants.ENTRUST_ORDER_MATCH_KEY + EntrustConstant.Entrust_Type.BUY + CacheConstants.SPLIT + "JESUS/USDT", str);
//				entrustMapper.deleteById(entrust.getId());
//			}
        }

        return false;
    }

    @Override
    public Object setEntrustBackstage(String entrustId) {
        QueryWrapper<Entrust> wrapper = new QueryWrapper<Entrust>();
        wrapper.eq("id", entrustId);
        wrapper.in("state", EntrustConstant.Order_State.CREATE, EntrustConstant.Order_State.MATCH);
        Entrust entrust = entrustMapper.selectOne(wrapper);
        if (null != entrust) {
            // 手续费，费率
            BigDecimal tradeFeeActive;
            if (entrust.getTradeRate() != null) {
                tradeFeeActive = entrust.getCount().multiply(entrust.getTradeRate());
            } else {
                tradeFeeActive = entrust.getCount().multiply(new BigDecimal("0.003"));
            }
            if (entrust.getTradeFee() != null) {
                entrust.setTradeFee(entrust.getTradeFee().add(tradeFeeActive));
            } else {
                entrust.setTradeFee(tradeFeeActive);
            }

            Entrust entrust2 = new Entrust();
            entrust2.setUld("UP");
            entrust2.setId("robotIdEntrust");
            entrust2.setCount(entrust.getCount());
            entrust2.setPairsName(entrust.getPairsName());
            entrust2.setPrice(entrust.getPrice());
            entrust2.setMember("robotMemberId");
            entrust2.setSort(10000000000L);

            String entrustType;
            if (entrust.getEntrustType().equals(EntrustConstant.Entrust_Type.SELL)) {
                entrust2.setEntrustType(EntrustConstant.Entrust_Type.BUY);
                entrustType = "BUY";
            } else {
                entrust2.setEntrustType(EntrustConstant.Entrust_Type.SELL);
                entrustType = "SELL";
            }

            redisRepository.zsetAdd(
                    CacheConstants.ENTRUST_ORDER_MATCH_KEY + entrustType + CacheConstants.SPLIT
                            + entrust2.getPairsName(),
                    JSONObject.toJSONString(entrust2), entrust2.getPrice().doubleValue());
            logger.info("被插入的数据====>>>" + JSONObject.toJSONString(entrust2));
            matchProducer.putMainEntrustMatch(JSONObject.toJSONString(entrust));
            return "SUCCESS";
        }
        return "FAIL";
    }

    public static void main(String[] args) {
        long seconds = Duration.between(LocalDateTime.now(), LocalDate.now().atTime(23, 59, 59)).getSeconds();

        System.out.println(seconds);

        for (int i = 0; i < 50; i++) {
            double next = RandomUtils.nextF6(0.000900, 0.000650);

            System.out.println(6.360095 * next);
        }

    }

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Object createKlineData(Integer number, Boolean isClear, String coin) {


        List<CurrencyControls> currencyControlsList = currencyControlsMapper.findCurrencyControlsList();
        long seconds = Duration.between(LocalDateTime.now(), LocalDate.now().atTime(23, 59, 59)).getSeconds();

        BigDecimal firstOpen = null;
        for (CurrencyControls currencyControls : currencyControlsList) {
            List<KlineData> klineDataList = new ArrayList<>();
            // 币起始价格
            BigDecimal startingPrice = currencyControls.getStartingPrice();
            // 币结束价格
            BigDecimal finalPrice = currencyControls.getFinalPrice();
            // 最大交易量
            BigDecimal tradingVolumeMax = currencyControls.getTradingVolumeMax();
            // 最小交易量
            BigDecimal tradingVolumeMin = currencyControls.getTradingVolumeMin();

            firstOpen = startingPrice;
            //根据最后一条数据的时间去生成 达到累计的效果
            LocalDateTime localDateTime = klineDataMapper.selectLastTime(currencyControls.getTokenCur());
            LocalDateTime now = LocalDateTime.now();
            if (localDateTime == null || now.isAfter(localDateTime)) {
                localDateTime = now;
            }
            // 如果有传入参数，则按照指定的数量生成
            if (null != number) {
                seconds = number;
            }

            //涨跌： 1 涨 2 跌
            int slice = RandomUtils.next(1, 2);
            //持续次数
            int repeat = RandomUtils.next(1, 3);

            int index = 0;

            for (int i = 0; i < seconds; i++) {
                localDateTime = localDateTime.plusSeconds(1);

                // 根据设置的起始价,结束价格   生成K线秒级数据的当前价格
                // Double price = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());

                Double openPrice = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());
                Double closePrice = RandomUtils.next(finalPrice.doubleValue(), startingPrice.doubleValue());

                // 随机生成最大最小交易量
                double volume = RandomUtils.next(tradingVolumeMax.doubleValue(), tradingVolumeMin.doubleValue());

                int count = RandomUtils.next(2, 11);

                if (repeat == index) {
                    // 到了
                    index = 0;
                    slice = RandomUtils.next(1, 2);
                    repeat = RandomUtils.next(1, 3);
//                    System.out.println((slice==1?"涨":"跌") + "    持续次数=>" + repeat);
                }
                BigDecimal n = startingPrice.multiply(BigDecimal.valueOf(0.0009));

                if (slice == 1) {
                    startingPrice = startingPrice.add(n);
                } else {
                    startingPrice = startingPrice.subtract(n);
                }
                index++;
                this.saveKlineData(currencyControls, klineDataList, localDateTime, startingPrice, openPrice, closePrice, volume, count);
            }
//            this.batchSaveKineSeconds(klineDataList);
            if (!klineDataList.isEmpty()) {
                List<List<KlineData>> m1 = split(klineDataList, 60);
                saveMinutesKlineData(m1, "1m", firstOpen);
                List<List<KlineData>> m5 = split(klineDataList, 300);
                saveMinutesKlineData(m5, "5m", firstOpen);
                List<List<KlineData>> m15 = split(klineDataList, 900);
                saveMinutesKlineData(m15, "15m", firstOpen);
                List<List<KlineData>> m30 = split(klineDataList, 1800);
                saveMinutesKlineData(m30, "30m", firstOpen);
                List<List<KlineData>> h1 = split(klineDataList, 3600);
                saveMinutesKlineData(h1, "1h", firstOpen);
                List<List<KlineData>> h4 = split(klineDataList, 14400);
                saveMinutesKlineData(h4, "4h", firstOpen);
                buildKlineData("1d", klineDataList, firstOpen);
            }
        }
        return null;
    }

    private void batchSaveKineSeconds(List<KlineData> klineDataList) {
        List<List<KlineData>> list = split(klineDataList, 500);
        for (List<KlineData> data : list) {
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            data.forEach(d -> klineDataMapper.insert(d));
            sqlSession.commit();
            sqlSession.clearCache();
            sqlSession.close();
        }

    }

    private void saveKlineData(CurrencyControls currencyControls, List<KlineData> klineDataList, LocalDateTime localDateTime, BigDecimal price, Double openPrice, Double closePrice, double volume, int count) {
        KlineData klineData;
        klineData = new KlineData();
        klineData.setTokenCur(currencyControls.getTokenCur());
        klineData.setOpen(BigDecimal.valueOf(openPrice));
        klineData.setClose(BigDecimal.valueOf(closePrice));
        klineData.setPrice(price);
        klineData.setVolume(BigDecimal.valueOf(volume));
        klineData.setCount(count);
        klineData.setCreateTime(localDateTime);
        klineData.setVol(BigDecimal.valueOf(volume).multiply(BigDecimal.valueOf(count)));
        klineDataList.add(klineData);
        klineDataMapper.insert(klineData);
    }


    private void saveMinutesKlineData(List<List<KlineData>> lists, String timeType, BigDecimal firstOpen) {
        for (List<KlineData> klineDataList : lists) {
            buildKlineData(timeType, klineDataList, firstOpen);
        }
    }

    Double lastClose = null;

    private void buildKlineData(String timeType, List<KlineData> klineDataList, BigDecimal firstOpen) {
        KlineData klineData1 = klineDataList.get(klineDataList.size() - 1);
        LocalDateTime createTime = klineData1.getCreateTime();
        klineDataList.sort(Comparator.comparing(KlineData::getPrice));

        // 最低价(本轮交易（分，时，天）)
        KlineData klineData = klineDataList.get(0);
        // 最高价
        KlineData klineData2 = klineDataList.get(klineDataList.size() - 1);

        StagePrice stagePrice = new StagePrice();
        stagePrice.setTokenCur(klineData.getTokenCur());

        stagePrice.setVolume(klineData2.getVolume());
        stagePrice.setVol(klineData2.getVol());
        stagePrice.setCount(klineData2.getCount());

        if (lastClose == null) {
            stagePrice.setOpen(firstOpen);
        } else {
            stagePrice.setOpen(BigDecimal.valueOf(lastClose));
        }
        // 闭盘价（上一分钟交易的平均数）
        double avg = klineDataList.stream().mapToDouble(value -> value.getPrice().doubleValue()).average().getAsDouble();
        stagePrice.setClose(BigDecimal.valueOf(avg));


//        stagePrice.setHigh(stagePrice.getClose());
//        stagePrice.setLow(klineData.getPrice());

        // 设置最高最低价格
        if (stagePrice.getOpen().doubleValue() > stagePrice.getClose().doubleValue()) {
            //这里是跌了 下面是收盘 上面是开盘
            double next = RandomUtils.nextF6(0.009900, 0.001650);
//            double next = 0.0025;

            BigDecimal multiply = stagePrice.getOpen().multiply(BigDecimal.valueOf(next));
            stagePrice.setHigh(stagePrice.getOpen().add(multiply));
            next = RandomUtils.nextF6(0.009900, 0.001650);
            BigDecimal multiply1 = stagePrice.getClose().multiply(BigDecimal.valueOf(next));
            stagePrice.setLow(stagePrice.getClose().subtract(multiply1));
        } else {
//            double next = 0.0025;
            double next = RandomUtils.nextF6(0.009900, 0.001650);
            BigDecimal multiply = stagePrice.getClose().multiply(BigDecimal.valueOf(next));
            stagePrice.setHigh(stagePrice.getClose().add(multiply));
            next = RandomUtils.nextF6(0.009900, 0.001650);
            BigDecimal multiply1 = stagePrice.getOpen().multiply(BigDecimal.valueOf(next));
            stagePrice.setLow(stagePrice.getOpen().subtract(multiply1));
        }


        stagePrice.setTimeType(timeType);
        stagePrice.setCreateTime(createTime);
        stagePriceMapper.insert(stagePrice);

        lastClose = avg;
    }


    /**
     * 拆分集合
     *
     * @param <T>           泛型对象
     * @param resList       需要拆分的集合
     * @param subListLength 每个子集合的元素个数
     * @return 返回拆分后的各个集合组成的列表
     * 代码里面用到了guava和common的结合工具类
     **/
    public static <T> List<List<T>> split(List<T> resList, int subListLength) {
        if (CollectionUtils.isEmpty(resList) || subListLength <= 0) {
            return Lists.newArrayList();
        }
        List<List<T>> ret = Lists.newArrayList();
        int size = resList.size();
        if (size <= subListLength) {
            // 数据量不足 subListLength 指定的大小
            ret.add(resList);
        } else {
            int pre = size / subListLength;
            int last = size % subListLength;
            // 前面pre个集合，每个大小都是 subListLength 个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = Lists.newArrayList();
                for (int j = 0; j < subListLength; j++) {
                    itemList.add(resList.get(i * subListLength + j));
                }
                ret.add(itemList);
            }
            // last的进行处理
            if (last > 0) {
                List<T> itemList = Lists.newArrayList();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * subListLength + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    private void extractCurrencyExchange(String member, String currencyName, BigDecimal balance, Integer type, Integer balanceType, Integer fundsType) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member,
                5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            wrapper.eq("user_id", member);
            wrapper.eq("currency", currencyName);
            Balance userBalance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = userBalance.getAssetsBalance();
            BigDecimal assetsBlockedBalance = userBalance.getAssetsBlockedBalance();
            if (type == 1) {
                userBalance.setAssetsBlockedBalance(userBalance.getAssetsBlockedBalance().subtract(balance));
            } else {
                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
            }

            balanceMapper.updateById(userBalance);

            if (type == 1) {
                saveBalanceRecord(member, currencyName, balanceType, fundsType, assetsBlockedBalance, userBalance.getAssetsBlockedBalance(), balance);
            } else {
                saveBalanceRecord(member, currencyName, balanceType, fundsType, assetsBalance, userBalance.getAssetsBalance(), balance);
            }

            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
        } else {
        }
    }

}
