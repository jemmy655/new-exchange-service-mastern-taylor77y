package cn.xa87.common.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign统一配置
 */
@Configuration
public class Xa87FeignAutoConfigure {

    /**
     * Feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    public RequestAttributeHystrixConcurrencyStrategy hystrixRequestAutoConfiguration() {
        return new RequestAttributeHystrixConcurrencyStrategy();
    }
}
