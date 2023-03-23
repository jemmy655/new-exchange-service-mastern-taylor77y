package cn.xa87.data.walletBusiness.service;

import cn.xa87.data.walletBusiness.exception.RpcServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class EthereumService {

    private final Logger log = LoggerFactory.getLogger(EthereumService.class);



    private static BigInteger GAS_LIMIT = BigInteger.valueOf(21000); // GasLimit set 22000
    private static BigInteger GAS_LIMIT_USDT = BigInteger.valueOf(60000); // GasLimit set 60000


    public String ETH_URL = "http://154.218.6.51:18545";

    public String KEYSTORE_FOLDER = "/root/.ethereum/keystore";


    public Web3j web3j = Web3j.build(new HttpService(ETH_URL));

    public EthereumService(String ethUrl) {

        web3j = Web3j.build(new HttpService(ethUrl));

        this.ETH_URL = ETH_URL;
    }


    public Web3j getWeb3j() {
        return web3j;
    }

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }


    public List<String> getAccountlist() throws RpcServiceException {
        List<String> accounts = new ArrayList<>();
        try {
            String[] list = new File(KEYSTORE_FOLDER ).list();
            for (String filename : list) {
                String address = "0x" + filename.substring(filename.lastIndexOf("--") + 2, filename.lastIndexOf("."));
                accounts.add(address);
            }
        } catch (Exception e) {
            log.error("获取账户列表异常:{}", e.getMessage());
            throw new RpcServiceException("获取账户列表异常");
        }

        return  accounts;
    }

    public BigInteger getBlockNumber() throws IOException {
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        BigInteger blockNumber = ethBlockNumber.getBlockNumber();
        return blockNumber;
    }

    /**
     * 创建帐户
     */
    public String createAccount(String password) throws RpcServiceException {

        try {
            File file = new File(KEYSTORE_FOLDER);
            if (!file.exists()) {
                file.mkdirs();
            }
            String address = WalletUtils.generateLightNewWalletFile(password, file);
            address = "0x" + address.substring(address.lastIndexOf("--") + 2, address.lastIndexOf("."));
            return address;
        } catch (Exception e) {
            log.error("创建帐户异常:{}", e.getMessage());
            throw new RpcServiceException("创建帐户异常");
        }
    }

    /**
     * 获取帐户余额
     */
    public BigDecimal getBalance(String address) throws RpcServiceException {

        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, defaultBlockParameter).send();
            BigInteger balance = ethGetBalance.getBalance();
            BigDecimal bigDecimal = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
            return bigDecimal;
        } catch (Exception e) {
            log.error("获取账户余额异常:{}", e.getMessage());
            throw new RpcServiceException("获取账户余额异常");
        }
    }

    public BigDecimal getTokenBalance(String address, String contractAddress) {
        BigDecimal balance = BigDecimal.ZERO;
        try {
            Function fn = new Function("balanceOf", Arrays.asList(new Address(address)), Collections.<TypeReference<?>>emptyList());
            String data = FunctionEncoder.encode(fn);
            Transaction transaction = new Transaction(null, null, null, null, contractAddress, null, data);
            try {
                DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
                String value = web3j.ethCall(transaction, defaultBlockParameter).sendAsync().get().getValue();
                if (StringUtils.isNotEmpty(value)) {
                    BigInteger balanceWei = Numeric.decodeQuantity(value);
                    balance = new BigDecimal(balanceWei);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("获取账户余额异常:{}", e.getMessage());
        }
        return balance;
    }


    /**
     * 获取用户交易记录数
     */
    public BigInteger getTransactionCount(String address) throws RpcServiceException {

        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            if (null == ethGetTransactionCount) {
                throw new RpcServiceException("失获取用户交易记录数");
            }
            return ethGetTransactionCount.getTransactionCount();
        } catch (Exception e) {
            log.error("获取用户交易记录数异常:{}", e.getMessage());
            throw new RpcServiceException("获取用户交易记录数异常");
        }
    }

    /**
     * 获取交易信息
     */
    public EthTransaction getTransaction(String transactionHash) throws RpcServiceException {

        try {
            return web3j.ethGetTransactionByHash(transactionHash).sendAsync().get();
        } catch (Exception e) {
            log.error("获取交易信息异常:{}", e.getMessage());
            throw new RpcServiceException("获取交易信息异常");
        }
    }

    public TransactionReceipt getTransactionReceipt(String transactionHash) throws RpcServiceException {

        try {
            return web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get().getTransactionReceipt().get();
        } catch (Exception e) {
            log.error("获取交易信息异常:{}", e.getMessage());
            throw new RpcServiceException("获取交易信息异常");
        }
    }

    public BigInteger getBlockHigh() {
        BigInteger blockNumber = BigInteger.ZERO;
        try {
            blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockNumber;
    }


    public  List<EthBlock.TransactionResult> getTransactionsByBlockNum(BigInteger number) {
        List<EthBlock.TransactionResult> transactions = new ArrayList<EthBlock.TransactionResult>();
        try {
            DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(number);
            EthBlock.Block block = web3j.ethGetBlockByNumber(blockNumber, true).send().getBlock();
            if (block != null) {
                transactions = block.getTransactions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }


    public String getKeyStoreFileName(String eth_Address) {
        String eth_filename = "";
        String[] list = new File(KEYSTORE_FOLDER).list();
        for (String filename : list) {
            if (filename.indexOf(eth_Address) != -1) {
                eth_filename = filename;
                break;
            }
        }
        return eth_filename;
    }


    public EthSendTransaction ethSendTransaction(String fromAddress, String fromPassword, String toAddress, BigDecimal coinNum) throws RpcServiceException {
        EthSendTransaction ethSendTransaction = new EthSendTransaction();

        try {
            String jsonfile = getKeyStoreFileName(fromAddress.substring(2));
            if (StringUtils.isNotEmpty(jsonfile)) {
                Credentials credentials = WalletUtils.loadCredentials(fromPassword, new File(KEYSTORE_FOLDER+"/"+jsonfile)); // 私钥
                EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
                BigInteger gasPrice = ethGasPrice.getGasPrice();
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                BigInteger weiValue = Convert.toWei(coinNum, Convert.Unit.ETHER).toBigInteger();
                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, GAS_LIMIT, toAddress, weiValue);
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);
                ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            }
        } catch (Exception e) {
            log.error("[{}]转账给[{}]{}个ETH异常:{}", fromAddress, toAddress, coinNum, e.toString());
            throw new RpcServiceException("转账异常");
        }
        return ethSendTransaction;
    }

    public String sendTokenTransaction(String fromAddress, String fromPassword, String toAddress, String contractAddress, BigInteger coinNum) throws RpcServiceException {


        String hash = "";
        try {

            String jsonfile = getKeyStoreFileName(fromAddress.substring(2));
            if (StringUtils.isNotEmpty(jsonfile)) {
                Credentials credentials = WalletUtils.loadCredentials(fromPassword, new File(KEYSTORE_FOLDER+"/"+jsonfile)); // 私钥
                Address address = new Address(toAddress);
                List<Type> parametersList = new ArrayList<>();
                parametersList.add(address);

                Uint256 value256 = new Uint256(coinNum);
                parametersList.add(value256);
                List<TypeReference<?>> outList = new ArrayList<>();

                Function function = new Function("transfer", parametersList, outList);
                String encodedFunction = FunctionEncoder.encode(function);

                BigInteger gasPrice;
                EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
                gasPrice = ethGasPrice.getGasPrice();
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromPassword, DefaultBlockParameterName.LATEST).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, GAS_LIMIT_USDT, contractAddress, encodedFunction);
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

                String hexValue = Numeric.toHexString(signedMessage);

                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                hash = ethSendTransaction.getTransactionHash();
            }
        } catch (Exception e) {
            log.error("[{}]转账给[{}]{}个ETH异常:{}", fromAddress, toAddress, coinNum, e.toString());
            throw new RpcServiceException("转账异常");
        }
        return hash;
    }

    public static void main(String[] args) throws RpcServiceException {
        final EthereumService ethereumService = new EthereumService("https://mainnet.infura.io/v3/54a12236ad264a02b0560bfe9377da69");
        //0x0db28a16486d219228046d4c22a3fff7b7a9c5f6  0xdac17f958d2ee523a2206206994597c13d831ec7

        BigDecimal tokenBalance = ethereumService.getTokenBalance("0x0db28a16486d219228046d4c22a3fff7b7a9c5f6", "0xdac17f958d2ee523a2206206994597c13d831ec7");
        System.out.println(tokenBalance);
    }
}
