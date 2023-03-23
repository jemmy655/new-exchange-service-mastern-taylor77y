package cn.xa87.socket.server;

import cn.xa87.socket.data.DataService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private DataService dataService;

    public WebSocketServerInitializer(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerHandler(dataService));
        pipeline.addLast("ping", new IdleStateHandler(720, 720, 720,
                TimeUnit.MINUTES));
        //对空闲检测进一步处理的Handler
        pipeline.addLast(new MyServerHandler());
//		// 以("\n")为结尾分割的 解码器
//		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
//				Delimiters.lineDelimiter()));
//		// 字符串解码 和 编码
//		pipeline.addLast("decoder", new StringDecoder());
//		pipeline.addLast("encoder", new StringEncoder());
    }


}
