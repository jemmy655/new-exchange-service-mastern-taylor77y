package cn.xa87.socket.server;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class ServiceInfoUtil implements ApplicationListener<WebServerInitializedEvent> {
    private static WebServerInitializedEvent event;

    public static int getPort() {
        Assert.notNull(event);
        int port = event.getWebServer().getPort();
        Assert.state(port != -1, "端口号获取失败");
        return port;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        ServiceInfoUtil.event = event;
    }
}
