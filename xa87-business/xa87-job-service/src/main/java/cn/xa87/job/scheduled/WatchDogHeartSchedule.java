package cn.xa87.job.scheduled;

import cn.xa87.job.socket.GameMatchSocket;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * socket 心跳狗
 */
@Component
@Slf4j
public class WatchDogHeartSchedule {

    @Resource
    private GameMatchSocket matchSocket;

    @Async
    @Scheduled(cron = "0/5 * * * * ?")
    public void task() {
        log.info("我是看门狗，在检测心跳了.");
        try {
            watch(matchSocket, "K线数据");
        } catch (Exception e) {
            log.error("看门狗出异常了。", e);
        }

    }

    private void watch(WebSocketClient client, String tag) {
        if (!client.isOpen()) {
            if (client.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                try {
                    log.info("还没有链接，进行链接");
                    client.connect();
                } catch (IllegalStateException e) {
                    log.error("socket链接出错了", e);
                }
            } else if (client.getReadyState().equals(ReadyState.CLOSING) || client.getReadyState().equals(ReadyState.CLOSED)) {
                log.warn("被关闭了，发起重连");
                client.reconnect();
            }
        } else {
            log.info("{} 发送心跳包...", tag);
//            client.sendPing();
            client.send("ping");
        }
    }
}
