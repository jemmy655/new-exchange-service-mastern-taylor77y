package cn.xa87.rabbit.business;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.TokenOrderConstant;
import cn.xa87.model.ContractMul;
import cn.xa87.model.ContractOrder;
import cn.xa87.model.Warehouse;
import cn.xa87.rabbit.mapper.ContractMulMapper;
import cn.xa87.rabbit.mapper.ContractOrderMapper;
import cn.xa87.rabbit.mapper.WarehouseMapper;
import cn.xa87.rabbit.service.WarehouseService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ContractBusiness {
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private ContractOrderMapper contractOrderMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private ContractMulMapper contractMulMapper;
    @Autowired
    private WarehouseService warehouseService;

    public void execute(String msg) {
        ContractOrder contractOrder = JSONObject.parseObject(msg, ContractOrder.class);
        setWarehouse(contractOrder);
    }


    private void setWarehouse(ContractOrder contractOrder) {
        try {
            ContractMul contractMul = contractMulMapper.selectById(contractOrder.getContractMulId());
            QueryWrapper<Warehouse> wrapperWarehouse = new QueryWrapper<Warehouse>();
            wrapperWarehouse.eq("pairs_name", contractOrder.getPairsName());
            wrapperWarehouse.eq("member", contractOrder.getMember());
            wrapperWarehouse.eq("trade_type", contractOrder.getTradeType());

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
            //总保证金
            warehouse.setMargin(sumMargin);
            // 持仓均价
            BigDecimal avePrice = sumTokenPrice.divide(sumHands.multiply(contractMul.getContractMul()), 8, BigDecimal.ROUND_HALF_UP);
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
            // 获取当前价
            String value = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + contractOrder.getPairsName());
            JSONObject jo = JSONObject.parseObject(value);
            BigDecimal nowPrice = new BigDecimal(jo.getBigDecimal("nowPrice").toString());
            // 未实现盈亏
            BigDecimal subPrice = nowPrice.subtract(avePrice);
            warehouse.setUnProfitLoss(subPrice.multiply(sumHands).multiply(contractMul.getContractMul()));
            //收益
            BigDecimal profit = subPrice.divide(avePrice, 8, BigDecimal.ROUND_HALF_UP).multiply(levers).divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP);
            warehouse.setProfit(profit);
            //收益率
            warehouse.setProfitUp(profit.multiply(new BigDecimal("100")));
            warehouse.setAvgLevel(levers.divide(new BigDecimal(contractOrders.size()), 8, BigDecimal.ROUND_HALF_UP));
            warehouse.setHands(sumHands);
            warehouse.setOrders(new BigDecimal(contractOrders.size()));
            warehouse.setClosePrice(nowPrice);
            warehouse.setMember(contractOrder.getMember());
            //预估强平价格
            //avePrice.multiply(sumHands).multiply(contractMul.getContractMul()).multiply(new , mc)
            warehouseService.saveOrUpdate(warehouse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
