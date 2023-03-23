package cn.xa87.data.walletBusiness.listener;

import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.BalanceConstant;
import cn.xa87.data.mapper.*;
import cn.xa87.data.walletBusiness.exception.RpcServiceException;
import cn.xa87.data.walletBusiness.service.EthereumService;
import cn.xa87.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class WalletJob {

    @Autowired
    private Xa87RedisRepository redisRepository;

    @Autowired
    private CoinTokenMapper coinTokenMapper;

    @Autowired
    private WalletPoolMapper walletPoolMapper;

    @Autowired
    private DepositHistoryMapper depositHistoryMapper;

    @Autowired
    private WithdrawHistoryMapper withdrawHistoryMapper;

    @Autowired
    private ExtractCoinMapper extractCoinMapper;

    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private EthereumService ethereumService;


    public static String  ETH_BLOCK_NUM = "ETH_BLOCK_NUM_EXCHANGE";
    /**
     * 查询ETH节点交易 2 分钟执行
     */
    @Scheduled(cron="*/10 * * * * ?")
    public void depositJob(){

        try {

            //最新高度
            Integer chainBlockHigh = Integer.valueOf(ethereumService.getBlockHigh().intValue());
            Object o = redisRepository.get(ETH_BLOCK_NUM);
            if(null == o){
                redisRepository.set(ETH_BLOCK_NUM,chainBlockHigh-1+"");
            }
            //当前高度
            Integer redisBlockHigh = Integer.valueOf(redisRepository.get(ETH_BLOCK_NUM).toString());
            

            QueryWrapper<CoinToken> objectQueryWrapper = new QueryWrapper<CoinToken>();
            List<CoinToken> coinTokens = coinTokenMapper.selectList(objectQueryWrapper);

            while (redisBlockHigh < chainBlockHigh) {
                final List<EthBlock.TransactionResult> transactionsByBlockNum = ethereumService.getTransactionsByBlockNum(BigInteger.valueOf(redisBlockHigh));
                for (EthBlock.TransactionResult transaction : transactionsByBlockNum) {
                    org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) transaction;
                    if(null != tx){
                        List<WalletPool> addressPools = walletPoolMapper.selectList(new QueryWrapper<>());

                        //区块高度
                        final BigInteger blockHight = tx.getBlockNumber();
                        //转账hash
                        final String hash = tx.getHash();
                        //如果是ETH 收币地址  token 就是合约地址
                        final String toAddress = tx.getTo()+"";
                        //转币地址
                        final String fromAddress = tx.getFrom();
                        String input = tx.getInput();
                        //代币转账
                        if (StringUtils.isNotEmpty(input) && input.length() >= 138) {
                            for (CoinToken coinPairs : coinTokens) {
                                //数据库配置的合约地址
                                String tokenContractAddress = coinPairs.getToken();

                                if (toAddress.equalsIgnoreCase(tokenContractAddress)) {
                                    final String coinName = coinPairs.getCoin();
                                    String data = input.substring(0, 9);
                                    data = data + input.substring(17, input.length());
                                    Function function = new Function("transfer", Arrays.asList(), Arrays.asList(new TypeReference<Address>() {
                                    }, new TypeReference<Uint256>() {
                                    }));
                                    List<Type> params = FunctionReturnDecoder.decode(data, function.getOutputParameters());
                                    String amount = params.get(1).getValue().toString();
                                    Integer unit = coinPairs.getPoint();
                                    final BigDecimal bigDecimal = new BigDecimal(amount).movePointLeft(unit);
                                    // 收币地址
                                    String address = params.get(0).getValue().toString();
                                    //根据收币地址查询收币用户


                                    for (WalletPool wallet : addressPools) {
                                        String address1 = wallet.getAddress();

                                        if (address1.equalsIgnoreCase(address)) {
                                            //收币用户
                                            String member = wallet.getMember();
                                            //增加用户充币资产
                                            if(bigDecimal.compareTo(BigDecimal.ZERO)>0){
                                                DepositHistory depositHistory = new DepositHistory();
                                                depositHistory.setId(UUID.randomUUID().toString());
                                                depositHistory.setAmount(bigDecimal);
                                                depositHistory.setBlockNumber(blockHight.toString());
                                                depositHistory.setFrom(fromAddress);
                                                depositHistory.setTo(toAddress);
                                                depositHistory.setContract(toAddress);
                                                depositHistory.setCoin(coinName);
                                                depositHistory.setHash(hash);
                                                depositHistory.setMember(member);
                                                depositHistory.setCreateTime(new Date());
                                                depositHistoryMapper.insert(depositHistory);
                                                //更新用户资产
                                                final QueryWrapper<Balance> balanceWrapper = new QueryWrapper<Balance>().eq("user_id", member).eq("currency", coinName.toUpperCase()).last("LIMIT 1");
                                                Balance balance = balanceMapper.selectOne(balanceWrapper);
                                                BigDecimal newBalance = balance.getAssetsBalance().add(bigDecimal);
                                                balance.setAssetsBalance(newBalance);
                                                balanceMapper.updateById(balance);
                                            }

                                        }
                                    }
                                }
                            }

                        } else {

                            BigInteger value = tx.getValue();
                            //根据收币地址查询收币用户

                            for (WalletPool wallet : addressPools) {
                                String address1 = wallet.getAddress();
                                if (address1.equalsIgnoreCase(toAddress)) {
                                    BigDecimal bigDecimal = Convert.fromWei(new BigDecimal(value), Convert.Unit.ETHER);
                                    //收币用户
                                    String member = wallet.getMember();
                                    //增加用户充币资产
                                    DepositHistory depositHistory = new DepositHistory();
                                    depositHistory.setId(UUID.randomUUID().toString());
                                    depositHistory.setAmount(bigDecimal);
                                    depositHistory.setBlockNumber(blockHight.toString());
                                    depositHistory.setFrom(fromAddress);
                                    depositHistory.setTo(toAddress);
                                    depositHistory.setContract(toAddress);
                                    depositHistory.setCoin("ETH");
                                    depositHistory.setHash(hash);
                                    depositHistory.setMember(member);
                                    depositHistory.setCreateTime(new Date());
                                    depositHistoryMapper.insert(depositHistory);

                                    final QueryWrapper<Balance> balanceWrapper = new QueryWrapper<Balance>().eq("user_id", member).eq("currency", "ETH").last("LIMIT 1");
                                    Balance balance = balanceMapper.selectOne(balanceWrapper);
                                    BigDecimal newBalance = balance.getAssetsBalance().add(bigDecimal);
                                    balance.setAssetsBalance(newBalance);
                                    balanceMapper.updateById(balance);

                                }

                            }
                        }
                    }
                }
                redisBlockHigh++;
                redisRepository.set(ETH_BLOCK_NUM, redisBlockHigh.toString());
            }
            redisRepository.set(ETH_BLOCK_NUM, redisBlockHigh.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Scheduled(cron="0 0/10 * * * ?")
    public void updateJob() {

        List<WalletPool> walletPools = walletPoolMapper.selectList(new QueryWrapper<>());

        List<CoinToken> coinTokens = coinTokenMapper.selectList(new QueryWrapper<>());

        for (WalletPool walletPool : walletPools) {

            String address = walletPool.getAddress();
            String member = walletPool.getMember();
            try {
                //更新ETH链上资产
                BigDecimal balance = ethereumService.getBalance(address);

                QueryWrapper<Balance> balanceQueryWrapper = new QueryWrapper<>();
                balanceQueryWrapper.eq("user_id", member);
                balanceQueryWrapper.eq("currency", "ETH");
                Balance userBalance = balanceMapper.selectOne(balanceQueryWrapper);
                userBalance.setChainBalance(balance);
                balanceMapper.updateById(userBalance);

                //更新ETH 代笔 链上资产
                for (CoinToken token : coinTokens) {
                    String coinName = token.getCoin();
                    String coinConstract = token.getToken();
                    final Integer point = token.getPoint();
                    BigDecimal tokenBalance = ethereumService.getTokenBalance(address, coinConstract);
                    BigDecimal bigDecimal = tokenBalance.movePointLeft(point.intValue());
                    QueryWrapper<Balance> tokenBalanceQueryWrapper = new QueryWrapper<>();
                    tokenBalanceQueryWrapper.eq("user_id", member);
                    tokenBalanceQueryWrapper.eq("currency", coinName);
                    Balance tokenBalance2 = balanceMapper.selectOne(tokenBalanceQueryWrapper);
                    if(null != tokenBalance2){
                        tokenBalance2.setChainBalance(bigDecimal);
                        balanceMapper.updateById(tokenBalance2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    @Scheduled(cron="0 0/10 * * * ?")
    public void job(){
        QueryWrapper<WithdrawHistory> withdrawHistoryQueryWrapper = new QueryWrapper<>();
        withdrawHistoryQueryWrapper.eq("status", WithdrawHistory.WITHDRAW_CHAIN_ING);
        final List<WithdrawHistory> withdrawHistories = withdrawHistoryMapper.selectList(withdrawHistoryQueryWrapper);
        try {
            //当前高度
            for (WithdrawHistory withdrawHistory : withdrawHistories) {
                String hash = withdrawHistory.getTxHash();
                TransactionReceipt transactionReceipt = ethereumService.getTransactionReceipt(hash);
                String status = transactionReceipt.getStatus();
                if (null != status && "0x1".equals(status)) {
                    BigInteger blockNumber = transactionReceipt.getBlockNumber();
                    withdrawHistory.setStatus(WithdrawHistory.WITHDRAW_SUCCESS);
                    withdrawHistory.setBlockNumber(blockNumber.toString());
                    withdrawHistoryMapper.updateById(withdrawHistory);

                    QueryWrapper<ExtractCoin> query = new QueryWrapper<ExtractCoin>();
                    query.eq("hex", hash);
                    ExtractCoin extractCoin = extractCoinMapper.selectOne(query);
                    extractCoin.setState(BalanceConstant.Extract_State.CLOSE);
                    extractCoinMapper.updateById(extractCoin);

                }
            }
        } catch (RpcServiceException e) {
            e.printStackTrace();
        }

    }
}
