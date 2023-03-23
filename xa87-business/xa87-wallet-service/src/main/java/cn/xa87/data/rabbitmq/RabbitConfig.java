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

    @Bean
    public DirectExchange defaultExchange_put_wallet() {
        return new DirectExchange(RabbitConstants.WALLET_PUT);
    }

    @Bean
    public Queue queuePut_wallet() {
        return new Queue(RabbitConstants.WALLET_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_wallet() {
        return BindingBuilder.bind(queuePut_wallet()).to(defaultExchange_put_wallet()).with(RabbitConstants.WALLET_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_put_payment() {
        return new DirectExchange(RabbitConstants.PAYMENTS_PUT);
    }

    @Bean
    public Queue queuePut_payment() {
        return new Queue(RabbitConstants.PAYMENTS_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_payment() {
        return BindingBuilder.bind(queuePut_payment()).to(defaultExchange_put_payment()).with(RabbitConstants.PAYMENT_ROUTINGKEY_PUT);
    }
}