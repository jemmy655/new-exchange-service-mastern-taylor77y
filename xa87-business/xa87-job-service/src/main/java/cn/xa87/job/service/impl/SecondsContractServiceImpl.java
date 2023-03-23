package cn.xa87.job.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.job.mapper.*;
import cn.xa87.job.service.SecondsContractService;
import cn.xa87.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SecondsContractServiceImpl implements SecondsContractService {

    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private SecondsBetLogMapper secondsBetLogMapper;
    @Autowired
    private SecondsConfigMapper secondsConfigMapper;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;

    @Override
    public void secondsContractTask() {

        log.info("定时任务已经启动");
        List<SecondsBetLog> secondsList = secondsBetLogMapper.findSecondsList();
        Random random = new Random();
        for (SecondsBetLog secondsBetLog : secondsList) {
            SecondsConfig secondsConfig = secondsConfigMapper.selectById(secondsBetLog.getSid());
            Member member = memberMapper.selectById(secondsBetLog.getMemberId());
//            String pastTimes = getPastTimes(secondsBetLog.getCreateTime());
//            String[] split = pastTimes.split("-");

            String seconds = secondsConfig.getSecondsTime().toLowerCase();

            switch (seconds) {
                case "s":
                    long time = getTime(secondsBetLog.getCreateTime(), new Date());
                    if(time >= secondsConfig.getSeconds()){
                        contractProcessing(secondsBetLog, secondsConfig, random, member);
                    }
                    break;
                case "m":
                    long minute = getMinute(secondsBetLog.getCreateTime(), new Date());
                    if(minute >= secondsConfig.getSeconds()){
                        contractProcessing(secondsBetLog, secondsConfig, random, member);
                    }
                    break;
                case "h":
                    long hour = getHour(secondsBetLog.getCreateTime(), new Date());
                    if (hour >= secondsConfig.getSeconds()) {
                        contractProcessing(secondsBetLog, secondsConfig, random, member);
                    }
                    break;
                case "d":
                    long days = getDays(secondsBetLog.getCreateTime(), new Date());
                    if (days >= secondsConfig.getSeconds()) {
                        contractProcessing(secondsBetLog, secondsConfig, random, member);
                    }
                    break;
            }
        }
    }


    public void contractProcessing(SecondsBetLog secondsBetLog,SecondsConfig secondsConfig,Random random,Member member){
        // 是否输赢1赢2输
        int beWin;
        // 获取当前虚拟币价格
        if (null == secondsBetLog.getCkType()) {
            secondsBetLog.setCkType("BTC");
        }

        BigDecimal currentPrice = pairsMapper.getCurrentPrice(secondsBetLog.getCkType());
        secondsBetLog.setFinishsAmount(currentPrice);
        // 买入状态 1涨2跌
        if(secondsBetLog.getBuyStatus().equals(1)){
            if(currentPrice.doubleValue() > secondsBetLog.getStartAmount().doubleValue()){
                beWin = 1;
            }else{
                beWin = 2;
            }
        }else {
            if(currentPrice.doubleValue() < secondsBetLog.getStartAmount().doubleValue()){
                beWin = 1;
            }else{
                beWin = 2;
            }
        }

        // 是否控盘：1赢2输
        // 控盘配置,如果控盘配置没有配置，则从1-9随机取一个数来计算
        BigDecimal controlPecent = new BigDecimal("0.00000000");
        if(member.getIsControl() != null){
            if(null != secondsBetLog.getControlPecent()){
                controlPecent  = secondsBetLog.getControlPecent();
            }

            if(controlPecent.compareTo(BigDecimal.valueOf(0)) < 1){
                int randomNumber = random.nextInt(9)+1;
                String str = "0.000"+randomNumber;
                controlPecent = BigDecimal.valueOf(Double.parseDouble(str));

                secondsBetLog.setControlPecent(controlPecent);
            }
            BigDecimal multiply = secondsBetLog.getStartAmount().multiply(controlPecent);

            if(member.getIsControl() == 1){
                beWin = 1;
                // 买涨
                if(secondsBetLog.getBuyStatus().equals(1)){
                    secondsBetLog.setFinishsAmount(secondsBetLog.getStartAmount().add(multiply));
                }else {
                    secondsBetLog.setFinishsAmount(secondsBetLog.getStartAmount().subtract(multiply));
                }
            }else if(member.getIsControl() == 2){
                beWin = 2;
                // 买涨
                if(secondsBetLog.getBuyStatus().equals(1)){
                    secondsBetLog.setFinishsAmount(secondsBetLog.getStartAmount().subtract(multiply));
                }else {
                    secondsBetLog.setFinishsAmount(secondsBetLog.getStartAmount().add(multiply));
                }
            }
        }

        secondsBetLog.setIsWin(beWin);
        // 下注金额
        BigDecimal amount = secondsBetLog.getAmount();
        // 赢以后得奖励比例
        BigDecimal str = secondsConfig.getOdds().divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);
        BigDecimal oddsDecimal = amount.multiply(str);

        // 判断是否输赢
        // 扣除冻结金额
        ContractOrder contractOrder = new ContractOrder();
        contractOrder.setMainCur(secondsBetLog.getCkName());
        contractOrder.setMember(secondsBetLog.getMemberId());
        if(beWin == 1){
            secondsBetLog.setProfit(oddsDecimal);
            updateBalanceContractBetting(contractOrder,amount.add(oddsDecimal),amount,secondsBetLog.getBuyStatus(),beWin,oddsDecimal);
        }else {
            secondsBetLog.setProfit(amount);
            updateBalanceContractBetting(contractOrder,BigDecimal.valueOf(0),amount,secondsBetLog.getBuyStatus(),beWin,oddsDecimal);
        }

        // 结算状态 1已结算
        secondsBetLog.setSettleStatus(1);
        secondsBetLog.setSettleTime(new Date());
        secondsBetLogMapper.updateById(secondsBetLog);
    }

    private void updateBalanceContractBetting(ContractOrder contractOrder, BigDecimal takeFee,BigDecimal reduce,Integer uyStatus, Integer beWin,BigDecimal oddsDecimal) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(
                CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + contractOrder.getMember(), 5000, 50,
                100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapperActive = new QueryWrapper<>();
            wrapperActive.eq("currency", contractOrder.getMainCur());
            wrapperActive.eq("user_id", contractOrder.getMember());
            Balance balanceActive = balanceMapper.selectOne(wrapperActive);
            BigDecimal balance = balanceActive.getAssetsBalance();
            BigDecimal assetsBlockedBalance = balanceActive.getAssetsBlockedBalance();

            // 合约余额
            balanceActive.setAssetsBalance(balance.add(takeFee));
            // 合约冻结余额
            balanceActive.setAssetsBlockedBalance(balanceActive.getAssetsBlockedBalance().subtract(reduce));

            balanceMapper.updateById(balanceActive);

            // 解冻日志
            BalanceRecord balanceRecord1 = new BalanceRecord();
            balanceRecord1.setMemberId(contractOrder.getMember());
            balanceRecord1.setCurrency(contractOrder.getMainCur());
            balanceRecord1.setBalanceType(uyStatus == 1? 42:45);
            balanceRecord1.setFundsType(beWin == 1? 2:1);
            balanceRecord1.setBalanceBefore(assetsBlockedBalance);
            balanceRecord1.setBalanceBack(balanceActive.getAssetsBlockedBalance());
            balanceRecord1.setBalanceDifference(reduce);
            balanceRecord1.setCreateTime(new Date());
            balanceRecord1.setDataClassification(4);
            balanceRecordMapper.insert(balanceRecord1);

            // 盈利
            BalanceRecord balanceRecord = new BalanceRecord();
            balanceRecord.setMemberId(contractOrder.getMember());
            balanceRecord.setCurrency(contractOrder.getMainCur());

            //  uyStatus:1涨2跌, beWin: 1赢2输
            if(beWin == 1){
                balanceRecord.setBalanceType(uyStatus == 1? 43:46);
                balanceRecord.setFundsType(2);
                balanceRecord.setBalanceBefore(balance);
                balanceRecord.setBalanceBack(balanceActive.getAssetsBalance());
                balanceRecord.setBalanceDifference(oddsDecimal);
                balanceRecord.setCreateTime(new Date());
                balanceRecord.setDataClassification(4);
                balanceRecordMapper.insert(balanceRecord);
            }

            redisDistributedLock.releaseLock(
                    CacheConstants.MEMBER_BALANCE_COIN_KEY + CacheConstants.SPLIT + balanceActive.getUserId());
        } else {
            updateBalanceContractBetting(contractOrder, takeFee,reduce, uyStatus, beWin, oddsDecimal);
        }
    }


    public static void main(String[] args) {
//        String dateStr = "2022-09-27 23:59:00";
//        String dateEnt = "2022-09-28 00:03:20";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try
//        {
//            Date date1 = format.parse(dateStr);
//            Date date2 = format.parse(dateEnt);
//            long minute = getTime(date1, date2);
//            System.out.println(minute);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    // 计算两个时间相差的秒数
    public static long getTime(Date startTime, Date endTime){
        long eTime = endTime.getTime();
        long sTime = startTime.getTime();
        return (eTime - sTime) / 1000;
    }

    // 计算两个时间相差的分钟
    public static long getMinute(Date startTime, Date endTime){
        long diff = endTime.getTime() - startTime.getTime();
        return diff / 60 / 1000;
    }

    // 计算两个时间相差的小时
    public static long getHour(Date startTime, Date endTime){
        long diff = endTime.getTime() - startTime.getTime();
        return diff / 60 / 60 / 1000;
    }

    // 计算两个时间相差的天
    public static long getDays(Date startTime, Date endTime){
        return (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24));
    }


    public static String getPastTimes(Date date) {
        try {

            // 时间转换成毫秒值
            long datetime = date.getTime();
            // 获取当前日期毫秒值
            long nowDate = new Date().getTime();
            // 差值
            long miss = nowDate - datetime;
            // 毫秒值处理
            long days = miss / (1000 * 60 * 60 * 24);
            long hours = (miss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (miss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (miss % (1000 * 60)) / 1000;

//            return days + " 天 " + hours + " 时 " + minutes + " 分 " + seconds + " 秒 ";
            return days + "-" + hours + "-" + minutes + "-" + seconds + "-";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
