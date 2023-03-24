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
import cn.xa87.data.product.MatchProducer;
import cn.xa87.data.service.ContractOrderService;
import cn.xa87.data.service.WarehouseService;
import cn.xa87.model.*;
import cn.xa87.po.BrokerageRecordPo;
import cn.xa87.vo.ContractDeliveryVo;
import cn.xa87.vo.PerpetualContractOrderVO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContractOrderServiceImpl extends ServiceImpl<ContractOrderMapper, ContractOrder>
        implements ContractOrderService {
    public static final ConcurrentHashMap<String, Object> match_entrust = new ConcurrentHashMap<String, Object>();
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private ContractMulMapper contractMulMapper;
    @Autowired
    private LeverMapper leverMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private MatchProducer matchProducer;
    @Autowired
    private SecondsBetLogMapper secondsBetLogMapper;
    @Autowired
    private SecondsConfigMapper secondsConfigMapper;
    @Autowired
    private MemberCurrencyConfigMapper memberCurrencyConfigMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;


    public boolean setContractOrderNew(ContractOrder contractOrder) {
        BigDecimal nowPrice=contractOrder.getPrice();
        // 判读交易对后台是否配置
        ContractMul contractMul = contractMulMapper.selectById(contractOrder.getContractMulId());
        if (!contractMul.getPairsName().equals(contractOrder.getPairsName())) {
            throw new BusinessException(AjaxResultEnum.TRADING_PAIR_ERROR.getMessage());
        }
        // 合约乘数 * 杠杆手数
        BigDecimal coinNum = contractMul.getContractMul().multiply(contractOrder.getContractHands());
        // 杠杆倍数
        Lever lever = leverMapper.selectById(contractOrder.getLeverId());
        // 保证金 = 合约乘数 * （杠杆手数 * 币价格）
        BigDecimal sumPrice = contractMul.getContractMul().multiply(contractOrder.getContractHands().multiply(new BigDecimal(1000)));

        // 资金扣除
        BigDecimal takeFee = openBalance(contractOrder, contractMul, sumPrice);

        contractOrder.setCoinNum(coinNum);
        contractOrder.setMargin(sumPrice);
        contractOrder.setMatchPrice(new BigDecimal(00.000));// 状态
        contractOrder.setOrderState(TokenOrderConstant.Order_State.CREATE);
        contractOrder.setLeverNum(lever.getLever());
        contractOrder.setLeverDesc(lever.getLeverDesc());
        contractOrder.setIsContractHands(contractOrder.getContractHands());
        contractOrder.setTakeFee(takeFee);
        contractOrder.setMatchFee(new BigDecimal(00.000));
        contractOrder.setOrderType(TokenOrderConstant.Order_Type.POSITIONS);
        contractOrderMapper.insert(contractOrder);



        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("pairs_name", contractOrder.getPairsName());
        wrapperWarehouse.eq("member", contractOrder.getMember());
        wrapperWarehouse.eq("trade_type", contractOrder.getTradeType());
        wrapperWarehouse.eq("state", TokenOrderConstant.Order_State.CREATE);
        Warehouse warehouse = warehouseMapper.selectOne(wrapperWarehouse);
        if (warehouse == null) {
            // 个人组合订单
            warehouse = new Warehouse();
        }
        warehouse.setState(TokenOrderConstant.Order_State.CREATE);
        warehouse.setPairsName(contractOrder.getPairsName());
        warehouse.setCoinName(contractOrder.getCoinName());
        warehouse.setMainCur(contractOrder.getMainCur());
        warehouse.setTradeType(contractOrder.getTradeType());
        // 持仓均价
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        wrapperOrder.eq("pairs_name", contractOrder.getPairsName());
        wrapperOrder.eq("member", contractOrder.getMember());
        wrapperOrder.eq("trade_type", contractOrder.getTradeType());
        wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
        wrapperOrder.eq("order_type", TokenOrderConstant.Order_Type.POSITIONS);
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        BigDecimal sumTokenPrice = new BigDecimal("0");
        BigDecimal sumHands = new BigDecimal("0");
        BigDecimal levers = new BigDecimal("0");
        BigDecimal sumMargin = new BigDecimal("0");
        for (ContractOrder contractOrderInfo : contractOrders) {
            sumTokenPrice = sumTokenPrice.add(contractOrderInfo.getPrice()
                    .multiply(contractOrderInfo.getIsContractHands()).multiply(contractMul.getContractMul()));
            sumHands = sumHands.add(contractOrderInfo.getIsContractHands());
            levers = levers.add(contractOrderInfo.getLeverNum());
            sumMargin = sumMargin.add(contractOrderInfo.getMargin());
        }

        // 总保证金
        warehouse.setMargin(sumMargin.add(contractOrder.getMargin()));
        // 持仓均价
        BigDecimal avePrice = sumTokenPrice.divide(sumHands.multiply(contractMul.getContractMul()), 8,
                BigDecimal.ROUND_HALF_UP);
        warehouse.setAvePrice(avePrice);
        // 仓位价值
        BigDecimal tokenPrice = contractMul.getContractMul()
                .multiply(contractOrder.getContractHands().multiply(contractOrder.getPrice()));
        tokenPrice = tokenPrice.add(sumTokenPrice);
        warehouse.setTokenPrice(tokenPrice);
        // 仓位总数
        warehouse.setTokenNum(new BigDecimal(contractOrders.size()));
        // 可用仓位总数
        QueryWrapper<ContractOrder> isTokenNumWrapper = new QueryWrapper<ContractOrder>();
        isTokenNumWrapper.eq("pairs_name", contractOrder.getPairsName());
        isTokenNumWrapper.eq("member", contractOrder.getMember());
        isTokenNumWrapper.ge("order_state", TokenOrderConstant.Order_State.CREATE);
        warehouse.setIsTokenNum(new BigDecimal(contractOrderMapper.selectCount(isTokenNumWrapper)));

        warehouse.setHands(sumHands);
        warehouse.setOrders(new BigDecimal(contractOrders.size()));
        warehouse.setClosePrice(nowPrice);
        warehouse.setMember(contractOrder.getMember());
        if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
            // 未实现盈亏
            BigDecimal subPrice = nowPrice.subtract(avePrice);
            warehouse.setUnProfitLoss(subPrice.multiply(sumHands).multiply(contractMul.getContractMul()));
            BigDecimal aveLevel = levers.divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
            warehouse.setAvgLevel(aveLevel);
            // 收益
            BigDecimal profit = subPrice.multiply(aveLevel).divide(avePrice, 8, BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
            warehouse.setProfit(profit);
        } else {
            // 未实现盈亏
            BigDecimal subPrice = avePrice.subtract(nowPrice);
            warehouse.setUnProfitLoss(subPrice.multiply(sumHands).multiply(contractMul.getContractMul()));
            BigDecimal aveLevel = levers.divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
            warehouse.setAvgLevel(aveLevel);
            // 收益
            BigDecimal profit = subPrice.multiply(aveLevel).divide(avePrice, 8, BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
            warehouse.setProfit(profit);
        }
        warehouseService.saveOrUpdate(warehouse);


        return true;
    }
    @Override
    public boolean setContractOrder(ContractOrder contractOrder) {
//        if (!match_entrust.containsKey(contractOrder.getMember() + contractOrder.getPairsName())) {
//            match_entrust.put(contractOrder.getMember() + contractOrder.getPairsName(), 1);
//        } else {
//            throw new BusinessException(AjaxResultEnum.FREQUENT_OPERATIONS.getMessage());
//        }

        if (contractOrder.getContractHands().doubleValue() <= 0) {
            throw new BusinessException(AjaxResultEnum.LEVERAGE_MUST_BE_GREATER.getMessage());
        }
        if (Strings.isBlank(contractOrder.getPairsName())) {
            //throw new BusinessException("交易对不允许为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getCoinName())) {
            //throw new BusinessException("币种名称不允许为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getMainCur())) {
            //throw new BusinessException("主币名称不允许为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getTradeType().getTradeType())) {
            //throw new BusinessException("交易类型不允许为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getPriceType().getPriceType())) {
           // throw new BusinessException("价格类型不允许为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getContractMulId())) {
           // throw new BusinessException("交易对配置id不能为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (Strings.isBlank(contractOrder.getLeverId())) {
           // throw new BusinessException("交易对杠杆id不能为空");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }
        if (contractOrder.getPrice().doubleValue() <= 0) {
           // throw new BusinessException("价格必须大于0");
            throw new BusinessException(AjaxResultEnum.PARAMETER_IS_EMPTY.getMessage());
        }



        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_PAIRS_KEY + contractOrder.getMember() + contractOrder.getPairsName(),
                5000, 50, 100);
        if (lock_coin) {
            match_entrust.remove(contractOrder.getMember() + contractOrder.getPairsName());
            // 获取当前价
            String value = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + contractOrder.getPairsName());
            JSONObject jo = JSONObject.parseObject(value);
            BigDecimal nowPrice = new BigDecimal(jo.getBigDecimal("nowPrice").toString());
            // 币数量
            // 判读交易对后台是否配置
            ContractMul contractMul = contractMulMapper.selectById(contractOrder.getContractMulId());
            if (!contractMul.getPairsName().equals(contractOrder.getPairsName())) {
                throw new BusinessException(AjaxResultEnum.TRADING_PAIR_ERROR.getMessage());
            }
            // 合约乘数 * 杠杆手数
            BigDecimal coinNum = contractMul.getContractMul().multiply(contractOrder.getContractHands());
            // 杠杆倍数
            Lever lever = leverMapper.selectById(contractOrder.getLeverId());
            // 保证金 = 合约乘数 * （杠杆手数 * 币价格）
//            BigDecimal sumPrice = contractMul.getContractMul()
//                    .multiply(contractOrder.getContractHands().multiply(contractOrder.getPrice()));
            BigDecimal sumPrice = contractMul.getContractMul()
                    .multiply(contractOrder.getContractHands().multiply(new BigDecimal(1000)));
//            BigDecimal sumPrice =contractOrder.getContractHands().multiply(contractOrder.getPrice());
//            BigDecimal margin = sumPrice.divide(lever.getLever(), 8, BigDecimal.ROUND_HALF_UP);
            BigDecimal margin = sumPrice;
            contractOrder.setCoinNum(coinNum);
            contractOrder.setMargin(margin);
            // 状态
            contractOrder.setOrderState(TokenOrderConstant.Order_State.CREATE);
            contractOrder.setLeverNum(lever.getLever());
            contractOrder.setLeverDesc(lever.getLeverDesc());
            contractOrder.setIsContractHands(contractOrder.getContractHands());

            // 资金扣除
            BigDecimal takeFee = openBalance(contractOrder, contractMul, margin);

            contractOrder.setTakeFee(takeFee);

            if (contractOrder.getPriceType().equals(ContractConstant.Price_Type.CUSTOM_PRICE)) {
                Set<String> set = new HashSet<String>();
                if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                    set.addAll(redisRepository.zsetRangByScore(
                            CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_DOWN
                                    + CacheConstants.SPLIT + contractOrder.getPairsName(),
                            0, contractOrder.getPrice().doubleValue()));

                } else {
                    set.addAll(redisRepository.zsetRevRangByScore(
                            CacheConstants.TOKEN_ORDER_MATCH_KEY + ContractConstant.Trade_Type.OPEN_UP
                                    + CacheConstants.SPLIT + contractOrder.getPairsName(),
                            contractOrder.getPrice().doubleValue(), Long.parseLong("99999999999")));
                }
                if (!set.isEmpty()) {
                    contractOrder.setOrderType(TokenOrderConstant.Order_Type.POSITIONS);
                    contractOrder.setPriceType(ContractConstant.Price_Type.MARKET_PRICE);
                } else {
                    contractOrder.setOrderType(TokenOrderConstant.Order_Type.ORDERS);
                    // 普通委托插入redis中
                    redisRepository.zsetAdd(
                            CacheConstants.TOKEN_ORDER_CUSTOM_KEY + contractOrder.getTradeType() + CacheConstants.SPLIT
                                    + contractOrder.getPairsName(),
                            JSONObject.toJSONString(contractOrder), contractOrder.getPrice().doubleValue());
                }
            } else {
                contractOrder.setOrderType(TokenOrderConstant.Order_Type.POSITIONS);
            }

            contractOrderMapper.insert(contractOrder);
            BrokerageRecordPo brokerageRecordPo = new BrokerageRecordPo(contractOrder.getMember(), "USDT", takeFee);
            matchProducer.putCurrency(JSONObject.toJSONString(brokerageRecordPo));
            if (contractOrder.getPriceType().equals(ContractConstant.Price_Type.MARKET_PRICE)) {
                QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
                wrapperWarehouse.eq("pairs_name", contractOrder.getPairsName());
                wrapperWarehouse.eq("member", contractOrder.getMember());
                wrapperWarehouse.eq("trade_type", contractOrder.getTradeType());
                wrapperWarehouse.eq("state", TokenOrderConstant.Order_State.CREATE);
                Warehouse warehouse = warehouseMapper.selectOne(wrapperWarehouse);
                if (warehouse == null) {
                    // 个人组合订单
                    warehouse = new Warehouse();
                }
                warehouse.setState(TokenOrderConstant.Order_State.CREATE);
                warehouse.setPairsName(contractOrder.getPairsName());
                warehouse.setCoinName(contractOrder.getCoinName());
                warehouse.setMainCur(contractOrder.getMainCur());
                warehouse.setTradeType(contractOrder.getTradeType());
                // 持仓均价
                QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
                wrapperOrder.eq("pairs_name", contractOrder.getPairsName());
                wrapperOrder.eq("member", contractOrder.getMember());
                wrapperOrder.eq("trade_type", contractOrder.getTradeType());
                wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
                wrapperOrder.eq("order_type", TokenOrderConstant.Order_Type.POSITIONS);
                List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
                BigDecimal sumTokenPrice = new BigDecimal("0");
                BigDecimal sumHands = new BigDecimal("0");
                BigDecimal levers = new BigDecimal("0");
                BigDecimal sumMargin = new BigDecimal("0");
                for (ContractOrder contractOrderInfo : contractOrders) {
                    sumTokenPrice = sumTokenPrice.add(contractOrderInfo.getPrice()
                            .multiply(contractOrderInfo.getIsContractHands()).multiply(contractMul.getContractMul()));
                    sumHands = sumHands.add(contractOrderInfo.getIsContractHands());
                    levers = levers.add(contractOrderInfo.getLeverNum());
                    sumMargin = sumMargin.add(contractOrderInfo.getMargin());
                }
                // 总保证金
                warehouse.setMargin(sumMargin);
                // 持仓均价
                BigDecimal avePrice = sumTokenPrice.divide(sumHands.multiply(contractMul.getContractMul()), 8,
                        BigDecimal.ROUND_HALF_UP);
                warehouse.setAvePrice(avePrice);
                // 仓位价值
                BigDecimal tokenPrice = contractMul.getContractMul()
                        .multiply(contractOrder.getContractHands().multiply(contractOrder.getPrice()));
                tokenPrice = tokenPrice.add(sumTokenPrice);
                warehouse.setTokenPrice(tokenPrice);
                // 仓位总数
                warehouse.setTokenNum(new BigDecimal(contractOrders.size()));
                // 可用仓位总数
                QueryWrapper<ContractOrder> isTokenNumWrapper = new QueryWrapper<ContractOrder>();
                isTokenNumWrapper.eq("pairs_name", contractOrder.getPairsName());
                isTokenNumWrapper.eq("member", contractOrder.getMember());
                isTokenNumWrapper.ge("order_state", TokenOrderConstant.Order_State.CREATE);
                warehouse.setIsTokenNum(new BigDecimal(contractOrderMapper.selectCount(isTokenNumWrapper)));

                warehouse.setHands(sumHands);
                warehouse.setOrders(new BigDecimal(contractOrders.size()));
                warehouse.setClosePrice(nowPrice);
                warehouse.setMember(contractOrder.getMember());
                if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                    // 未实现盈亏
                    BigDecimal subPrice = nowPrice.subtract(avePrice);
                    warehouse.setUnProfitLoss(subPrice.multiply(sumHands).multiply(contractMul.getContractMul()));
                    BigDecimal aveLevel = levers.divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
                    warehouse.setAvgLevel(aveLevel);
                    // 收益
                    BigDecimal profit = subPrice.multiply(aveLevel).divide(avePrice, 8, BigDecimal.ROUND_HALF_UP)
                            .divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
                    warehouse.setProfit(profit);
                } else {
                    // 未实现盈亏
                    BigDecimal subPrice = avePrice.subtract(nowPrice);
                    warehouse.setUnProfitLoss(subPrice.multiply(sumHands).multiply(contractMul.getContractMul()));
                    BigDecimal aveLevel = levers.divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
                    warehouse.setAvgLevel(aveLevel);
                    // 收益
                    BigDecimal profit = subPrice.multiply(aveLevel).divide(avePrice, 8, BigDecimal.ROUND_HALF_UP)
                            .divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
                    warehouse.setProfit(profit);
                }
                warehouseService.saveOrUpdate(warehouse);
            }
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_PAIRS_KEY + contractOrder.getMember() + contractOrder.getPairsName());
        } else {
            throw new BusinessException(AjaxResultEnum.FREQUENT_OPERATIONS.getMessage());
        }

        return true;
    }

    private BigDecimal openBalance(ContractOrder contractOrder, ContractMul contractMul, BigDecimal margin) {
        // 手续费
        BigDecimal takeFee = new BigDecimal("0");
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMember(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", contractOrder.getMainCur());
            wrapperMain.eq("user_id", contractOrder.getMember());
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);

            try {
                if (balanceMain.getAssetsBalance().compareTo(margin) == -1) {
                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_MARGIN_BALANCE.getMessage());
                }
            } catch (Exception e) {
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_MARGIN_BALANCE.getMessage());
            }
            // 手续费
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                /*takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
                        .multiply(contractOrder.getPrice()).multiply(contractMul.getMakerFee());*/
                takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
                        .multiply(new BigDecimal(1000)).multiply(contractMul.getMakerFee());
            } else {
//                takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
//                        .multiply(contractOrder.getPrice()).multiply(contractMul.getTakerFee());
                takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
                        .multiply(new BigDecimal(1000)).multiply(contractMul.getTakerFee());
            }

            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balanceMain.getAssetsBlockedBalance();

            // 冻结用户余额，保证金 = 可用资金 - 保证金*2 -手續費
            BigDecimal  kk=margin.multiply(new BigDecimal(2));
            BigDecimal subtract = balanceMain.getAssetsBalance().subtract(kk).subtract(takeFee);
            balanceMain.setAssetsBalance(subtract);
            balanceMain.setAssetsBlockedBalance(balanceMain.getAssetsBlockedBalance().add(margin));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());

            // 资金减少记录
            saveBalanceRecord(contractOrder.getMember(),contractOrder.getMainCur(),10,1,assetsBalance,balanceMain.getAssetsBalance(),subtract);

            // 冻结日志
            saveBalanceRecord(contractOrder.getMember(),contractOrder.getMainCur(),13,2,assetsBlockedBalance,balanceMain.getAssetsBlockedBalance(),margin);

        } else {
            openBalance(contractOrder, contractMul, margin);
        }
        return takeFee;

    }


    // 保存资金记录
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

    @Override
    public ContractOrder getContractOrder(String id) {
        return contractOrderMapper.selectById(id);
    }

    @Override
    public boolean setOrderMatch(String id, BigDecimal hands) {
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("id", id);
        Warehouse warehouse = warehouseMapper.selectOne(wrapperWarehouse);
        if (warehouse == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_DOES_NOT_EXIST.getMessage());
        }
        // 查询币配置
        QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
        wrapper.eq("pairs_name", warehouse.getPairsName());
        ContractMul contractMul = contractMulMapper.selectOne(wrapper);
        BigDecimal startHands = hands;
        // 查询当前未完成委托
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        wrapperOrder.eq("pairs_name", warehouse.getPairsName());
        wrapperOrder.eq("member", warehouse.getMember());
        wrapperOrder.eq("trade_type", warehouse.getTradeType());
        wrapperOrder.eq("order_type", TokenOrderConstant.Order_Type.POSITIONS);
        wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        for (ContractOrder contractOrder : contractOrders) {
            BigDecimal matchHands = new BigDecimal("0");
            BigDecimal margin = new BigDecimal("0");
            // 持仓手数 小于 平仓手数
            if (contractOrder.getIsContractHands().compareTo(hands) == -1) {
                matchHands = contractOrder.getIsContractHands();
                hands = hands.subtract(contractOrder.getIsContractHands());
                contractOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
                contractOrder.setIsContractHands(new BigDecimal("0"));
                contractOrder.setCloseType(TokenOrderConstant.Close_Type.MATCH);
                margin = contractOrder.getMargin();
            }
            // 持仓手数 等于 平仓手数
            if (contractOrder.getIsContractHands().compareTo(hands) == 0) {
                matchHands = hands;
                hands = new BigDecimal("0");
                contractOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
                contractOrder.setIsContractHands(new BigDecimal("0"));
                contractOrder.setCloseType(TokenOrderConstant.Close_Type.MATCH);
                margin = contractOrder.getMargin();
            }
            // 持仓手数 大于 平仓手数
            if (contractOrder.getIsContractHands().compareTo(hands) == 1) {
                // 平仓手数 * （持仓手数 * 保证金）
                margin = hands.divide(contractOrder.getIsContractHands(), 8, BigDecimal.ROUND_HALF_UP)
                        .multiply(contractOrder.getMargin());
                matchHands = hands;
                contractOrder.setIsContractHands(contractOrder.getIsContractHands().subtract(hands));
                hands = new BigDecimal("0");
                contractOrder.setCloseType(TokenOrderConstant.Close_Type.MATCH);

            }
            // 获取币最新价
            String value = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + contractOrder.getPairsName());
            JSONObject jo = JSONObject.parseObject(value);
            BigDecimal price = new BigDecimal(jo.getBigDecimal("nowPrice").toString());

            //2023-03-22 控盘金额操作
            if (warehouse.getWin()>0){
                price=warehouse.getClosePrice();
            }

            // 手续费
            BigDecimal takeFee = new BigDecimal("0");
            // 合约方向-看涨
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                // 手续费 = 平仓手数 * 持仓手数 * 价格 * 开仓手续费
                /*takeFee = matchHands.multiply(contractMul.getContractMul()).multiply(contractOrder.getPrice())
                        .multiply(contractMul.getMakerFee());*/
                takeFee = matchHands.multiply(contractMul.getContractMul()).multiply(new BigDecimal(1000))
                        .multiply(contractMul.getMakerFee());
            } else {
                // 手续费 = 平仓手数 * 持仓手数 * 价格 * 平仓手续费
                /*takeFee = matchHands.multiply(contractMul.getContractMul()).multiply(contractOrder.getPrice())
                        .multiply(contractMul.getTakerFee());*/
                takeFee = matchHands.multiply(contractMul.getContractMul()).multiply(new BigDecimal(1000))
                        .multiply(contractMul.getTakerFee());
            }


            // activeMargin=activeMargin.subtract(takeFee);
//			BigDecimal priceResultUp = price.subtract(contractOrder.getPrice());
//			BigDecimal priceResultDown = contractOrder.getPrice().subtract(price);
//			BigDecimal matchFee = new BigDecimal("0");
            // 已实现盈亏
//			if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
//				matchFee = priceResultUp.multiply(matchHands).multiply(contractMul.getContractMul());
//			} else {
//				matchFee = priceResultDown.multiply(matchHands).multiply(contractMul.getContractMul());
//			}
//			contractOrder.setMatchFee(matchFee);
            // 保证金减少
            contractOrder.setMargin(contractOrder.getMargin().subtract(margin));
            updateBalance(contractOrder, takeFee, margin);
            contractOrder.setMatchPrice(price);
            contractOrderMapper.updateById(contractOrder);

            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_UP);
            } else {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_DOWN);
            }
            contractOrder.setContractHands(matchHands);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            contractOrder.setId(uuid);
            contractOrder.setPrice(price);
            contractOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
            contractOrder.setCreateTime(null);
            contractOrder.setTakeFee(takeFee);
            contractOrderMapper.insert(contractOrder);

        }
        // 平仓手数 小于 总持仓手数
        if (startHands.compareTo(warehouse.getHands()) == -1) {
            // 持仓均价
            QueryWrapper<ContractOrder> wrapperOrderMatch = new QueryWrapper<ContractOrder>();
            wrapperOrderMatch.eq("pairs_name", warehouse.getPairsName());
            wrapperOrderMatch.eq("member", warehouse.getMember());
            wrapperOrderMatch.eq("trade_type", warehouse.getTradeType());
            wrapperOrderMatch.ne("order_state", TokenOrderConstant.Order_State.FINAL);
            wrapperOrderMatch.eq("order_type", TokenOrderConstant.Order_Type.POSITIONS);
            List<ContractOrder> contractOrderList = contractOrderMapper.selectList(wrapperOrderMatch);
            BigDecimal sumTokenPrice = new BigDecimal("0");
            BigDecimal sumHands = new BigDecimal("0");
            BigDecimal levers = new BigDecimal("0");
            BigDecimal sumMargin = new BigDecimal("0");
            for (ContractOrder contractOrderInfo : contractOrderList) {
                sumTokenPrice = sumTokenPrice.add(contractOrderInfo.getPrice()
                        .multiply(contractOrderInfo.getIsContractHands()).multiply(contractMul.getContractMul()));
                sumHands = sumHands.add(contractOrderInfo.getIsContractHands());
                levers = levers.add(contractOrderInfo.getLeverNum());
                sumMargin = sumMargin.add(contractOrderInfo.getMargin());
            }
            // 总保证金
            warehouse.setMargin(sumMargin);
            // 持仓均价
            BigDecimal avePrice = sumTokenPrice.divide(sumHands.multiply(contractMul.getContractMul()), 8,
                    BigDecimal.ROUND_HALF_UP);
            warehouse.setAvePrice(avePrice);
            warehouse.setTokenPrice(sumTokenPrice);
            // 仓位总数
            warehouse.setTokenNum(new BigDecimal(contractOrders.size()));
            // 可用仓位总数
            QueryWrapper<ContractOrder> isTokenNumWrapper = new QueryWrapper<ContractOrder>();
            isTokenNumWrapper.eq("pairs_name", warehouse.getPairsName());
            isTokenNumWrapper.eq("member", warehouse.getMember());
            isTokenNumWrapper.ge("order_state", TokenOrderConstant.Order_State.CREATE);
            warehouse.setIsTokenNum(new BigDecimal(contractOrderMapper.selectCount(isTokenNumWrapper)));
            warehouse.setHands(sumHands);
            warehouse.setOrders(new BigDecimal(contractOrders.size()));
            warehouseService.saveOrUpdate(warehouse);
        } else {
            warehouseMapper.deleteById(warehouse.getId());
            redisRepository.del(CacheConstants.MEMBER_RISK_KEY + warehouse.getMember());
        }

        QueryWrapper<Warehouse> wrapperWarehouse1 = new QueryWrapper<Warehouse>();
        wrapperWarehouse1.eq("member", warehouse.getMember());
        wrapperWarehouse1.eq("state", TokenOrderConstant.Order_State.CREATE);
        List<Warehouse> warehouses = warehouseMapper.selectList(wrapperWarehouse);
        if (warehouses.isEmpty()) {
            redisRepository.del(CacheConstants.MEMBER_PROFIT_KEY + warehouse.getMember());
        }
        return true;
    }

    /**
     * @param contractOrder 合约
     * @param takeFee 手续费
     * @param margin 保证金
     */
    private void updateBalance(ContractOrder contractOrder, BigDecimal takeFee, BigDecimal margin) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMember(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
            wrapperActive.eq("currency", contractOrder.getMainCur());
            wrapperActive.eq("user_id", contractOrder.getMember());
            Balance balanceActive = balanceMapper.selectOne(wrapperActive);

            BigDecimal assetsBalance = balanceActive.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balanceActive.getAssetsBlockedBalance();

            BigDecimal balance = balanceActive.getAssetsBalance().add(margin).subtract(takeFee);
            balanceActive.setAssetsBalance(balance);

            balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(margin));
            balanceMapper.updateById(balanceActive);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceActive.getUserId());

            // 平仓可用增加
            saveBalanceRecord(contractOrder.getMember(),contractOrder.getMainCur(),11,2,assetsBalance,balanceActive.getAssetsBalance(),balance);

            // 平仓冻结减少
            saveBalanceRecord(contractOrder.getMember(),contractOrder.getMainCur(),12,1,assetsBlockedBalance,balanceActive.getAssetsBlockedBalance(),margin);
        } else {
            updateBalance(contractOrder, takeFee, margin);
        }
    }

    @Override
    public boolean setOrdTriggerMatch(String id, BigDecimal price, TokenOrderConstant.Match_Type matchType) {
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("id", id);
        Warehouse warehouse = warehouseMapper.selectOne(wrapperWarehouse);
        if (warehouse == null) {
            throw new BusinessException(AjaxResultEnum.ORDER_DOES_NOT_EXIST.getMessage());
        }
        if (matchType.equals(TokenOrderConstant.Match_Type.TRIGGER)) {
            warehouse.setTriggerPrice(price);
        }
        if (matchType.equals(TokenOrderConstant.Match_Type.ORD)) {
            warehouse.setOrdPrice(price);
        }
        warehouseMapper.updateById(warehouse);
        return true;
    }

    /**
     * 一键平仓，平掉当前所有的合约委托
     * @param member
     * @param pairsName
     * @return
     */
    @Override
    public boolean setAllContractMatch(String member, String pairsName) {
        QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
        wrapperWarehouse.eq("member", member);
        wrapperWarehouse.eq("pairs_name", pairsName);
        wrapperWarehouse.eq("state", TokenOrderConstant.Order_State.CREATE);
        List<Warehouse> warehouses = warehouseMapper.selectList(wrapperWarehouse);
        for (Warehouse warehouse : warehouses) {
            this.setOrderMatch(warehouse.getId(), warehouse.getHands());
        }
        QueryWrapper<Warehouse> wrapperWarehouse1 = new QueryWrapper<Warehouse>();
        wrapperWarehouse1.eq("member", member);
        wrapperWarehouse1.eq("state", TokenOrderConstant.Order_State.CREATE);
        List<Warehouse> warehouses1 = warehouseMapper.selectList(wrapperWarehouse);
        if (warehouses1.isEmpty()) {
            redisRepository.del(CacheConstants.MEMBER_PROFIT_KEY + member);
        }
        return true;
    }

    @Override
    public JSONObject getWarehouses(String member, String pairsName) {
        JSONObject josnInfo = new JSONObject();
        if (Strings.isNotBlank(pairsName)) {
            QueryWrapper<Warehouse> wrapper = new QueryWrapper<Warehouse>();
            wrapper.eq("member", member);
            wrapper.eq("pairs_name", pairsName);
            wrapper.orderByDesc("create_time");
            List<Warehouse> list = warehouseMapper.selectList(wrapper);
            // 获取当前价
            String value = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + pairsName);
            JSONObject jo = JSONObject.parseObject(value);
            BigDecimal nowPrice = new BigDecimal(jo.getBigDecimal("nowPrice").toString());
            List<Warehouse> newList = new ArrayList<Warehouse>();
            if (list.isEmpty()) {
                josnInfo.put("warehouses", newList);
                josnInfo.put("nowPrice", value);
                return josnInfo;
            }
            // 获取净头寸
            BigDecimal position = new BigDecimal("0");
            if (list.size() > 1) {
                position = list.get(0).getHands().subtract(list.get(1).getHands());
            } else {
                position = list.get(0).getHands();
            }
            position = new BigDecimal(Math.abs(position.doubleValue()));
            for (Warehouse warehouse : list) {
                QueryWrapper<ContractMul> wrapperContract = new QueryWrapper<ContractMul>();
                wrapperContract.eq("pairs_name", warehouse.getPairsName());
                ContractMul contractMul = contractMulMapper.selectOne(wrapperContract);
                if (position.compareTo(new BigDecimal("0")) == 1) {
                    QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
                    wrapperMain.eq("currency", warehouse.getMainCur());
                    wrapperMain.eq("user_id", warehouse.getMember());
                    Balance balanceMain = balanceMapper.selectOne(wrapperMain);
                    BigDecimal one = new BigDecimal("1").add(contractMul.getEnsure());
                    BigDecimal three = warehouse.getAvePrice().multiply(one);
                    BigDecimal two = balanceMain.getAssetsBalance()
                            .divide(position.multiply(contractMul.getContractMul()), 8, BigDecimal.ROUND_HALF_UP);
                    BigDecimal forcePrice = new BigDecimal("0");
                    if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                        // 预估强平价格
                        forcePrice = three.subtract(two);
                    } else {
                        // 预估强平价格
                        forcePrice = three.add(two);
                    }
                    // forcePrice = new BigDecimal(Math.abs(forcePrice.doubleValue()));
                    warehouse.setForcePrice(forcePrice);
                } else {
                    warehouse.setForcePrice(new BigDecimal("0"));
                }
                //傻逼操作
                double random = Math.random()*100;
                BigDecimal  pri = new BigDecimal(random);
                if(warehouse.getWin() == 1){//赢
                    nowPrice= nowPrice.add(pri);
                    warehouseService.UpdateControlPrice(warehouse.getId(),nowPrice);
                } else if (warehouse.getWin() == 2) {//输
                    nowPrice= nowPrice.subtract(pri);
                    warehouseService.UpdateControlPrice(warehouse.getId(),nowPrice);
                }
                if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                    // 未实现盈亏
                    BigDecimal subPrice = nowPrice.subtract(warehouse.getAvePrice());
                    warehouse.setUnProfitLoss(
                            subPrice.multiply(warehouse.getHands()).multiply(contractMul.getContractMul()));
                    // 收益
                    BigDecimal profit = subPrice.multiply(warehouse.getAvgLevel()).divide(warehouse.getAvePrice(), 8,
                            BigDecimal.ROUND_HALF_UP);
                    warehouse.setProfitUp(profit);
                } else {
                    // 未实现盈亏
                    BigDecimal subPrice = warehouse.getAvePrice().subtract(nowPrice);
                    warehouse.setUnProfitLoss(
                            subPrice.multiply(warehouse.getHands()).multiply(contractMul.getContractMul()));
                    // 收益
                    BigDecimal profit = subPrice.multiply(warehouse.getAvgLevel()).divide(warehouse.getAvePrice(), 8,
                            BigDecimal.ROUND_HALF_UP);
                    warehouse.setProfitUp(profit);
                }


                newList.add(warehouse);
            }
            
            josnInfo.put("warehouses", newList);
            josnInfo.put("nowPrice", value);
        } else {
            QueryWrapper<Warehouse> wrapper = new QueryWrapper<Warehouse>();
            wrapper.eq("member", member);
            wrapper.select("pairs_name");
            wrapper.groupBy("pairs_name");
            List<Object> objs = warehouseMapper.selectObjs(wrapper);
            List<Warehouse> newList = new ArrayList<Warehouse>();
            for (Object obj : objs) {
                QueryWrapper<Warehouse> wrapperWh = new QueryWrapper<Warehouse>();
                wrapperWh.eq("member", member);
                wrapperWh.eq("pairs_name", obj);
                wrapperWh.orderByDesc("create_time");
                List<Warehouse> list = warehouseMapper.selectList(wrapperWh);
                if (list.isEmpty()) {
                    continue;
                }
                // 获取当前价
                String value = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + obj);
                JSONObject jo = JSONObject.parseObject(value);
                BigDecimal nowPrice = new BigDecimal(jo.getBigDecimal("nowPrice").toString());
                // 获取净头寸
                BigDecimal position = new BigDecimal("0");
                if (list.size() > 1) {
                    position = list.get(0).getHands().subtract(list.get(1).getHands());
                } else {
                    position = list.get(0).getHands();
                }
                position = new BigDecimal(Math.abs(position.doubleValue()));
                for (Warehouse warehouse : list) {
                    QueryWrapper<ContractMul> wrapperContract = new QueryWrapper<ContractMul>();
                    wrapperContract.eq("pairs_name", warehouse.getPairsName());
                    ContractMul contractMul = contractMulMapper.selectOne(wrapperContract);
                    if (position.compareTo(new BigDecimal("0")) == 1) {
                        QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
                        wrapperMain.eq("currency", warehouse.getMainCur());
                        wrapperMain.eq("user_id", warehouse.getMember());
                        Balance balanceMain = balanceMapper.selectOne(wrapperMain);
                        BigDecimal one = new BigDecimal("1").add(contractMul.getEnsure());
                        BigDecimal two = balanceMain.getAssetsBalance()
                                .divide(position.multiply(contractMul.getContractMul()), 8, BigDecimal.ROUND_HALF_UP);
                        BigDecimal three = warehouse.getAvePrice().multiply(one);
                        BigDecimal forcePrice = new BigDecimal("0");
                        if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                            // 预估强平价格
                            forcePrice = three.subtract(two);
                        } else {
                            // 预估强平价格
                            forcePrice = three.add(two);
                        }
                        // forcePrice = new BigDecimal(Math.abs(forcePrice.doubleValue()));
                        warehouse.setForcePrice(forcePrice);
                    } else {
                        warehouse.setForcePrice(new BigDecimal("0"));
                    }
                    if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                        // 未实现盈亏
                        BigDecimal subPrice = nowPrice.subtract(warehouse.getAvePrice());
                        warehouse.setUnProfitLoss(
                                subPrice.multiply(warehouse.getHands()).multiply(contractMul.getContractMul()));
                        // 收益
                        BigDecimal profit = subPrice.multiply(warehouse.getAvgLevel()).divide(warehouse.getAvePrice(),
                                8, BigDecimal.ROUND_HALF_UP);
                        warehouse.setProfitUp(profit);
                    } else {
                        // 未实现盈亏
                        BigDecimal subPrice = warehouse.getAvePrice().subtract(nowPrice);
                        warehouse.setUnProfitLoss(
                                subPrice.multiply(warehouse.getHands()).multiply(contractMul.getContractMul()));
                        // 收益
                        BigDecimal profit = subPrice.multiply(warehouse.getAvgLevel()).divide(warehouse.getAvePrice(),
                                8, BigDecimal.ROUND_HALF_UP);
                        warehouse.setProfitUp(profit);
                    }
                    newList.add(warehouse);
                }
            }
            josnInfo.put("warehouses", newList);
            josnInfo.put("nowPrice", null);
        }
        return josnInfo;
    }

    @Override
    public boolean closeContractOrder(String orderId) {
        ContractOrder contractOrder = contractOrderMapper.selectById(orderId);
        if (!contractOrder.getOrderType().equals(TokenOrderConstant.Order_Type.ORDERS)) {
            throw new BusinessException(AjaxResultEnum.THIS_ORDER_IS_ALREADY_OPEN_AND_IRREVOCABLE.getMessage());
        }
        contractOrder.setOrderState(TokenOrderConstant.Order_State.CANCEL);
        closeBalance(contractOrder);
        contractOrder.setMargin(new BigDecimal("0"));
        contractOrderMapper.updateById(contractOrder);
        return false;
    }

    private void closeBalance(ContractOrder contractOrder) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMember(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", contractOrder.getMainCur());
            wrapperMain.eq("user_id", contractOrder.getMember());
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            balanceMain.setAssetsBalance(balanceMain.getAssetsBalance().add(contractOrder.getMargin()));
            balanceMain
                    .setAssetsBlockedBalance(balanceMain.getAssetsBlockedBalance().subtract(contractOrder.getMargin()));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
        } else {
            closeBalance(contractOrder);
        }
    }

    @Override
    public Response getHistoryOrders(String member, String pairsName, TokenOrderConstant.Order_State orderState,Integer pageNum,Integer pageSize) {
        QueryWrapper<ContractOrder> wrapper = new QueryWrapper<ContractOrder>();
        wrapper.eq("member", member);
        if (Strings.isNotBlank(pairsName)) {
            wrapper.eq("pairs_name", pairsName);
        }
        wrapper.eq("order_state", orderState);
        wrapper.orderByDesc("create_time");
//        List<ContractOrder> list = contractOrderMapper.selectList(wrapper);

        IPage<ContractOrder> page = contractOrderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        return Response.success(page);
    }

    @Override
    public Map<String, Object> getRisk(String member) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 风险度
        String risk = redisRepository.get(CacheConstants.MEMBER_RISK_KEY + member);
        // 资金使用率
        Map<String, BigDecimal> map = balanceMapper.getSumBalance(member);
        BigDecimal moneyRate = new BigDecimal("0");
        if (map != null) {
            if (map.get("assets_blocked_balance").compareTo(new BigDecimal("0")) == 1) {
                moneyRate = map.get("assets_blocked_balance").divide(map.get("assets_balance"), 8,
                        BigDecimal.ROUND_HALF_UP);
            }
        }
        // 资金杠杆
        BigDecimal sumTokenPrice = balanceMapper.getSumTokenPrice(member);
        BigDecimal moneyLever = new BigDecimal("0");
        if (sumTokenPrice == null) {
            sumTokenPrice = new BigDecimal("0");
        }
        if (sumTokenPrice.compareTo(new BigDecimal("0")) == 1) {
            moneyLever = sumTokenPrice.divide(map.get("assets_balance"), 8, BigDecimal.ROUND_HALF_UP);
        }
        resultMap.put("risk", risk);
        resultMap.put("moneyRate", moneyRate);
        resultMap.put("moneyLever", moneyLever);
        return resultMap;
    }

    @Override
    public List<ContractOrder> getEntrustOrder(String member, String pairsName) {
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        if (Strings.isNotBlank(pairsName)) {
            wrapperOrder.eq("pairs_name", pairsName);
        }
        wrapperOrder.eq("member", member);
        wrapperOrder.eq("order_state", TokenOrderConstant.Order_State.CREATE);
        wrapperOrder.eq("order_type", TokenOrderConstant.Order_Type.ORDERS);
        wrapperOrder.orderByDesc("create_time");
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        return contractOrders;
    }

    @Override
    public BigDecimal getMaxHands(String member, String pairsName, BigDecimal price, BigDecimal level) {
        if (Strings.isBlank(member)) {
            throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
        }
        if (Strings.isBlank(pairsName)) {
            throw new BusinessException(AjaxResultEnum.TRADING_PAIR_IS_NOT_ALLOWED_EMPTY.getMessage());
        }
        QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
        wrapper.eq("pairs_name", pairsName);
        ContractMul contractCfg = contractMulMapper.selectOne(wrapper);

        QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
        wrapperMain.eq("currency", "USDT");
        wrapperMain.eq("user_id", member);
        Balance balanceMain = balanceMapper.selectOne(wrapperMain);

        BigDecimal one = new BigDecimal("1").divide(level, 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal two = one.add(contractCfg.getTakerFee().multiply(new BigDecimal("2")));
        BigDecimal three = balanceMain.getAssetsBalance()
                .divide(price.multiply(contractCfg.getContractMul()).multiply(two), 8, BigDecimal.ROUND_HALF_UP);
        return three;
    }

    @Override
    public Object secondContractBetting(ContractDeliveryVo vo) {
        if (Strings.isBlank(vo.getMember())) {
            throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
        }

        if(null == vo.getDeliveryTime()){
            throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
        }

        if (Strings.isBlank(vo.getCurrency())) {
            vo.setCurrency("BTC");
        }

        // 判断上一笔是否有处理
//        QueryWrapper<SecondsBetLog> wrapperMain = new QueryWrapper<SecondsBetLog>();
//        wrapperMain.eq("member_id",vo.getMember());
//        wrapperMain.eq("settle_status",2);
//        List<SecondsBetLog> secondsBetLogs = secondsBetLogMapper.selectList(wrapperMain);
//        if(secondsBetLogs.size() > 0){
//            throw new BusinessException(AjaxResultEnum.T_L_O_H_N_B_S.getMessage());
//        }

        // 判断下注金额是否低于最低下注
        SecondsConfig secondsConfig = secondsConfigMapper.selectById(vo.getDeliveryTime());
        if(vo.getDeliveryAmount().doubleValue() < secondsConfig.getMinimum().doubleValue()){
            throw new BusinessException(AjaxResultEnum.LESS_THAN_THE_MINIMUM_BET_AMOUNT.getMessage());
        }

        SecondsBetLog secondsBetLog = new SecondsBetLog();
        secondsBetLog.setMemberId(vo.getMember());
        secondsBetLog.setCkType(vo.getCurrency());
        secondsBetLog.setCkName("USDT");
        secondsBetLog.setBuyStatus(vo.getContractType());
        secondsBetLog.setStartAmount(vo.getCurrentPrice());
        secondsBetLog.setAmount(vo.getDeliveryAmount());
        secondsBetLog.setSid(vo.getDeliveryTime());
        secondsBetLog.setCreateTime(new Date());
        secondsBetLog.setSettleStatus(2);


        // 冻结合约金额
        ContractOrder contractOrder = new ContractOrder();
        contractOrder.setMember(vo.getMember());
        contractOrder.setMainCur(secondsBetLog.getCkName());
        updateBalanceContractBetting(contractOrder,vo.getDeliveryAmount(),vo);

        secondsBetLogMapper.insert(secondsBetLog);

        return secondsBetLog.getId();
    }

    private void updateBalanceContractBetting(ContractOrder contractOrder, BigDecimal takeFee,ContractDeliveryVo vo) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMember(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<Balance>();
            wrapperActive.eq("currency", contractOrder.getMainCur());
            wrapperActive.eq("user_id", contractOrder.getMember());
            Balance balanceActive = balanceMapper.selectOne(wrapperActive);
            BigDecimal balance = balanceActive.getAssetsBalance();

            if (takeFee.doubleValue() > balance.doubleValue()) {
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_MARGIN_BALANCE.getMessage());
            }

            // 余额
            balanceActive.setAssetsBalance(balance.subtract(takeFee));
            // 冻结余额
            balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().add(takeFee));

            balanceMapper.updateById(balanceActive);


            // 解冻日志
            BalanceRecord balanceRecord1 = new BalanceRecord();
            balanceRecord1.setMemberId(contractOrder.getMember());
            balanceRecord1.setCurrency(contractOrder.getMainCur());
            balanceRecord1.setBalanceType(vo.getContractType() == 1? 41:44);
            balanceRecord1.setFundsType(1);
            balanceRecord1.setBalanceBefore(balance);
            balanceRecord1.setBalanceBack(balanceActive.getAssetsBalance());
            balanceRecord1.setBalanceDifference(takeFee);
            balanceRecord1.setCreateTime(new Date());
            balanceRecord1.setDataClassification(4);
            balanceRecordMapper.insert(balanceRecord1);

            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceActive.getUserId());
        } else {
            updateBalanceContractBetting(contractOrder, takeFee,vo);
        }
    }


    @Override
    public List<SecondsConfig> getSecondsContract() {
        QueryWrapper<SecondsConfig> wrapperMain = new QueryWrapper<SecondsConfig>();
        wrapperMain.orderByAsc("sort");
        return secondsConfigMapper.selectList(wrapperMain);
    }

    @Override
    public List<MemberCurrencyConfig> getCurrencyConfiguration() {
        QueryWrapper<MemberCurrencyConfig> wrapperMain = new QueryWrapper<MemberCurrencyConfig>();
        wrapperMain.orderByAsc("sort");
        return memberCurrencyConfigMapper.selectList(wrapperMain);
    }

    @Override
    public Response getSecondContractRecord(String member,Integer type, Integer pageNum, Integer pageSize) {
        QueryWrapper<SecondsBetLog> wrapperMain = new QueryWrapper<SecondsBetLog>();
        wrapperMain.eq("member_id",member);
        wrapperMain.eq("settle_status",type);
        wrapperMain.orderByDesc("create_time");
//        List<SecondsBetLog> secondsBetLogs = secondsBetLogMapper.selectList(wrapperMain);

        IPage<SecondsBetLog> page = secondsBetLogMapper.selectPage(new Page<>(pageNum, pageSize), wrapperMain);

        List<SecondsBetLog> records = page.getRecords();
        for (SecondsBetLog secondsBetLog : records) {
            SecondsConfig secondsConfig = secondsConfigMapper.selectById(secondsBetLog.getSid());
            secondsBetLog.setSeconds(secondsConfig.getSeconds());
            secondsBetLog.setSecondsTime(secondsConfig.getSecondsTime());
        }

        return Response.success(page);
    }

    @Override
    public SecondsBetLog getSecondContract(Integer secondId) {
        SecondsBetLog secondsBetLog = secondsBetLogMapper.selectById(secondId);
        if(null != secondsBetLog){
            SecondsConfig secondsConfig = secondsConfigMapper.selectById(secondsBetLog.getSid());
            secondsBetLog.setSeconds(secondsConfig.getSeconds());
            secondsBetLog.setSecondsTime(secondsConfig.getSecondsTime());

            if(secondsBetLog.getSettleStatus() == 2){
                long time = secondsBetLog.getCreateTime().getTime();
                // 获取当前日期毫秒值
                long nowDate = new Date().getTime();
                long seconds = (nowDate - time) / 1000;

                if(seconds >= secondsConfig.getSeconds()){
                    secondsBetLog.setTiming(0);
                }else {
                    secondsBetLog.setTiming(secondsConfig.getSeconds() - (int) seconds);
                }
            }else {
                secondsBetLog.setTiming(0);
            }
        }
        return secondsBetLog;
    }

}
