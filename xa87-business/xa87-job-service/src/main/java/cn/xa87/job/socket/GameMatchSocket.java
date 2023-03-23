package cn.xa87.job.socket;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.constant.CoinConstant;
import cn.xa87.job.mapper.PairsMapper;
import cn.xa87.job.service.impl.KlineServiceImpl;
import cn.xa87.model.Pairs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

/**
 * 消息接收器
 */
@Slf4j
@Component
public class GameMatchSocket extends WebSocketClient implements ApplicationListener<ApplicationReadyEvent> {

    String[] timeArray = {"1min","5min","15min","30min","60min","1day","1week"};

    private AtomicInteger errorCount;
    @Autowired
    private Xa87RedisRepository redisRepository;
    @Autowired
    private PairsMapper pairsMapper;
    @Autowired
    public GameMatchSocket() {
        super(URI.create("wss://api.huobi.pro/ws"));
        errorCount = new AtomicInteger(0);
        setConnectionLostTimeout(30);

    }

    public GameMatchSocket(URI serverUri) {
        super(serverUri);
    }
    private void openK(String str){
        for (String s : timeArray) {
            send("{" + " \"sub\":\"market."+str+".kline."+s+"\"," + "  \"id\":\"id10\"" + "}");
        }
    }

    // 收到数据
    private void content(String str){
        JSONObject jsonObject = JSONObject.parseObject(str);
        String ch = (String) jsonObject.get("ch");
        if(!StringUtils.isEmpty(ch)){
            String[] split = ch.split("\\.");

            String period="";
            switch (split[3]) {
                case "1min":
                    period="1m";
                    break;
                case "5min":
                    period="5m";
                    break;
                case "15min":
                    period="15m";
                    break;
                case "30min":
                    period="30m";
                    break;
                case "60min":
                    period="1h";
                    break;
                case "1day":
                    period="1d";
                    break;
                case "1week":
                    period="1w";
                    break;
                default:
                    break;
            }

            String pairsName = split[1];
            int usdt = pairsName.lastIndexOf("usdt");
            String a = pairsName.substring(0,usdt)+"/"+"usdt";
            String redisKey = CacheConstants.KLINE_KEY + period + CacheConstants.SPLIT + a.toUpperCase();
            Object tick = jsonObject.get("tick");
            JSONObject jsonInfo =JSONObject.parseObject(tick.toString());
            jsonInfo.put("volume", jsonInfo.get("amount"));
            long time=Long.parseLong(jsonInfo.get("id").toString().concat("000"));
            jsonInfo.put("time", time);
            redisRepository.zsetRmByScore(redisKey, time);
            redisRepository.zsetAdd(redisKey, jsonInfo.toJSONString(), time);
        }

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("打开了链接...");
        errorCount.set(0);

        QueryWrapper<Pairs> wrapper = new QueryWrapper<Pairs>();
        wrapper.eq("state", CoinConstant.Coin_State.NORMAL);
        wrapper.ne("token_cur","USDT");
        wrapper.ne("pairs_type",1);
        List<Pairs> list = pairsMapper.selectList(wrapper);
        for (Pairs pairs : list) {
            String pairsName = pairs.getPairsName().replace("/", "").toLowerCase();
            openK(pairsName);
        }

        sendPing();
    }

    @Override
    public void onMessage(String s) {
        log.info("收到消息 {} ", s);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        String gunzip = gunzip(bytes);

        // 心跳回复
        if(gunzip.contains("ping")){
            String replaceAll = gunzip.replaceAll("ping", "pong");
            send(replaceAll);
            return;
        }
        content(gunzip);
    }


    // 数据解压
    public static String gunzip(ByteBuffer bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        //byte[] compressed = null;
        String decompressed = null;
        try {
            //compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(bytes.array());
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    @PreDestroy
    public void disconnect() {
        super.close();
    }

    @Override
    public void onClose(int code, String s, boolean b) {
        log.warn("链接关闭了 url=>{} code=>{} err=>{} b=>{}", getURI(), code, s, b);
//        errorCount.addAndGet(1);
        //重试5次
//        if (errorCount.get() <= 5 && code!=1002) {
//            reconnect();
//        }

    }

    @Override
    public void onError(Exception e) {
        log.error("socker 发生错误了 ", e);
    }


    public static void main(String[] args) {
        String pairsName="market.filusdt.kline.1week";
//        int usdt = pairsName.lastIndexOf("usdt");
//        String a = pairsName.substring(0,usdt)+"/"+"usdt";
        String[] split = pairsName.split("\\.");
        System.out.println(split[1]);
//        GameMatchSocket gameMatchSocket = new GameMatchSocket();
//        gameMatchSocket.connect();




    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //log.info("socket 开始启动了");
        // this.connect();
    }
}
