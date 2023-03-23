package cn.xa87.gateway.zuul.config;

import cn.xa87.common.redis.template.Xa87RedisRepository;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicRouteConfiguration {
    private Registration registration;
    private DiscoveryClient discovery;
    private ZuulProperties zuulProperties;
    private ServerProperties server;
    private Xa87RedisRepository redisRepository;

    public DynamicRouteConfiguration(Registration registration, DiscoveryClient discovery,
                                     ZuulProperties zuulProperties, ServerProperties server, Xa87RedisRepository redisRepository) {
        this.registration = registration;
        this.discovery = discovery;
        this.zuulProperties = zuulProperties;
        this.server = server;
        this.redisRepository = redisRepository;
    }

    @Bean
    public DiscoveryClientRouteLocator dynamicRouteLocator() {
        return new DynamicRouteLocator(
                server.getServlet().getPath()
                , discovery
                , zuulProperties
                , registration
                , redisRepository);
    }
}
