package cn.xa87.data.rabbitmq;

import cn.xa87.common.constants.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     * 挂单队列初始化
     */
    @Bean
    public DirectExchange defaultExchange_put() {
        return new DirectExchange(RabbitConstants.MATCH_PUT);
    }

    @Bean
    public Queue queuePut() {
        return new Queue(RabbitConstants.MATCH_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut() {
        return BindingBuilder.bind(queuePut()).to(defaultExchange_put()).with(RabbitConstants.MATCH_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_ConsumeCurrency() {
        return new DirectExchange(RabbitConstants.CONSUME_CURRENCY_PUT);
    }

    @Bean
    public Queue queuePut_ConsumeCurrency() {
        return new Queue(RabbitConstants.CONSUME_CURRENCY_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_ConsumeCurrency() {
        return BindingBuilder.bind(queuePut_ConsumeCurrency()).to(defaultExchange_ConsumeCurrency()).with(RabbitConstants.CONSUME_CURRENCY_ROUTINGKEY_PUT);
    }

    /**
     * 经纪人管理后台 (php高露要用)
     */
    @Bean
    public DirectExchange defaultExchange_BrokerManage() {
        return new DirectExchange(RabbitConstants.BROKER_MANAGE_PUT);
    }

    @Bean
    public Queue queuePut_BrokerManage() {
        return new Queue(RabbitConstants.BROKER_MANAGE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_BrokerManage() {
        return BindingBuilder.bind(queuePut_BrokerManage()).to(defaultExchange_BrokerManage()).with(RabbitConstants.BROKER_MANAGE_ROUTINGKEY_PUT);
    }
}