package cn.xa87.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Xa87JobServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Xa87JobServiceApplication.class, args);
    }


}
