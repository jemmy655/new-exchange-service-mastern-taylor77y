package cn.xa87.gateway.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class Xa87GatewayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xa87GatewayZuulApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(102400 * 10);
        factory.setMaxRequestSize(102400 * 10);
        return factory.createMultipartConfig();
    }
}
