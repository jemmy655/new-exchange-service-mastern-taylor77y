package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.data.mapper.*;
import cn.xa87.data.service.PerpetualContractOrderService;
import cn.xa87.model.*;
import cn.xa87.vo.PerpetualContractOrderVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PerpetualContractOrderServiceImpl extends ServiceImpl<PerpetualContractOrderMapper, PerpetualContractOrder>
        implements PerpetualContractOrderService {
    public static final ConcurrentHashMap<String, Object> match_entrust = new ConcurrentHashMap<String, Object>();

    @Autowired
    private PerpetualContractOrderMapper perpetualContractOrderMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private LeverMapper leverMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Override
    public boolean setContractOrder(PerpetualContractOrderVO perpetualContractOrder) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_PAIRS_KEY + perpetualContractOrder.getMemberId() + perpetualContractOrder.getPairsName(),
                5000, 50, 100);
        if (lock_coin) {
            Lever lever = leverMapper.selectById(perpetualContractOrder.getLeverId());
            if (lever==null){
                throw new BusinessException("当前传递杠杆Id，数据库不存在");
            }
            // 资金扣除
            BigDecimal takeFee = openBalance(perpetualContractOrder);

            perpetualContractOrder.setBPrice(perpetualContractOrder.getKPrice());//平仓价格
            perpetualContractOrder.setOrderState("POSITIONS"); //持仓POSITIONS  已平仓 closeOut
            perpetualContractOrder.setLeverDesc(lever.getPairsName());
            perpetualContractOrder.setLeverNum(lever.getLever());
            perpetualContractOrder.setProfit(new BigDecimal(0.00));

            PerpetualContractOrder perpetual=new PerpetualContractOrder(
                    perpetualContractOrder.getMemberId(),
                    perpetualContractOrder.getPairsName(),
                    perpetualContractOrder.getKPrice(),
                    perpetualContractOrder.getBPrice(),
                    perpetualContractOrder.getAmount(),
                    perpetualContractOrder.getMargin(),
                    perpetualContractOrder.getMatchFee(),
                    perpetualContractOrder.getProfit(),
                    perpetualContractOrder.getIsWin(),
                    perpetualContractOrder.getIsControl(),
                    perpetualContractOrder.getControlPrice(),
                    perpetualContractOrder.getLeverId(),
                    perpetualContractOrder.getLeverNum(),
                    perpetualContractOrder.getLeverDesc(),
                    perpetualContractOrder.getOrderState(),
                    perpetualContractOrder.getTradeType(),
                    perpetualContractOrder.getContractHands(),
                    new Date(),
                    null
            );
            perpetualContractOrderMapper.insert(perpetual);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_PAIRS_KEY + perpetualContractOrder.getMemberId() + perpetualContractOrder.getPairsName());
        }
        return false;
    }

    @Override
    public Map<Object,Object>  getContractOrder(String id) {
        DecimalFormat df = new DecimalFormat("0.00%");
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("id", id);
        PerpetualContractOrder perpetual=perpetualContractOrderMapper.selectOne(wrapperMain);
        if (perpetual==null){
            throw new BusinessException("请检查订单id是否正确");
        }
        Map<Object,Object> map = JSON.parseObject(JSON.toJSONString(perpetual), Map.class);
        if (perpetual.getProfit().compareTo(new BigDecimal(0.00))==1) {
            map.put("profitUp",df.format(perpetual.getProfit().divide(perpetual.getAmount())));
        } else {
            map.put("profitUp","0.00");
        }
        return map;
    }

    @Override
    public boolean setOrderMatch(String id) {
        return false;
    }

    @Override
    public boolean setAllContractMatch(String member, String pairsName) {
        return false;
    }

    @Override
    public List<Object> getWarehouses(String memberId, String pairsName,String price) {
        List<Object> list=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00%");
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("pairs_name", pairsName);
        wrapperMain.eq("order_state", "POSITIONS"); //状态为持仓
        wrapperMain.eq("member_id", memberId);
        List<PerpetualContractOrder> balanceMain = perpetualContractOrderMapper.selectList(wrapperMain);
        for (PerpetualContractOrder o:balanceMain) {
            double random = Math.random()*100;
            JSONObject josnInfo = new JSONObject();
            BigDecimal nowPrice=new BigDecimal(price);
            //傻逼操作 控盘
            BigDecimal  pri = new BigDecimal(random);
            if(o.getIsControl() == 1) {//控盘
                if (o.getIsWin() == 0) {//输
                    nowPrice = o.getProfit().add(nowPrice.add(pri));
                } else {
                    nowPrice = nowPrice.add(pri);
                    o.setControlPrice(nowPrice);
                }
            }
            //计算收益
            BigDecimal profit=o.getKPrice().subtract(nowPrice);
            o.setBPrice(nowPrice);
            o.setProfit(profit);
            perpetualContractOrderMapper.updateById(o);

            josnInfo.put("price",o.getKPrice()); //成本价
            josnInfo.put("time",o.getCreateTime());//创建时间
//            if (o.getProfit().compareTo(new BigDecimal(0.00))==1) {
//                josnInfo.put("profitUp", df.format(o.getProfit().divide(o.getAmount())));//收益率
//            }else {
//                //应该随机
//                josnInfo.put("profitUp","0.00%");
//            }
            BigDecimal profitUp = o.getProfit().divide(o.getAmount());
            josnInfo.put("profitUp", df.format(profitUp));//收益率
            josnInfo.put("amount",o.getAmount());
            josnInfo.put("id",o.getId());
            josnInfo.put("tradeType",o.getTradeType());

            list.add(josnInfo);
        }
        return list;
    }

    @Override
    public Response getHistoryOrders(String member, String pairsName, TokenOrderConstant.Order_State orderState, Integer pageNum, Integer pageSize) {
        return null;
    }

    private BigDecimal openBalance(PerpetualContractOrderVO perpetualContractOrderVO) {
        // 手续费
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + perpetualContractOrderVO.getMemberId(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", perpetualContractOrderVO.getCoinName());
            wrapperMain.eq("user_id", perpetualContractOrderVO.getMemberId());
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain==null){
                throw new BusinessException(perpetualContractOrderVO.getCoinName().concat("不足"));
            }
            //要扣除的价格
            BigDecimal deductPrice=perpetualContractOrderVO.getMargin().add(perpetualContractOrderVO.getMatchFee());
            try {
                if (balanceMain.getAssetsBalance().compareTo(deductPrice) == -1) {
                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_MARGIN_BALANCE.getMessage());
                }
            } catch (Exception e) {
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_MARGIN_BALANCE.getMessage());
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balanceMain.getAssetsBlockedBalance();

            // 冻结用户余额，保证金 = 可用资金 - 保证金 -手续费
            BigDecimal subtract = balanceMain.getAssetsBalance().subtract(deductPrice);
            balanceMain.setAssetsBalance(subtract);
            balanceMain.setAssetsBlockedBalance(balanceMain.getAssetsBlockedBalance().add(perpetualContractOrderVO.getMargin()));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());

            // 资金减少记录
            saveBalanceRecord(perpetualContractOrderVO.getMemberId(),perpetualContractOrderVO.getCoinName(),10,1,assetsBalance,balanceMain.getAssetsBalance(),subtract);

            // 冻结日志
            saveBalanceRecord(perpetualContractOrderVO.getMemberId(),perpetualContractOrderVO.getCoinName(),13,2,assetsBlockedBalance,balanceMain.getAssetsBlockedBalance(),perpetualContractOrderVO.getMargin());

        } else {
            openBalance(perpetualContractOrderVO);
        }
        //不使用
        return new BigDecimal(0.00);

    }


    private void saveBalanceRecord(String memberId,String currency,Integer balanceType,Integer fundsType,BigDecimal balanceBefore, BigDecimal balanceBack,BigDecimal balanceDifference){
        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setMemberId(memberId);
        balanceRecord.setCurrency(currency);
        balanceRecord.setBalanceType(balanceType);
        balanceRecord.setFundsType(fundsType);
        balanceRecord.setBalanceBefore(balanceBefore);
        balanceRecord.setBalanceBack(balanceBack);
        balanceRecord.setBalanceDifference(balanceDifference);
        balanceRecord.setCreateTime(new Date());
        balanceRecord.setDataClassification(1);
        balanceRecordMapper.insert(balanceRecord);
    }
}
