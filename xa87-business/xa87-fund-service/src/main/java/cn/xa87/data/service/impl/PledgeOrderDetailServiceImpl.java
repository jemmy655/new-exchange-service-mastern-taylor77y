package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.data.mapper.BalanceMapper;
import cn.xa87.data.mapper.BalanceRecordMapper;
import cn.xa87.data.mapper.PledgeOrderDetailMapper;
import cn.xa87.data.mapper.PledgeOrderMapper;
import cn.xa87.data.service.PledgeOrderDetailService;
import cn.xa87.model.Balance;
import cn.xa87.model.BalanceRecord;
import cn.xa87.model.PledgeOrder;
import cn.xa87.model.PledgeOrderDetail;
import cn.xa87.vo.PledgeOrderDetailVo;
import cn.xa87.vo.PledgeOrderVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PledgeOrderDetailServiceImpl extends ServiceImpl<PledgeOrderDetailMapper, PledgeOrderDetail> implements PledgeOrderDetailService {
    @Autowired
    private PledgeOrderMapper pledgeOrderMapper;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Override
    public Boolean setPledgeOrderDetail(PledgeOrderDetailVo pledgeOrderDetailVo) {
        PledgeOrder p= pledgeOrderMapper.selectById(pledgeOrderDetailVo.getOrderId());
        PledgeOrderVo vo=getLoanMoney(new BigDecimal(p.getDeadline()),p.getPledgeName(),p.getBorrowMoney().add(pledgeOrderDetailVo.getMoney()),p.getPledgeMoney().add(pledgeOrderDetailVo.getPledgePrice()));
        //增加 金额 总负债
        p.setBorrowMoney(p.getBorrowMoney().add(pledgeOrderDetailVo.getMoney()));
        p.setTotalIncurDebts(p.getTotalIncurDebts().add(pledgeOrderDetailVo.getMoney()));
        //计算 总利息
        p.setTotalMoney(vo.getTotalMoney());
        //计算 质押率
        p.setPledgeRate(vo.getPledgeRate());
        p.setPredictRefundMoney(vo.getPredictRefundMoney());
        p.setForcePrice(vo.getForcePrice());
        pledgeOrderMapper.updateById(p);

        PledgeOrderDetail detail=new PledgeOrderDetail();
        detail.setOrderId(p.getId());
        detail.setCreationTime(new Date());
        detail.setMoney(pledgeOrderDetailVo.getMoney());
        detail.setPledgeType("币");
        detail.setPledgePrice(pledgeOrderDetailVo.getPledgePrice());
        detail.setPledgeName(pledgeOrderDetailVo.getPledgeName());
        detail.setType(pledgeOrderDetailVo.getType());
        this.baseMapper.insert(detail);
        if(pledgeOrderDetailVo.getType().equals("NEWPLEDGE")){
            openBalance(p.getPledgeName(),p.getMemberId(),pledgeOrderDetailVo.getPledgePrice());
            //减少
        }else if(pledgeOrderDetailVo.getType().equals("RENEWAL")){
            //增加
            updateBalance(p.getBorrowName(),p.getMemberId(),pledgeOrderDetailVo.getMoney());
        }
        return true;
    }
    public BigDecimal nowPrice(String currencyTarget){
        BigDecimal nowPrice = new BigDecimal("1");
        // 目标币价格
        if(!currencyTarget.equals("USDT")){
            String resultTarget = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + currencyTarget.toUpperCase() + "/USDT");
            if(null != resultTarget){
                JSONObject jsonInfo = JSONObject.parseObject(resultTarget);
                nowPrice = jsonInfo.getBigDecimal("nowPrice");
            }
        }
        return nowPrice;
    }
    public PledgeOrderVo getLoanMoney(BigDecimal loanCycle, String pledge_name, BigDecimal borrow_price, BigDecimal pledge_price) {
        PledgeOrderVo vo=new PledgeOrderVo();
        BigDecimal rate=new BigDecimal(0.00);
        try {
            rate=borrow_price.divide(nowPrice(pledge_name),5,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1));
        }catch(Exception e){
            System.out.println("计算出错了");
        }
        BigDecimal totalMoney=borrow_price.multiply(loanCycle.multiply(new BigDecimal(0.048))); //总利息
        vo.setForcePrice(borrow_price);//强平价格
        vo.setPledgeRate(rate); //质押率
        vo.setTotalMoney(totalMoney); //总利息
        vo.setPredictRefundMoney(borrow_price.add(totalMoney)); //预计还款
        return vo;
    }

    /**
     * 减少金额
     *
     * @param userId
     * @param price  要增加的金额
     * @return
     */
    private Boolean openBalance(String buyPairName,String userId, BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", buyPairName);
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain == null) {
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.subtract(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金减少记录
            saveBalanceRecord(userId, buyPairName, 10, 1,
                    assetsBalance, balanceMain.getAssetsBalance(), price);
        } else {
            openBalance(buyPairName,userId, price);
        }
        //不使用
        return true;
    }

    /**
     * 增加金额
     *
     * @param userId
     * @param price
     * @return
     */
    private BigDecimal updateBalance(String buyPairName,String userId, BigDecimal price) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + userId, 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperMain = new QueryWrapper<Balance>();
            wrapperMain.eq("currency", buyPairName);
            wrapperMain.eq("user_id", userId);
            Balance balanceMain = balanceMapper.selectOne(wrapperMain);
            if (balanceMain == null) {
                throw new BusinessException("用户账号不存在");
            }
            BigDecimal assetsBalance = balanceMain.getAssetsBalance();
            balanceMain.setAssetsBalance(assetsBalance.add(price));
            balanceMapper.updateById(balanceMain);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceMain.getUserId());
            // 资金减少记录
            saveBalanceRecord(userId, buyPairName, 11, 1,
                    assetsBalance, balanceMain.getAssetsBalance(), price);
        } else {
            updateBalance(buyPairName,userId, price);
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
