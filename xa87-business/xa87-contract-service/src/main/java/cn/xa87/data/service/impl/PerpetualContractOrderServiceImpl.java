package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.data.mapper.*;
import cn.xa87.data.service.PerpetualContractOrderService;
import cn.xa87.model.*;
import cn.xa87.vo.PerpetualContractOrderVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
                    null,
                    perpetualContractOrder.getContractHands()
            );
            perpetualContractOrderMapper.insert(perpetual);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_PAIRS_KEY + perpetualContractOrder.getMemberId() + perpetualContractOrder.getPairsName());
        }
        return true;
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
        if (perpetual.getProfit().compareTo(new BigDecimal(0))==0) { //a = b true
            map.put("profitUp","0.00%");
        } else {
            map.put("profitUp",df.format(perpetual.getProfit().divide(perpetual.getAmount(),3,
                    BigDecimal.ROUND_HALF_UP)));
        }
        return map;
    }

    @Override
    public boolean setOrderMatch(String id, String coinName,String price) {
        PerpetualContractOrderVO vo=new PerpetualContractOrderVO();
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("id", id);
        PerpetualContractOrder balanceMain = perpetualContractOrderMapper.selectOne(wrapperMain);
        if (balanceMain==null){
            throw new BusinessException("为空");
        }

        vo.setCoinName(coinName);
        vo.setMemberId(balanceMain.getMemberId());
        vo.setMargin(balanceMain.getMargin());
        //退回金额
        //修改状态为已平仓 ClLOSEOUT
        BigDecimal margin= balanceMain.getAmount().add(balanceMain.getProfit());//扣除金额
        updateBalance(vo,margin);
        balanceMain.setOrderState("ClLOSEOUT");
        balanceMain.setMargin(new BigDecimal(0.00000));//保证金减少并且为0
        balanceMain.setBPrice(new BigDecimal(price));//结束价格
        balanceMain.setUsableControlHands(new BigDecimal(0));
        balanceMain.setSettleTime(new Date());
        perpetualContractOrderMapper.updateById(balanceMain);
        return true;
    }

    @Override
    public boolean setAllContractMatch(String memberId, String coinName, String pairsName,String price) {
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("pairs_name", pairsName);
        wrapperMain.eq("order_state", "POSITIONS"); //状态为持仓
        wrapperMain.eq("member_id", memberId);
        List<PerpetualContractOrder> balanceMain = perpetualContractOrderMapper.selectList(wrapperMain);
        for (PerpetualContractOrder b:balanceMain) {
            PerpetualContractOrderVO vo=new PerpetualContractOrderVO();
            //退回金额
            vo.setCoinName(coinName);
            vo.setMemberId(b.getMemberId());
            vo.setMargin(b.getMargin());
            //退回金额
            //修改状态为已平仓 ClLOSEOUT
            BigDecimal margin= b.getAmount().add(b.getProfit());//扣除金额
            updateBalance(vo,margin);
            b.setOrderState("ClLOSEOUT");
            b.setMargin(new BigDecimal(0.00000));//保证金减少并且为0
            b.setBPrice(new BigDecimal(price));//结束价格
            b.setUsableControlHands(new BigDecimal(0));
            b.setSettleTime(new Date());
            perpetualContractOrderMapper.updateById(b);
        }
        return true;
    }

    @Override
    public List<Object> getWarehouses(String memberId, String pairsName,String price) {
        List<Object> list=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00%");
        double random = Math.round((0.001 + (0.01 - 0.001) * Math.random()) * 100000.0) / 100000.0;//生成0.01  - 0.001的随机数
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("pairs_name", pairsName);
        wrapperMain.eq("order_state", "POSITIONS"); //状态为持仓
        wrapperMain.eq("member_id", memberId);
        List<PerpetualContractOrder> balanceMain = perpetualContractOrderMapper.selectList(wrapperMain);
        for (PerpetualContractOrder o:balanceMain) {
            JSONObject josnInfo = new JSONObject();
            BigDecimal nowPrice=new BigDecimal(price);

            // 控盘
            BigDecimal  pri = new BigDecimal(random);
            //计算收益率
            BigDecimal profitUp=new BigDecimal(0.00);
            BigDecimal profit=new BigDecimal(0);
            if(o.getIsControl() == 1) {//控盘
                profitUp=o.getProfit().divide(o.getAmount(),3,BigDecimal.ROUND_HALF_UP);
                if (profitUp.compareTo(new BigDecimal(1))>0 || profitUp.compareTo(new BigDecimal(-1))<0){  //100% 1 -1
                    o.setControlPrice(o.getControlPrice());
                    profit=o.getProfit();
                    nowPrice=o.getKPrice().add(new BigDecimal(1000).multiply(new BigDecimal(1)).multiply(profitUp)); //加上一手的收益
                }else {
                    if (o.getIsWin() == 0) { // 跌
                        profitUp = profitUp.subtract(pri);
                    } else { // 赢
                        //收益为负 说明 当前价 大于 成本价  涨了 将当前价减少
                        profitUp = profitUp.add(pri);
                    }
                    o.setControlPrice(profitUp);
                    profit=o.getAmount().multiply(profitUp);
                    nowPrice=o.getKPrice().add(new BigDecimal(1000).multiply(new BigDecimal(1)).multiply(profitUp)); //加上一手的收益
                }
            }
            //计算收益
            if(o.getTradeType().equals("OPEN_DOWN")){//看跌 跌就是收益
                if(o.getIsControl() == 0) {//控盘
                    profit = o.getKPrice().subtract(nowPrice).multiply(o.getUsableControlHands());
                    profitUp=profit.divide(o.getAmount(),3,BigDecimal.ROUND_HALF_UP);
                }
            }else {
                if(o.getIsControl() == 0) {// 涨就是收益
                    profit = o.getKPrice().subtract(nowPrice).multiply(o.getUsableControlHands()).negate();
                    profitUp=profit.divide(o.getAmount(),3,BigDecimal.ROUND_HALF_UP);
                }
            }

            o.setBPrice(nowPrice);
            o.setProfit(profit);
            perpetualContractOrderMapper.updateById(o);

            josnInfo.put("price",o.getKPrice()); //成本价
            josnInfo.put("time",o.getCreateTime());//创建时间
            josnInfo.put("profitUp", df.format(profitUp));//收益率 百分号
            if (profitUp.compareTo(new BigDecimal(0))>0){
                josnInfo.put("profitUp", "+"+df.format(profitUp));//收益率 百分号
            }else {
                josnInfo.put("profitUp", df.format(profitUp));//收益率 百分号
            }
            josnInfo.put("amount",o.getAmount());
            josnInfo.put("id",o.getId());
            josnInfo.put("tradeType",o.getTradeType());
            list.add(josnInfo);
        }
        return list;
    }
    @Override
    public Response getHistoryOrders(String member, String pairsName, TokenOrderConstant.Order_State orderState, Integer pageNum, Integer pageSize) {
        List<Object> list=new LinkedList<>();
        DecimalFormat df = new DecimalFormat("0.00%");
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("order_state", "ClLOSEOUT"); //状态为持仓
        wrapperMain.eq("member_id", member);
        wrapperMain.orderByDesc("create_time");
        List<PerpetualContractOrder> balanceMain = perpetualContractOrderMapper.selectList(wrapperMain);
        for (PerpetualContractOrder b:balanceMain) {
            Map<Object,Object> map = JSON.parseObject(JSON.toJSONString(b), Map.class);

            if (b.getProfit().compareTo(new BigDecimal(0.00))==1) {
                map.put("profitUp",df.format(b.getProfit().divide(b.getAmount(),3,
                        BigDecimal.ROUND_HALF_UP)));
            } else {
                map.put("profitUp","0.00%");
            }
            list.add(map);
        }
        return Response.success(list);
    }

    @Override
    public boolean setContractOrderSell(PerpetualContractOrderVO perpetualContractOrder) {
        QueryWrapper<PerpetualContractOrder> wrapperMain = new QueryWrapper<PerpetualContractOrder>();
        wrapperMain.eq("pairs_name", perpetualContractOrder.getPairsName());
        wrapperMain.eq("order_state", "POSITIONS"); //状态为持仓
        wrapperMain.eq("member_id", perpetualContractOrder.getMemberId());
        List<PerpetualContractOrder> balanceMain = perpetualContractOrderMapper.selectList(wrapperMain);
        if (balanceMain==null){
            throw new BusinessException("没有持仓手数可用");
        }
        if(perpetualContractOrder.getTradeType().equals("CLOSE_DOWN")){perpetualContractOrder.setTradeType("OPEN_DOWN");}
        if(perpetualContractOrder.getTradeType().equals("CLOSE_UP")){perpetualContractOrder.setTradeType("OPEN_UP");}
        List<PerpetualContractOrder> b=balanceMain.stream().filter(a -> a.getTradeType().equals(perpetualContractOrder.getTradeType())).collect(Collectors.toList());
        if (new BigDecimal(b.size()).compareTo(perpetualContractOrder.getContractHands())==-1){
            throw new BusinessException("持仓手数小于要扣除手数");
        }
        for (PerpetualContractOrder per:b) {
            if (per.getContractHands().compareTo(new BigDecimal(0))==0){ //已全部扣除
                break;
            }
            if (per.getContractHands().compareTo(perpetualContractOrder.getContractHands())==0){ //等于要扣除的手数
                //退回金额
                perpetualContractOrder.getContractHands().subtract(per.getContractHands());//减少手数
                //修改状态为已平仓 ClLOSEOUT
                BigDecimal margin= per.getAmount().add(per.getProfit());//扣除金额
                updateBalance(perpetualContractOrder,margin);
                per.setOrderState("ClLOSEOUT");
                per.setMargin(new BigDecimal(0.00000));//保证金减少并且为0
                per.setBPrice(perpetualContractOrder.getKPrice());//结束价格
                per.setUsableControlHands(new BigDecimal(0));
                per.setSettleTime(new Date());
                perpetualContractOrderMapper.updateById(per);
                break;
            }
            if (per.getContractHands().compareTo(perpetualContractOrder.getContractHands())==-1){ //小于要扣除的手数
                //退回金额
                perpetualContractOrder.getContractHands().subtract(per.getContractHands());//减少手数
                //修改状态为已平仓 ClLOSEOUT
                BigDecimal margin= perpetualContractOrder.getAmount().add(per.getProfit());//扣除金额
                updateBalance(perpetualContractOrder,margin);//还有持仓手数 不减少 收益
                per.setOrderState("ClLOSEOUT");
                per.setBPrice(perpetualContractOrder.getKPrice());
                per.setMargin(new BigDecimal(0.00000));//保证金减少并且为0
                per.setUsableControlHands(new BigDecimal(0));
                per.setSettleTime(new Date());
                perpetualContractOrderMapper.updateById(per);
            }
            if (per.getContractHands().compareTo(perpetualContractOrder.getContractHands())==1){ //大于要扣除的手数

                //退回金额
                perpetualContractOrder.getContractHands().subtract(per.getContractHands());//减少手数
                //修改状态为已平仓 ClLOSEOUT
                BigDecimal a=perpetualContractOrder.getContractHands().multiply(new BigDecimal(1000));//要减少的金额

                updateBalance(perpetualContractOrder,a);//还有持仓手数 不减少 收益

                per.setBPrice(perpetualContractOrder.getKPrice());
                per.setUsableControlHands(perpetualContractOrder.getContractHands().subtract(per.getUsableControlHands()));
                per.setSettleTime(new Date());
                perpetualContractOrderMapper.updateById(per);
                break;
            }

        }
        return true;
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
                throw new BusinessException("用户账号不存在");
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
            saveBalanceRecord(perpetualContractOrderVO.getMemberId(),perpetualContractOrderVO.getCoinName(),10,1,
                    assetsBalance,balanceMain.getAssetsBalance(),subtract);

            // 冻结日志
            saveBalanceRecord(perpetualContractOrderVO.getMemberId(),perpetualContractOrderVO.getCoinName(),13,2,assetsBlockedBalance,balanceMain.getAssetsBlockedBalance(),perpetualContractOrderVO.getMargin());

        } else {
            openBalance(perpetualContractOrderVO);
        }
        //不使用
        return new BigDecimal(0.00);

    }

    /**
     *  退回金额
     * @param contractOrder
     * @param margin  要退回的金额
     */
    private void updateBalance(PerpetualContractOrderVO contractOrder, BigDecimal margin) {
        // 要回到账户余额 = 金额 + 收益
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMemberId(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
            wrapperActive.eq("currency", contractOrder.getCoinName());
            wrapperActive.eq("user_id", contractOrder.getMemberId());
            Balance balanceActive = balanceMapper.selectOne(wrapperActive);

            BigDecimal assetsBalance = balanceActive.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balanceActive.getAssetsBlockedBalance();

            BigDecimal balance = balanceActive.getAssetsBalance().add(margin);
            balanceActive.setAssetsBalance(balance);

            balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(contractOrder.getMargin()));
            balanceMapper.updateById(balanceActive);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceActive.getUserId());

            // 平仓可用增加
            saveBalanceRecord(contractOrder.getMemberId(),contractOrder.getCoinName(),11,2,assetsBalance,balanceActive.getAssetsBalance(),balance);

            // 平仓冻结减少
            saveBalanceRecord(contractOrder.getMemberId(),contractOrder.getCoinName(),12,1,assetsBlockedBalance,balanceActive.getAssetsBlockedBalance(),margin);
        } else {
            updateBalance(contractOrder, margin);
        }
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
