package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.ContractConstant;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.*;
import cn.xa87.po.BrokerageRecordPo;
import cn.xa87.rabbit.mapper.*;
import cn.xa87.rabbit.rabbitmq.producer.RabbitMqProducer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class TokenPriceBusiness {
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private ContractMulMapper contractMulMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private RabbitMqProducer rabbitMqProducer;
    @Autowired
    private LeverMapper leverMapper;

    // 止盈止损
    @Async
    public void execute(String msg) {
        try {
            String[] splitResult = msg.split("-");
            BigDecimal price = new BigDecimal(splitResult[0]);
            String pairsName = splitResult[1];
            QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
            wrapperWarehouse.eq("pairs_name", pairsName);
            List<Warehouse> warehouses = warehouseMapper.selectList(wrapperWarehouse);

            QueryWrapper<ContractMul> wrapper = new QueryWrapper<ContractMul>();
            wrapper.eq("pairs_name", pairsName);
            ContractMul contractMul = contractMulMapper.selectOne(wrapper);

            for (Warehouse warehouse : warehouses) {
                if (warehouse.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                    if (warehouse.getTriggerPrice() != null) {
                        BigDecimal triggerPrice = warehouse.getTriggerPrice();
                        if (price.compareTo(triggerPrice) == 1 || price.compareTo(triggerPrice) == 0) {
                            setOrderMatch(warehouse, price, contractMul);
                        }
                    }
                    if (warehouse.getOrdPrice() != null) {
                        BigDecimal ordPrice = warehouse.getOrdPrice();
                        if (price.compareTo(ordPrice) == -1 || price.compareTo(ordPrice) == 0) {
                            setOrderMatch(warehouse, price, contractMul);
                        }
                    }

                } else {
                    if (warehouse.getTriggerPrice() != null) {
                        BigDecimal triggerPrice = warehouse.getTriggerPrice();
                        if (price.compareTo(triggerPrice) == -1 || price.compareTo(triggerPrice) == 0) {
                            setOrderMatch(warehouse, price, contractMul);
                        }
                    }
                    if (warehouse.getOrdPrice() != null) {
                        BigDecimal ordPrice = warehouse.getOrdPrice();
                        if (price.compareTo(ordPrice) == 1 || price.compareTo(ordPrice) == 0) {
                            setOrderMatch(warehouse, price, contractMul);
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Async
    public boolean setOrderMatch(Warehouse warehouse, BigDecimal price, ContractMul contractMul) {
        QueryWrapper<ContractOrder> wrapperOrder = new QueryWrapper<ContractOrder>();
        wrapperOrder.eq("pairs_name", warehouse.getPairsName());
        wrapperOrder.eq("member", warehouse.getMember());
        wrapperOrder.eq("trade_type", warehouse.getTradeType());
        wrapperOrder.ne("order_state", TokenOrderConstant.Order_State.FINAL);
        List<ContractOrder> contractOrders = contractOrderMapper.selectList(wrapperOrder);
        for (ContractOrder contractOrder : contractOrders) {

            BigDecimal hands = contractOrder.getIsContractHands();
            // 手续费
            BigDecimal takeFee = new BigDecimal("0");
            // 手续费
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {

                takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
                        .multiply(contractOrder.getPrice()).multiply(contractMul.getMakerFee());
            } else {
                takeFee = contractOrder.getIsContractHands().multiply(contractMul.getContractMul())
                        .multiply(contractOrder.getPrice()).multiply(contractMul.getTakerFee());
            }
            // activeMargin=activeMargin.subtract(takeFee);
            BigDecimal priceResultUp = price.subtract(contractOrder.getPrice());
            BigDecimal priceResultDown = contractOrder.getPrice().subtract(price);
            BigDecimal matchFee = new BigDecimal("0");
            // 已实现盈亏
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                matchFee = priceResultUp.multiply(contractOrder.getIsContractHands())
                        .multiply(contractMul.getContractMul());
            } else {
                matchFee = priceResultDown.multiply(contractOrder.getIsContractHands())
                        .multiply(contractMul.getContractMul());
            }
            contractOrder.setMatchFee(matchFee);

            Lever lever = leverMapper.selectById(contractOrder.getLeverId());
            // 保证金
            BigDecimal sumPrice = contractMul.getContractMul()
                    .multiply(contractOrder.getContractHands().multiply(contractOrder.getPrice()));
            BigDecimal margin = sumPrice.divide(lever.getLever(), 8, BigDecimal.ROUND_HALF_UP);

            updateBalance(contractOrder, takeFee, margin);

            contractOrder.setMatchPrice(price);
            contractOrder.setOrderState(TokenOrderConstant.Order_State.FINAL);
            contractOrder.setIsContractHands(new BigDecimal("0"));
            contractOrderMapper.updateById(contractOrder);
            if (contractOrder.getTradeType().equals(ContractConstant.Trade_Type.OPEN_UP)) {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_UP);
            } else {
                contractOrder.setTradeType(ContractConstant.Trade_Type.CLOSE_DOWN);
            }
            contractOrder.setContractHands(hands);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            contractOrder.setId(uuid);
            contractOrder.setPrice(price);
            contractOrder.setCreateTime(null);
            contractOrderMapper.insert(contractOrder);
            BrokerageRecordPo brokerageRecordPo = new BrokerageRecordPo(contractOrder.getMember(), "USDT", takeFee);
            rabbitMqProducer.putCurrency(JSONObject.toJSONString(brokerageRecordPo));
        }
        warehouseMapper.deleteById(warehouse.getId());
        return true;
    }

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
            balanceActive.setAssetsBalance(
                    balanceActive.getAssetsBalance().add(margin).subtract(takeFee));
            balanceActive
                    .setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(margin));
            balanceMapper.updateById(balanceActive);
            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceActive.getUserId());
        } else {
            updateBalance(contractOrder, takeFee, margin);
        }
    }

}
