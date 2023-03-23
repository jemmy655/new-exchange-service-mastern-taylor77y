package cn.xa87.rabbit.rabbitmq.config;

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

    @Bean
    public DirectExchange defaultExchange_main_entrust() {
        return new DirectExchange(RabbitConstants.ENTRUST_MAIN_MATCH_PUT);
    }

    @Bean
    public Queue queuePut_main_entrust() {
        return new Queue(RabbitConstants.ENTRUST_MAIN_MATCH_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_main_entrust() {
        return BindingBuilder.bind(queuePut_main_entrust()).to(defaultExchange_main_entrust()).with(RabbitConstants.ENTRUST_MAIN_MATCH_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_project_entrust() {
        return new DirectExchange(RabbitConstants.ENTRUST_PROJECT_MATCH_PUT);
    }

    @Bean
    public Queue queuePut_project_entrust() {
        return new Queue(RabbitConstants.ENTRUST_PROJECT_MATCH_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_project_entrust() {
        return BindingBuilder.bind(queuePut_project_entrust()).to(defaultExchange_project_entrust()).with(RabbitConstants.ENTRUST_PROJECT_MATCH_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_EntrustBalance() {
        return new DirectExchange(RabbitConstants.ENTRUST_BALANCE_PUT);
    }

    @Bean
    public Queue queuePut_EntrustBalance() {
        return new Queue(RabbitConstants.ENTRUST_BALANCE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_EntrustBalance() {
        return BindingBuilder.bind(queuePut_EntrustBalance()).to(defaultExchange_EntrustBalance()).with(RabbitConstants.ENTRUST_BALANCE_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_EntrustPrice() {
        return new DirectExchange(RabbitConstants.ENTRUST_PRICE_PUT);
    }

    @Bean
    public Queue queuePut_EntrustPrice() {
        return new Queue(RabbitConstants.ENTRUST_PRICE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_EntrustPrice() {
        return BindingBuilder.bind(queuePut_EntrustPrice()).to(defaultExchange_EntrustPrice()).with(RabbitConstants.ENTRUST_PRICE_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_EntrustMarketBalance() {
        return new DirectExchange(RabbitConstants.ENTRUST_MARKET_BALANCE_PUT);
    }

    @Bean
    public Queue queuePut_EntrustMarketBalance() {
        return new Queue(RabbitConstants.ENTRUST_MARKET_BALANCE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_EntrustMarketBalance() {
        return BindingBuilder.bind(queuePut_EntrustMarketBalance()).to(defaultExchange_EntrustMarketBalance()).with(RabbitConstants.ENTRUST_MARKET_BALANCE_ROUTINGKEY_PUT);
    }
}