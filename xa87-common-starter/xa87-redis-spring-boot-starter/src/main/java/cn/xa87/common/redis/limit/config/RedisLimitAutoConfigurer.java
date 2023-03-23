package cn.xa87.common.redis.limit.config;

import cn.xa87.common.redis.limit.RedisLimit;
import cn.xa87.common.redis.limit.intercept.SpringMvcIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(RedisLimitProperties.class)
@ConditionalOnProperty(name = "xa87.redis.limit.enabled", havingValue = "true")
public class RedisLimitAutoConfigurer implements WebMvcConfigurer {


    @Autowired
    private RedisLimitProperties limitProperties;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisLimit redisLimit() {
        return new RedisLimit.Builder(jedisConnectionFactory, limitProperties.getType())
                .limit(limitProperties.getValue())
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringMvcIntercept(redisLimit()));
    }
}
