package cn.xa87.socket.server;

import cn.xa87.socket.data.DataService;
import cn.xa87.socket.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Component
@EnableScheduling
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    public static final ConcurrentHashMap<Channel, String> channel_map_kline = new ConcurrentHashMap<Channel, String>();
    public static final ConcurrentHashMap<Channel, Long> channel_time = new ConcurrentHashMap<Channel, Long>();
    public static final ConcurrentHashMap<String, ChannelGroup> kline_group = new ConcurrentHashMap<String, ChannelGroup>();
    public static final ConcurrentHashMap<Channel, String> channel_map_match = new ConcurrentHashMap<Channel, String>();
    public static final ConcurrentHashMap<String, ChannelGroup> match_group = new ConcurrentHashMap<String, ChannelGroup>();
    public static final ConcurrentHashMap<Channel, String> channel_map_entrust = new ConcurrentHashMap<Channel, String>();
    public static final ConcurrentHashMap<String, ChannelGroup> entrust_group = new ConcurrentHashMap<String, ChannelGroup>();

    public static final ConcurrentHashMap<String, Channel> chat_group = new ConcurrentHashMap<String, Channel>();
    public static final List<String> chat_group_admin = new ArrayList<>();
    private WebSocketServerHandshaker handshaker;
    private DataService dataService;

    public WebSocketServerHandler(DataService dataService) {
        this.dataService = dataService;
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = String.valueOf(req.headers().get(HttpHeaderNames.HOST)) + "/ws/websocket";
        return "ws://" + location;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        if (req.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));

            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
                null, true, 5242880);
        this.handshaker = wsFactory.newHandshaker(req);
        if (this.handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            this.handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            String mapKey = channel_map_kline.get(ctx.channel());
            if (kline_group.containsKey(mapKey)) {
                kline_group.get(mapKey).remove(ctx.channel());
                if (kline_group.get(mapKey).isEmpty()) {
                    kline_group.remove(mapKey);
                }
                channel_map_kline.remove(ctx.channel());
            }
            String mapKeyMatch = channel_map_match.get(ctx.channel());
            if (mapKeyMatch != null) {
                if (match_group.containsKey(mapKeyMatch)) {
                    match_group.get(mapKeyMatch).remove(ctx.channel());
                    if (match_group.get(mapKeyMatch).isEmpty()) {
                        match_group.remove(mapKeyMatch);
                    }
                    channel_map_match.remove(ctx.channel());
                }
            }
            String mapKeyEntrust = channel_map_entrust.get(ctx.channel());
            if (mapKeyEntrust != null) {
                if (entrust_group.containsKey(mapKeyEntrust)) {
                    entrust_group.get(mapKeyEntrust).remove(ctx.channel());
                    if (entrust_group.get(mapKeyEntrust).isEmpty()) {
                        entrust_group.remove(mapKeyEntrust);
                    }
                    channel_map_entrust.remove(ctx.channel());
                }
            }
            channel_time.remove(ctx.channel());
            return;
        }
        if (frame instanceof io.netty.handler.codec.http.websocketx.PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame twsf = (TextWebSocketFrame) frame;
            if (twsf.text().contains("initEntrust")) {
                String mapKeyMatch = channel_map_match.get(ctx.channel());
                if (mapKeyMatch != null) {
                    if (match_group.containsKey(mapKeyMatch)) {
                        match_group.get(mapKeyMatch).remove(ctx.channel());
                        if (match_group.get(mapKeyMatch).isEmpty()) {
                            match_group.remove(mapKeyMatch);
                        }
                        channel_map_match.remove(ctx.channel());
                    }
                }

                //盘口
                String pairsName = twsf.text().split("-")[1];
                Object obj = dataService.map_entrust.get(pairsName);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                //heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString("entrust*" + obj)).getBytes());
                heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString(obj)).getBytes());
                ctx.channel().writeAndFlush(new BinaryWebSocketFrame(heapBuffer));

                ChannelGroup channelGroup = (ChannelGroup) entrust_group.get(pairsName);
                if (channelGroup == null) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
                channelGroup.add(ctx.channel());
                entrust_group.put(pairsName, channelGroup);
                //记录每一个客户端所请求的信息--如果存在则删除之前的组员信息
                if (channel_map_entrust.containsKey(ctx.channel())) {
                    String mapKey = channel_map_entrust.get(ctx.channel());
                    if (!mapKey.equals(pairsName)) {
                        channel_map_entrust.put(ctx.channel(), pairsName);
                        entrust_group.get(mapKey).remove(ctx.channel());
                        if (entrust_group.get(mapKey).isEmpty()) {
                            entrust_group.remove(mapKey);
                        }
                    }
                } else {
                    channel_map_entrust.put(ctx.channel(), pairsName);
                }
                channel_time.put(ctx.channel(), Long.valueOf(System.currentTimeMillis()));
            }
            if (twsf.text().contains("initMatch")) {
                String mapKeyEntrust = channel_map_entrust.get(ctx.channel());
                if (mapKeyEntrust != null) {
                    if (entrust_group.containsKey(mapKeyEntrust)) {
                        entrust_group.get(mapKeyEntrust).remove(ctx.channel());
                        if (entrust_group.get(mapKeyEntrust).isEmpty()) {
                            entrust_group.remove(mapKeyEntrust);
                        }
                        channel_map_entrust.remove(ctx.channel());
                    }
                }

                //盘口
                String pairsName = twsf.text().split("-")[1];
                Object obj = dataService.map_match.get(pairsName);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString("match*" + obj)).getBytes());
                //heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString(obj)).getBytes());
                ctx.channel().writeAndFlush(new BinaryWebSocketFrame(heapBuffer));

                ChannelGroup channelGroup = (ChannelGroup) match_group.get(pairsName);
                if (channelGroup == null) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
                channelGroup.add(ctx.channel());
                match_group.put(pairsName, channelGroup);
                //记录每一个客户端所请求的信息--如果存在则删除之前的组员信息
                if (channel_map_match.containsKey(ctx.channel())) {
                    String mapKey = channel_map_match.get(ctx.channel());
                    if (!mapKey.equals(pairsName)) {
                        channel_map_match.put(ctx.channel(), pairsName);
                        match_group.get(mapKey).remove(ctx.channel());
                        if (match_group.get(mapKey).isEmpty()) {
                            match_group.remove(mapKey);
                        }
                    }
                } else {
                    channel_map_match.put(ctx.channel(), pairsName);
                }
                channel_time.put(ctx.channel(), Long.valueOf(System.currentTimeMillis()));
            }
            //K线
            if (twsf.text().contains("initKline")) {
                String[] params = twsf.text().split("-");
                //String method=params[0];
                String pairsName = params[1];
                String timeType = params[2];
                Long start = Long.parseLong(params[3]);
                Long end = Long.parseLong(params[4]);
                String methodKey = pairsName + "-" + timeType;
                Set<String> set = dataService.getKlineByTime(pairsName, timeType, start, end);
                JSONObject jo = new JSONObject();
                jo.put("data", set);
                jo.put("method", "initKline");
                jo.put("pairs", pairsName);
                jo.put("timeType", timeType);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                heapBuffer.writeBytes(ZipUtil.gzip(jo.toJSONString()).getBytes());
                ctx.channel().writeAndFlush(new BinaryWebSocketFrame(heapBuffer));
//				TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(set));
//				ctx.channel().writeAndFlush(textWebSocketFrame);

                ChannelGroup channelGroup = (ChannelGroup) kline_group.get(methodKey);
                if (channelGroup == null) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
                channelGroup.add(ctx.channel());
                kline_group.put(methodKey, channelGroup);
                //记录每一个客户端所请求的信息--如果存在则删除之前的组员信息
                if (channel_map_kline.containsKey(ctx.channel())) {
                    String mapKey = channel_map_kline.get(ctx.channel());
                    if (!mapKey.equals(methodKey)) {
                        channel_map_kline.put(ctx.channel(), methodKey);
                        kline_group.get(mapKey).remove(ctx.channel());
                        if (kline_group.get(mapKey).isEmpty()) {
                            kline_group.remove(mapKey);
                        }
                    }
                } else {
                    channel_map_kline.put(ctx.channel(), methodKey);
                }
                channel_time.put(ctx.channel(), Long.valueOf(System.currentTimeMillis()));
            } else if (twsf.text().equals("PING")) {
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("PONG");
                ctx.channel().writeAndFlush(textWebSocketFrame);
                channel_time.put(ctx.channel(), Long.valueOf(System.currentTimeMillis()));
            }
            String text = twsf.text();
            if (text.startsWith("chatReg->")) {
                //[1]:id , [2]:isAdmin
                String[] split = text.split("->", 2);
                if (split.length == 3) {
                    if (split[2] == "isAdmin") {
                        chat_group_admin.add(split[1]);
                    }
                    chat_group.put(split[1], ctx.channel());
                }
                return;
            }
            if (text.startsWith("chat->")) {
                //[1]:from , [2]:to , [3]:message
                String[] split = text.split("->", 3);
                if(split.length == 4){
                    Channel fromChannel = chat_group.get(split[2]);
                    if (fromChannel != null) {
                        fromChannel.writeAndFlush(new TextWebSocketFrame(split[2]));
                        return;
                    }
                }
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Scheduled(fixedRate = 1000L)
    public void sendAllMessageKline() {
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (String methodKey : kline_group.keySet()) {
            es.submit(new sendKline(methodKey));
        }
        es.shutdown();
    }

    @Scheduled(fixedRate = 1000L)
    public void sendAllMessageMatch() {
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (String methodKey : match_group.keySet()) {
            es.submit(new sendMatch(methodKey));
        }
        es.shutdown();
    }

    @Scheduled(fixedRate = 1000L)
    public void sendAllMessageEntrust() {
        ThreadPoolExecutor es = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (String methodKey : entrust_group.keySet()) {
            es.submit(new sendEntrust(methodKey));
        }
        es.shutdown();
    }

    @Scheduled(fixedRate = 60000L)
    public void remKlineChannel() {
        for (Channel channel : channel_time.keySet()) {
            try {
                Long time = channel_time.get(channel).longValue() + 60000L;
                if (time < System.currentTimeMillis()) {
                    String mapKey = channel_map_kline.get(channel);
                    if (kline_group.containsKey(mapKey)) {
                        kline_group.get(mapKey).remove(channel);
                        if (kline_group.get(mapKey).isEmpty()) {
                            kline_group.remove(mapKey);
                        }
                        channel_map_kline.remove(channel);
                    }
                    String mapKeyMatch = channel_map_match.get(channel);
                    if (match_group.containsKey(mapKey)) {
                        match_group.get(mapKeyMatch).remove(channel);
                        if (match_group.get(mapKeyMatch).isEmpty()) {
                            match_group.remove(mapKeyMatch);
                        }
                        channel_map_match.remove(channel);
                    }
                    String mapKeyEntrust = channel_map_entrust.get(channel);
                    if (entrust_group.containsKey(mapKeyEntrust)) {
                        entrust_group.get(mapKeyEntrust).remove(channel);
                        if (entrust_group.get(mapKeyEntrust).isEmpty()) {
                            entrust_group.remove(mapKeyEntrust);
                        }
                        channel_map_entrust.remove(channel);
                    }
                    channel_time.remove(channel);
                    channel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class sendEntrust implements Runnable {
        private String methodKey;

        sendEntrust(String methodKey) {
            this.methodKey = methodKey;
        }

        @Override
        public void run() {
            try {
                Object obj = dataService.map_entrust.get(methodKey);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString(obj)).getBytes());
                entrust_group.get(methodKey).writeAndFlush(new BinaryWebSocketFrame(heapBuffer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class sendMatch implements Runnable {
        private String methodKey;

        sendMatch(String methodKey) {
            this.methodKey = methodKey;
        }

        @Override
        public void run() {
            try {
                Object obj = dataService.map_match.get(methodKey);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                heapBuffer.writeBytes(ZipUtil.gzip(JSONObject.toJSONString(obj)).getBytes());
                match_group.get(methodKey).writeAndFlush(new BinaryWebSocketFrame(heapBuffer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class sendKline implements Runnable {
        private String methodKey;

        sendKline(String methodKey) {
            this.methodKey = methodKey;
        }

        @Override
        public void run() {
            try {
                //String methodKey=pairsName+"-"+timeType;
                String[] methodKeys = methodKey.split("-");
                JSONObject jo = new JSONObject();
                jo.put("data", dataService.getInintKline(methodKey));
                jo.put("method", "oneKline");
                jo.put("pairs", methodKeys[0]);
                jo.put("timeType", methodKeys[1]);
                ByteBuf heapBuffer = Unpooled.buffer(8);
                heapBuffer.writeBytes(ZipUtil.gzip(jo.toJSONString()).getBytes());
                kline_group.get(methodKey).writeAndFlush(new BinaryWebSocketFrame(heapBuffer));
                //ctx.channel().writeAndFlush(new BinaryWebSocketFrame(heapBuffer));
                //TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(dataService.getInintKline(methodKey)));
                //kline_group.get(methodKey).writeAndFlush(textWebSocketFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

