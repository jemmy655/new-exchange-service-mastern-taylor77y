package cn.xa87.data.walletBusiness.service;

import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.data.tool.HttpReques;
import cn.xa87.data.tool.MD5Util;
import cn.xa87.data.walletBusiness.exception.RpcServiceException;
import cn.xa87.model.WalletPool;
import com.alibaba.fastjson.JSONObject;
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
import java.util.*;


public class ZtpayService {

    private final Logger log = LoggerFactory.getLogger(ZtpayService.class);



    public  String Appid = "";
    public  String AppSecret = "";
    public  String Url = "https://sapi.ztpay.org/api/v2";


    public ZtpayService(String ethUrl,String AppSecret,String Appid) {

        this.Url= ethUrl;
        this.AppSecret= AppSecret;
        this.Appid= Appid;

    }


    //数组签名
    public  String generateSignature(final Map<String, String> data, String key){
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if(k=="sign" || data.get(k).trim()=="0"){
                continue;
            }
            if(data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5Util.encrypt(sb.toString()).toUpperCase();
        //return sb.toString();
    }
    //创建地址
    public  String get_address(String name)  {
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "get_address");
        mapParams.put("name", name);
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data="";
        try {
            data=HttpReques.sendPost(Url,mapParams);
        } catch (IOException e) {
            return get_address( name);
        }

        apidata obj = JSONObject.parseObject(data,apidata.class);
        if(null != obj && obj.code==0){
            data=obj.data.address;
        }

        return data;
    }
    //查询余额
    public  String get_balance(String name,String address) throws IOException {
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "get_balance");
        mapParams.put("name", name);
        mapParams.put("address", address);
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;
    }
    //推荐
    //动态获取eth和usdt_erc20的矿工费
    //提高转账效率快速到账

    public  String get_eth_gasprice()  throws IOException
    {
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "get_eth_gasprice");
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);
        return data;

    }



    //转账
    public  String transfer(String name,String from,String to,String amount)  throws IOException{

        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "transfer");
        mapParams.put("name", name);
        mapParams.put("from", from);
        mapParams.put("to", to);
        mapParams.put("amount", amount);
        //mapParams.put("gas", 70000);//eth建议30000和erc20建议70000设置矿工费专用
        //mapParams.put("gasprice", 40);//eth和erc20设置矿工费专用 可以用get_eth_gasprice方法动态获取
        //mapParams.put("change_address", change_address);//btc币种打开此参数，找零地址
        //mapParams.put("fee_address", fee_address);//usdt_omni币种打开此参数,转账时可以指定手续费地址
        //mapParams.put("fee_amount", fee_amount);//指定矿工费数量.此字段仅支持：BTC ，USDT_omni，VDS，XRP，BCH，LTC
        //mapParams.put("memo", memo);//EOS和XRP币种打开此参数专用 转账备注标签
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;

    }
    //获取行情
    public  String market() throws IOException{
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "market");
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;
    }
    //效验地址
    public  String check_address(String name,String address) throws IOException{
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "check_address");
        mapParams.put("name", name);
        mapParams.put("address", address);
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;
    }
    //交易日志
    public  String transaction_logs(String name,String from,String to) throws IOException{

        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "transaction_logs");
        mapParams.put("name", name);
        mapParams.put("from", from);
        mapParams.put("to", to);
        //mapParams.put("transaction_page", "");//交易日志获取的页码，默认为1
        //mapParams.put("transaction_pagesize", "");//设置获取交易日志每页请求的数量，最大100条
        //mapParams.put("transaction_type", "");//0-全部 1-转入 2-转出
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;

    }
    //交易详情
    public  String transaction_detail(String name,String hash,String tr_native) throws IOException{

        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "transaction_detail");
        mapParams.put("name", name);
        mapParams.put("hash", hash);//交易hash或者txid值
        mapParams.put("tr_native", tr_native);//0-原生 1-平台已过滤

        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;

    }
    //获取eos余额
    public  String eos_get_balance(String name,String eos_account_name) throws IOException{
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "eos_get_balance");
        mapParams.put("name", name);
        mapParams.put("eos_account_name", eos_account_name);
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;
    }
    //获取eos账户信息
    public  String eos_get_account(String name,String eos_account_name) throws IOException{
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", Appid);
        mapParams.put("method", "eos_get_account");
        mapParams.put("name", name);
        mapParams.put("eos_account_name", eos_account_name);
        String sign=generateSignature(mapParams,AppSecret);
        mapParams.put("sign", sign);
        String data= HttpReques.sendPost(Url,mapParams);

        return data;
    }
    public static void main(String[] args) throws RpcServiceException {
        /*ZtpayService ethereumService = new ZtpayService("https://sapi.ztpay.org/api/v2","lQzxwfHBraFumshtQfrN7D6E34sxklpz","ztpay91xrbde6wsucn");

        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("appid", "ztpay91xrbde6wsucn");
        mapParams.put("hash", "ztpay91xrbde6wsucn");
        mapParams.put("from", "from");
        mapParams.put("to", "0x7dda533ae01392dd7d8b88bb8ab0b952f9542493");
        mapParams.put("amount", "3.0");
        mapParams.put("fee_amount", "1.0");
        mapParams.put("memo", "");
        mapParams.put("add_time", "");
        mapParams.put("type", "1");
        mapParams.put("state", "1");
        System.out.println(ethereumService.generateSignature(mapParams,"key"));
        */ZtpayService ethereumService = new ZtpayService("https://sapi.ztpay.org/api/v2","D8sIoRp1vAExoCaDuLljRcgluoRE2xmM","ztpayookbrsbh7baer");
        String address= null;

            address = ethereumService.get_address("USDT");
            apidata obj = JSONObject.parseObject(address,apidata.class);
            System.out.println(address);
            if(null != obj && obj.code==0){
                address=obj.data.address;
            }
            System.out.println(address);


    }
}
