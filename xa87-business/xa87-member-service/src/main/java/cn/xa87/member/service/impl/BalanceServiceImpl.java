package cn.xa87.member.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.DictionariesConstant;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.BalanceConstant;
import cn.xa87.constant.CoinConstant;
import cn.xa87.member.mapper.*;
import cn.xa87.member.product.RegisterProducer;
import cn.xa87.member.service.BalanceService;
import cn.xa87.model.*;
import cn.xa87.vo.ExtractCoinVo;
import cn.xa87.vo.MemberRechargeVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static cn.xa87.common.constants.BrokerQueueTypeConstant.BROKER_QUEUE_TYPE;
import static cn.xa87.common.constants.BrokerQueueTypeConstant.TRANSFER;
import static cn.xa87.common.constants.DictionariesConstant.*;

@Slf4j
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private WalletPoolMapper walletPoolMapper;
    @Resource
    private ExtractCoinMapper extractCoinMapper;
    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private Xa87RedisRepository redisRepository;
    @Resource
    private DictionariesMapper dictionariesMapper;
    @Resource
    private TansferInfoMapper tansferInfoMapper;
    @Resource
    private RegisterProducer registerProducer;
    @Resource
    private DepositHistoryMapper depositHistoryMapper;
    @Resource
    private PairsMapper pairsMapper;
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Resource
    private MemberRechargeMapper memberRechargeMapper;
    @Resource
    private MoneyAccountMapper moneyAccountMapper;
    @Resource
    private MemberCurrencyConfigMapper memberCurrencyConfigMapper;
    @Resource
    private BalanceRecordMapper balanceRecordMapper;
    @Resource
    private ExchangeRecordMapper exchangeRecordMapper;
    @Resource
    private BillingAddressMapper billingAddressMapper;
    @Override
    public List<Balance> getBalances(String currency, String userId) {
        QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
        if (Strings.isNotBlank(currency)) {
            wrapper.eq("currency", currency);
        }
        wrapper.eq("user_id", userId);
        List<Balance> balance = balanceMapper.selectList(wrapper);
        return balance;
    }

    @Override
    public Boolean transferBalances(String currency, String userId, BigDecimal balance,
                                    BalanceConstant.Move_Type moveType) {
//        moveBalance(currency, userId, balance, moveType);
        return true;
    }

    @Override
    public String getETHAddress(String member ,String coinname) {

        String only_one_deposit_address = sysConfigMapper.selectByParamKey(SysConfigMapper.ONLY_ONE_DEPOSIT_ADDRESS);
        if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(only_one_deposit_address,"OK")){
            WalletPool deposit_account = walletPoolMapper.getMainAccount(WalletPoolMapper.MAIN_DEPOSIT_ACCOUNT);
            return deposit_account.getAddress();
        }
        QueryWrapper<WalletPool> objectQueryWrapper = new QueryWrapper<WalletPool>();
        objectQueryWrapper.eq("member", member);
        objectQueryWrapper.eq("coin", coinname);
        WalletPool walletPool = walletPoolMapper.selectOne(objectQueryWrapper);
        return walletPool.getAddress();
    }

//    private void moveBalance(String currency, String userId, BigDecimal balance, BalanceConstant.Move_Type moveType) {
//        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
//        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId,
//                5000, 50, 100);
//        if (lock_coin) {
//            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
//            wrapper.eq("user_id", userId);
//            wrapper.eq("currency", currency);
//            Balance userBalance = balanceMapper.selectOne(wrapper);
//            if (moveType.equals(BalanceConstant.Move_Type.BALANCE_MOVE_TOKEN)) {
//                if (userBalance.getBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setBalance(userBalance.getBalance().subtract(balance));
//                userBalance.setTokenBalance(userBalance.getTokenBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.TOKEN_MOVE_BALANCE)) {
//                if (userBalance.getTokenBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                QueryWrapper<Warehouse> wrapWarehouse = new QueryWrapper<Warehouse>();
//                wrapWarehouse.eq("member", userId);
//                List<Warehouse> list = warehouseMapper.selectList(wrapWarehouse);
//                if (!list.isEmpty()) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.PLEASE_CLOSE_THE_CONTRACT_ORDER_FIRST.getMessage());
//                }
//                userBalance.setBalance(userBalance.getBalance().add(balance));
//                userBalance.setTokenBalance(userBalance.getTokenBalance().subtract(balance));
//
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.FB_MOVE_BALANCE)) {
//                if (userBalance.getFbBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setFbBalance(userBalance.getFbBalance().subtract(balance));
//                userBalance.setBalance(userBalance.getBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.BALANCE_MOVE_FB)) {
//                if (userBalance.getBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setFbBalance(userBalance.getFbBalance().add(balance));
//                userBalance.setBalance(userBalance.getBalance().subtract(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.FB_MOVE_TOKEN)) {
//                if (userBalance.getFbBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setTokenBalance(userBalance.getTokenBalance().add(balance));
//                userBalance.setFbBalance(userBalance.getFbBalance().subtract(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.TOKEN_MOVE_FB)) {
//                if (userBalance.getTokenBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setTokenBalance(userBalance.getTokenBalance().subtract(balance));
//                userBalance.setFbBalance(userBalance.getFbBalance().add(balance));
//            }
//
//            if (moveType.equals(BalanceConstant.Move_Type.BALANCE_MOVE_ASSETS)) {
//                if (userBalance.getBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setBalance(userBalance.getBalance().subtract(balance));
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.ASSETS_MOVE_BALANCE)) {
//                if (userBalance.getAssetsBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(balance));
//                userBalance.setBalance(userBalance.getBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.FB_MOVE_ASSETS)) {
//                if (userBalance.getFbBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setFbBalance(userBalance.getFbBalance().subtract(balance));
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.ASSETS_MOVE_FB)) {
//                if (userBalance.getAssetsBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(balance));
//                userBalance.setFbBalance(userBalance.getFbBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.TOKEN_MOVE_ASSETS)) {
//                if (userBalance.getTokenBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setTokenBalance(userBalance.getTokenBalance().subtract(balance));
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
//            }
//            if (moveType.equals(BalanceConstant.Move_Type.ASSETS_MOVE_TOKEN)) {
//                if (userBalance.getAssetsBalance().compareTo(balance) == -1) {
//                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
//                }
//                userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(balance));
//                userBalance.setTokenBalance(userBalance.getTokenBalance().add(balance));
//            }
//
//            balanceMapper.updateById(userBalance);
//            TansferInfo tansferInfo = new TansferInfo(userId, moveType, currency, balance, userBalance.getTokenBalance());
//            tansferInfoMapper.insert(tansferInfo);
//            sendQueueTransfer(tansferInfo);
//            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
//        } else {
//            moveBalance(currency, userId, balance, moveType);
//        }
//    }


    /**
     * 划转发送队列
     *
     * @param transfer
     */
    private void sendQueueTransfer(TansferInfo transfer) {
        Map<String, Object> map = new HashMap<>();
        map.put(BROKER_QUEUE_TYPE, TRANSFER);
        map.put("memberId", transfer.getMember());
        map.put("transferType", transfer.getType());
        map.put("currency", transfer.getCurrency());
        map.put("fee", transfer.getFee());
        map.put("balance", transfer.getBalance());
        map.put("createTime", System.currentTimeMillis());
        registerProducer.putBrokerManage(JSONObject.toJSONString(map));
    }

    // 计算两个时间相差的分钟
    public static long getMinute(Date startTime, Date endTime){
        long diff = endTime.getTime() - startTime.getTime();
        return diff / 60 / 1000;
    }

    /**
     *
     * @param currency 币名称
     * @param member    用户id
     * @param wallet    提币地址
     * @param balance   提币数量
     * @param type      类型
     * @param chainName 链子名称
     * @param mccId
     * @param baId       提币id
     * @return
     */
    @Override
    public Boolean extractCoin(String currency, String member, String wallet, BigDecimal balance,Integer type, String chainName, Integer mccId,Integer baId) {
        Member memberInfo = memberMapper.selectById(member);
        if (memberInfo.getType() != null) {
            if (memberInfo.getType().equals("INTERNAL")) {
                throw new BusinessException(AjaxResultEnum.INTERNAL_ACCOUNTS_ARE_NOT_ALLOWED.getMessage());
            }
        }


        // 判断上一笔未处理提现居然当前时间
        QueryWrapper<ExtractCoin> wrapper = new QueryWrapper<>();
        wrapper.eq("member", member);
        wrapper.eq("state", "CREATE");
        wrapper.orderByDesc("create_time");
        List<ExtractCoin> extractCoins = extractCoinMapper.selectList(wrapper);
        if(extractCoins.size() > 0){
            // 如果未处理提现订单超过3笔则不允许发起提现
            if(extractCoins.size() >= 3){
                throw new BusinessException(AjaxResultEnum.T_M_U_O_P_C_C_S_F_P.getMessage());
            }

            ExtractCoin extractCoin = extractCoins.get(0);
            long minute = getMinute(extractCoin.getCreateTime(), new Date());
            if(minute < 10){
                throw new BusinessException(AjaxResultEnum.W_A_C_P_T_L.getMessage());
            }
        }


        final Pairs pairs = pairsMapper.selectPairByCoin(currency);
        //BigDecimal tradeMin = pairs.getTradeMin();
        final String isDw = pairs.getIsDw();
        // 最小提现金额
        BigDecimal withdrawMin = new BigDecimal(0);
        // 提现手续费
        BigDecimal withdrawFee = new BigDecimal(0);
        if(currency.equals("USDT")){
            QueryWrapper<Dictionaries> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("ukey", DictionariesConstant.USDT_EXTRACT_MIN_LIMIT);
            Dictionaries dictionaries1 = dictionariesMapper.selectOne(wrapper1);
            if(null != dictionaries1){
                withdrawMin = new BigDecimal(dictionaries1.getUvalue());
            }

            QueryWrapper<Dictionaries> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("ukey", DictionariesConstant.USDT_EXTRACT_HANDLING);
            Dictionaries dictionaries2 = dictionariesMapper.selectOne(wrapper2);
            if(null != dictionaries2) {
                withdrawFee = new BigDecimal(dictionaries2.getUvalue());
            }
        }else {
            withdrawMin  = pairs.getWithdrawMin();
            withdrawFee = pairs.getWithdrawFee();
        }

        if(!StringUtils.containsIgnoreCase(isDw,"2")){
            throw new BusinessException(AjaxResultEnum.WITHDRAWAL_CHANNEL_IS_TEMPORARILY_CLOSED.getMessage());
        }

//        if (balance.compareTo(withdrawMin) == -1) {
//            throw new BusinessException(AjaxResultEnum.WITHDRAWAL_AMOUNT_IS_LESS_THAN_THE_MINIMUM_AMOUNT.getMessage());
//        }
        QueryWrapper<Warehouse> wrapWarehouse = new QueryWrapper<Warehouse>();
        wrapWarehouse.eq("member", member);
        List<Warehouse> list = warehouseMapper.selectList(wrapWarehouse);
        if (!list.isEmpty()) {
            throw new BusinessException(AjaxResultEnum.PLEASE_CLOSE_THE_CONTRACT_ORDER_FIRST.getMessage());
        }
        extractCoin(member, currency, balance);

        ExtractCoin extractCoin = new ExtractCoin();
        extractCoin.setMember(member);
        extractCoin.setBalance(balance.subtract(withdrawFee));
        extractCoin.setCurrency(currency);
        extractCoin.setHandlingFee(withdrawFee);
        extractCoin.setState(BalanceConstant.Extract_State.CREATE);
        extractCoin.setUpdateTime(new Date());
        extractCoin.setCreateTime(new Date());
        extractCoin.setExtractTime(new Date());
        extractCoin.setType(type);

        if(type == 1){
            BillingAddress billingAddress = billingAddressMapper.selectById(baId);
            if(null != billingAddress){
                extractCoin.setWallet(billingAddress.getAddress());
                extractCoin.setChainName(billingAddress.getCurrencyType());
            }

        }else {

            MemberCurrencyConfig memberCurrencyConfig = memberCurrencyConfigMapper.selectById(mccId);
            if(null == memberCurrencyConfig){
                throw new BusinessException(AjaxResultEnum.ILLEGAL_OPERATION.getMessage());
            }

            // 保存当前银行卡信息
            extractCoin.setBankUserName(memberInfo.getBankUserName());
            extractCoin.setBankName(memberInfo.getBankName());
            extractCoin.setBankAddress(memberInfo.getBankAddress());
            extractCoin.setBankCard(memberInfo.getBankCard());
            extractCoin.setExchangeRate(memberCurrencyConfig.getRate());
            extractCoin.setCurrencySymbol(memberCurrencyConfig.getMark());
            extractCoin.setAmount(extractCoin.getBalance().multiply(memberCurrencyConfig.getRate()));
        }
        extractCoinMapper.insert(extractCoin);
        return true;
    }

    private void extractCoin(String member, String currencyName, BigDecimal balance) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member,
                5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            wrapper.eq("user_id", member);
            wrapper.eq("currency", currencyName);
            Balance userBalance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = userBalance.getAssetsBalance();
            if (userBalance.getAssetsBalance().compareTo(balance) == -1) {
                redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
            }
            userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(balance));
            balanceMapper.updateById(userBalance);

            // 减少可用
            BalanceRecord balanceRecord = new BalanceRecord();
            balanceRecord.setMemberId(member);
            balanceRecord.setCurrency(currencyName);
            balanceRecord.setBalanceType(53);
            balanceRecord.setFundsType(1);
            balanceRecord.setBalanceBefore(assetsBalance);
            balanceRecord.setBalanceBack(userBalance.getAssetsBalance());
            balanceRecord.setBalanceDifference(balance);
            balanceRecord.setCreateTime(new Date());
            balanceRecord.setDataClassification(5);
            balanceRecordMapper.insert(balanceRecord);

            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
        } else {
            extractCoin(member, currencyName, balance);
        }
    }

    /**
     *
     * @param member
     * @param currency
     * @param coinType  ASSETS
     * @return
     */
    @Override
    public Map<String, Object> getBalanceList(String member, String currency, CoinConstant.Coin_Type coinType) {
        List<Balance> balances = balanceMapper.getBalanceList(member, currency, coinType.getCoinType());
        balances.addAll(0,balanceMapper.getBalanceMainList(member, currency));

//        Collections.reverse(balances);
        Map<String, Object> sumPriceMap = new HashMap<String, Object>();
        BigDecimal[] sumChPrice = {new BigDecimal("0")};
        BigDecimal[] sumPrice = {new BigDecimal("0")};
        balances.stream().forEach(balance -> {
//            BigDecimal sumBalance = balance.getBalance().add(balance.getBlockedBalance());
//            BigDecimal sumTokenBalance = balance.getTokenBalance().add(balance.getTokenBlockedBalance());
//            BigDecimal sumFbBalance = balance.getFbBalance().add(balance.getFbBlockedBalance());
            BigDecimal sumAssetsBalance = balance.getAssetsBalance().add(balance.getAssetsBlockedBalance());
            BigDecimal sumAeleaseBalance = balance.getRaiseBalance();

            BigDecimal chPrice = new BigDecimal(6.7364);
            BigDecimal price = new BigDecimal(1);
            /*String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + balance.getCurrency().toUpperCase() + "/USDT");

            if (result == null) {
                result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + balance.getCurrency().toUpperCase());
                if(!balance.getCurrency().toUpperCase().equals("USDT")){
                    JSONObject jsonInfo = JSONObject.parseObject(result);
                    if (jsonInfo == null) {
                        chPrice = new BigDecimal("0");
                        price = new BigDecimal("0");
                    } else {
                        chPrice = jsonInfo.getBigDecimal("close");
                        price = new BigDecimal("1");
                    }
                }else {
                    chPrice = new BigDecimal("6.7364");
                    price = new BigDecimal("1");
                }

            } else {
                JSONObject jsonInfo = JSONObject.parseObject(result);
                chPrice = jsonInfo.getBigDecimal("chPrice");
                price = jsonInfo.getBigDecimal("nowPrice");
            }*/
            // 币币
//            if (coinType.equals(CoinConstant.Coin_Type.SPOT) && sumBalance.compareTo(new BigDecimal("0")) == 1) {
//                balance.setScaleBalanceUsdt(sumBalance.multiply(price));
//                balance.setScaleBalanceCny(sumBalance.multiply(chPrice));
//                sumChPrice[0] = sumChPrice[0].add(sumBalance.multiply(chPrice));
//                sumPrice[0] = sumPrice[0].add(sumBalance.multiply(price));
//            }
//            // 合约
//            if (coinType.equals(CoinConstant.Coin_Type.CONTRACT) && sumTokenBalance.compareTo(new BigDecimal("0")) == 1) {
//                balance.setScaleTokenUsdt(sumTokenBalance.multiply(price));
//                balance.setScaleTokenCny(sumTokenBalance.multiply(chPrice));
//                sumChPrice[0] = sumChPrice[0].add(sumTokenBalance.multiply(chPrice));
//                sumPrice[0] = sumPrice[0].add(sumTokenBalance.multiply(price));
//            }
//            // 法币
//            if (coinType.equals(CoinConstant.Coin_Type.FB) && sumFbBalance.compareTo(new BigDecimal("0")) == 1) {
//                balance.setScaleFbUsdt(sumFbBalance.multiply(price));
//                balance.setScaleFbCny(sumFbBalance.multiply(chPrice));
//                sumChPrice[0] = sumChPrice[0].add(sumFbBalance.multiply(chPrice));
//                sumPrice[0] = sumPrice[0].add(sumFbBalance.multiply(price));
//            }
            // 充提
            if (coinType.equals(CoinConstant.Coin_Type.ASSETS) && sumAssetsBalance.compareTo(new BigDecimal("0")) == 1) {
                balance.setScaleAssetsUsdt(sumAssetsBalance.multiply(price));
                balance.setScaleAssetsCny(sumAssetsBalance.multiply(chPrice));
                sumChPrice[0] = sumChPrice[0].add(sumAssetsBalance.multiply(chPrice));
                sumPrice[0] = sumPrice[0].add(sumAssetsBalance.multiply(price));
            }
            //
            if (coinType.equals(CoinConstant.Coin_Type.RELEASE) && sumAeleaseBalance.compareTo(new BigDecimal("0")) == 1) {
                balance.setScaleAssetsUsdt(sumAeleaseBalance.multiply(price));
                balance.setScaleAssetsCny(sumAeleaseBalance.multiply(chPrice));
                sumChPrice[0] = sumChPrice[0].add(sumAeleaseBalance.multiply(chPrice));
                sumPrice[0] = sumPrice[0].add(sumAeleaseBalance.multiply(price));
            }


        });
        sumPriceMap.put("cnyPrice", sumChPrice[0]);
        sumPriceMap.put("usdtPrice", sumPrice[0]);
        sumPriceMap.put("balances", balances);
        return sumPriceMap;
    }


    @Override
    public Response getRechargeRecording(String memberId, String currency, Integer pageNum, Integer pageSize) {
        IPage<DepositHistory> page = depositHistoryMapper.selectRechargeRecord(new Page<>(pageNum, pageSize), memberId, currency);
        return Response.success(page);
    }


    @Override
    public Response getWithdrawRecording(String memberId, String state, String currency, Integer pageNum, Integer pageSize) {
        IPage<ExtractCoinVo> coinIPage =
                extractCoinMapper.selectExtractRecording(
                        new Page<>(pageNum, pageSize), memberId, state, currency);
        return Response.success(coinIPage);
    }

    @Override
    public Response selectWithdrawCurrency(String memberId) {
        List<String> list = extractCoinMapper.selectWithdrawCurrency(memberId);
        return Response.success(list);
    }

    @Override
    public Response getTransferRecording(String memberId, String type, Integer pageNum, Integer pageSize) {
        QueryWrapper<TansferInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("member", memberId)
                .select("type", "fee", "currency", "create_time")
                .orderByDesc("create_time");
        if (StringUtils.isNotBlank(type)) {
            wrapper.eq("type", type);
        }
        IPage<TansferInfo> page = tansferInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Response.success(page);
    }


    @Override
    public Object rechargeCurrency(String memberId,BigDecimal amount,String currency, String chainName, String paymentVoucher) {
        QueryWrapper<MemberRecharge> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        List<MemberRecharge> memberRecharges = memberRechargeMapper.selectList(wrapper);
        if (memberRecharges.size() > 0) {
            // 如果上一笔充值订单还未处理则只能稍后在发起
            MemberRecharge memberRecharge = memberRecharges.get(0);
            long minute = getMinute(memberRecharge.getCreateTime(), new Date());
            if(minute < 3){
                throw new BusinessException(AjaxResultEnum.B_P_T_L.getMessage());
            }
        }

        MemberRecharge memberRecharge = new MemberRecharge();
        memberRecharge.setMemberId(memberId);
        memberRecharge.setAmount(amount);
        memberRecharge.setCreateTime(new Date());
        memberRecharge.setCurrency(currency);
        memberRecharge.setStatus(1);
        memberRecharge.setChainName(chainName);
        memberRecharge.setPaymentVoucher(paymentVoucher);
        memberRechargeMapper.insert(memberRecharge);
        return "SUCCESS";
    }

    @Override
    public Response rechargeCurrencyRecord(String memberId, String currency, Integer pageNum, Integer pageSize) {

        IPage<MemberRecharge> page = memberRechargeMapper.rechargeCurrencyRecord(new Page<>(pageNum, pageSize), memberId, currency);
        return Response.success(page);
    }

    @Override
    public List<MoneyAccount> getRechargeWallet(Integer type) {
        QueryWrapper<MoneyAccount> wrapper = new QueryWrapper<MoneyAccount>();
        wrapper.eq("type", type);
        List<MoneyAccount> list = moneyAccountMapper.selectList(wrapper);
//        List<MoneyAccount> list = new ArrayList<>();
//        //////////////////////////////////////////// MBTC-NEW  //////////////////////////////////////////////
//        MoneyAccount moneyAccount5 = new MoneyAccount();
//        moneyAccount5.setAccount("0x6c20dCA0b80A04E457b7b6F22Ee4F8D7063A36B9");
//        moneyAccount5.setBankname("0x6c20dCA0b80A04E457b7b6F22Ee4F8D7063A36B9");
//        moneyAccount5.setUsername("ERC");
//        moneyAccount5.setId(23);
//        moneyAccount5.setStatus(1);
//        moneyAccount5.setType(1);
//        list.add(moneyAccount5);
//        MoneyAccount moneyAccount6 = new MoneyAccount();
//        moneyAccount6.setAccount("TCJuUavJWp7y7rBwteaGf6ZEBWon9gy6qY");
//        moneyAccount6.setBankname("TCJuUavJWp7y7rBwteaGf6ZEBWon9gy6qY");
//        moneyAccount6.setUsername("TRC");
//        moneyAccount6.setId(25);
//        moneyAccount6.setStatus(1);
//        moneyAccount6.setType(1);
//        list.add(moneyAccount6);
//        MoneyAccount moneyAccount7 = new MoneyAccount();
//        moneyAccount7.setAccount("3DBUtDoZMiGdNihU9KjeW7XUmuURSQbmYH");
//        moneyAccount7.setBankname("3DBUtDoZMiGdNihU9KjeW7XUmuURSQbmYH");
//        moneyAccount7.setUsername("BTC");
//        moneyAccount7.setId(22);
//        moneyAccount7.setStatus(1);
//        moneyAccount7.setType(1);
//        list.add(moneyAccount7);
        return list;
    }

    @Override
    public Response getRechargeConfiguration() {
        Map<String,String> responseMap = new HashMap<>();

        QueryWrapper<Dictionaries> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("ukey", DictionariesConstant.USDT_EXTRACT_MIN_LIMIT);
        Dictionaries dictionaries1 = dictionariesMapper.selectOne(wrapper1);
        if(null != dictionaries1){
            responseMap.put("minimumWithdrawal",dictionaries1.getUvalue());
        }

        QueryWrapper<Dictionaries> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("ukey", DictionariesConstant.USDT_EXTRACT_HANDLING);
        Dictionaries dictionaries2 = dictionariesMapper.selectOne(wrapper2);
        if(null != dictionaries2) {
            responseMap.put("feeWithdrawal",dictionaries2.getUvalue());
        }
        return Response.success(responseMap);
    }

    @Override
    public Response newGetRechargeConfiguration(String key) {
        Map<String,String> responseMap = new HashMap<>();

        QueryWrapper<Dictionaries> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("ukey", key);
        Dictionaries dictionaries1 = dictionariesMapper.selectOne(wrapper1);
        if(null != dictionaries1){
            responseMap.put("value",dictionaries1.getUvalue());
        }
        return Response.success(responseMap);
    }

    /**
     * 币币兑换
     * @param member 用户ID
     * @param currency 原始币种
     * @param currencyTarget 目标币种
     * @param quantity 兑换数量
     */
    @Override
    public Object currencyExchange(String member, String currency, String currencyTarget, BigDecimal quantity) {
        BigDecimal price = new BigDecimal("1");
        BigDecimal priceTarget = new BigDecimal("1");

        if(quantity.compareTo(new BigDecimal("0")) < 0){
            throw new BusinessException(AjaxResultEnum.QUANTITY_IS_EMPTY.getMessage());
        }

        // 原始币价格
        if(!currency.equals("USDT")){
            String result = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + currency.toUpperCase() + "/USDT");
            if(null != result){
                JSONObject jsonInfo = JSONObject.parseObject(result);
                price = jsonInfo.getBigDecimal("nowPrice");
            }
        }

        // 目标币价格
        if(!currencyTarget.equals("USDT")){
            String resultTarget = redisRepository.get(CacheConstants.PRICE_HIG_LOW_KEY + currencyTarget.toUpperCase() + "/USDT");
            if(null != resultTarget){
                JSONObject jsonInfo = JSONObject.parseObject(resultTarget);
                priceTarget = jsonInfo.getBigDecimal("nowPrice");
            }
        }

        // 原始币换算成USDT
        BigDecimal calculatePrice = quantity.multiply(price);

        // 原始币换算成目标可得多少数量
        BigDecimal afterExchangeQuantity = calculatePrice.divide(priceTarget,8, BigDecimal.ROUND_HALF_UP);

        // 资金扣减
        extractCurrencyExchange(member,currency,quantity,1);

        // 资金增加
        extractCurrencyExchange(member,currencyTarget,afterExchangeQuantity,2);

        // 保存币币兑换记录
        ExchangeRecord record = new ExchangeRecord();
        record.setMemberId(member);
        record.setCurrency(currency);
        record.setCurrencyTarget(currencyTarget);
        record.setQuantity(quantity);
        record.setAfterExchangeQuantity(afterExchangeQuantity);

        exchangeRecordMapper.insert(record);
        return null;
    }

    @Override
    public Response currencyExchangeRecord(String member, Integer pageNum, Integer pageSize) {
        QueryWrapper<ExchangeRecord> wrapper = new QueryWrapper<ExchangeRecord>();
        wrapper.eq("member_id", member);
        wrapper.orderByDesc("create_time");

        IPage<ExchangeRecord> page = exchangeRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Response.success(page);
    }

    private void extractCurrencyExchange(String member, String currencyName, BigDecimal balance, Integer type) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member,
                5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            wrapper.eq("user_id", member);
            wrapper.eq("currency", currencyName);
            Balance userBalance = balanceMapper.selectOne(wrapper);
            BigDecimal assetsBalance = userBalance.getAssetsBalance();

            if(type == 1){
                if (userBalance.getAssetsBalance().compareTo(balance) == -1) {
                    redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
                    throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.getMessage());
                }
                userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(balance));
            }else {
                userBalance.setAssetsBalance(userBalance.getAssetsBalance().add(balance));
            }

            balanceMapper.updateById(userBalance);

            // 减少可用
            BalanceRecord balanceRecord = new BalanceRecord();
            balanceRecord.setMemberId(member);
            balanceRecord.setCurrency(currencyName);
            balanceRecord.setBalanceType(type == 1? 56:57);
            balanceRecord.setFundsType(type == 1? 1:2);
            balanceRecord.setBalanceBefore(assetsBalance);
            balanceRecord.setBalanceBack(userBalance.getAssetsBalance());
            balanceRecord.setBalanceDifference(balance);
            balanceRecord.setCreateTime(new Date());
            balanceRecord.setDataClassification(3);
            balanceRecordMapper.insert(balanceRecord);

            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + member);
        }
    }

}
