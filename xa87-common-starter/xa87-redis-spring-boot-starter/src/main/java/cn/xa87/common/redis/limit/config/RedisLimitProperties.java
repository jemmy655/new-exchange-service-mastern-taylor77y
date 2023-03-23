package cn.xa87.common.redis.limit.config;

import cn.xa87.common.redis.constant.RedisToolsConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xa87.redis.limit")
public class RedisLimitProperties {

    private Boolean enabled;
    /**
     * 具体限流value
     */
    private int value;
    /**
     * redis部署类型 1单机 2集群
     */
    private int type = RedisToolsConstant.SINGLE;
}
