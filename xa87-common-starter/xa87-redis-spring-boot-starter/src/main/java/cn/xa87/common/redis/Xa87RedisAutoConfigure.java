package cn.xa87.common.redis;

import cn.xa87.common.redis.template.Xa87RedisRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis 配置类
 */
@Configuration
@ConditionalOnClass(Xa87RedisRepository.class)
@EnableConfigurationProperties(RedisProperties.class)
public class Xa87RedisAutoConfigure {

    /**
     * Redis repository redis repository.
     *
     * @param redisTemplate the redis template
     * @return the redis repository
     */
    @Bean
    @ConditionalOnMissingBean
    public Xa87RedisRepository redisRepository(RedisTemplate<String, String> redisTemplate) {
        return new Xa87RedisRepository(redisTemplate);
    }


    /**
     * redis，监听key过期触发事件
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(new RedisExpiredListener(), new PatternTopic("__keyevent@0__:expired"));
        return container;
    }
}
