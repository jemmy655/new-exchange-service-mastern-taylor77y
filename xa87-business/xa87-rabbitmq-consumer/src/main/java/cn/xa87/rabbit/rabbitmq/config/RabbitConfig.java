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
    public DirectExchange defaultExchange_Register() {
        return new DirectExchange(RabbitConstants.REGISTER_PUT);
    }

    @Bean
    public Queue queuePut_Register() {
        return new Queue(RabbitConstants.REGISTER_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_Register() {
        return BindingBuilder.bind(queuePut_Register()).to(defaultExchange_Register()).with(RabbitConstants.REGISTER_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_Match() {
        return new DirectExchange(RabbitConstants.MATCH_PUT);
    }

    @Bean
    public Queue queuePut_Match() {
        return new Queue(RabbitConstants.MATCH_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_Match() {
        return BindingBuilder.bind(queuePut_Match()).to(defaultExchange_Match()).with(RabbitConstants.MATCH_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_Balance() {
        return new DirectExchange(RabbitConstants.BALANCE_PUT);
    }

    @Bean
    public Queue queuePut_Balance() {
        return new Queue(RabbitConstants.BALANCE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_BALANCE() {
        return BindingBuilder.bind(queuePut_Balance()).to(defaultExchange_Balance()).with(RabbitConstants.BALANCE_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_to_contract() {
        return new DirectExchange(RabbitConstants.MATCH_TO_CONTRACT_PUT);
    }

    @Bean
    public Queue queuePut_to_contract() {
        return new Queue(RabbitConstants.MATCH_TO_CONTRACT_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_to_contract() {
        return BindingBuilder.bind(queuePut_to_contract()).to(defaultExchange_to_contract()).with(RabbitConstants.MATCH_TO_CONTRACT_ROUTINGKEY_PUT);
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

    @Bean
    public DirectExchange defaultExchange_profit_put() {
        return new DirectExchange(RabbitConstants.PROFITLOSS_PRICE_PUT);
    }

    @Bean
    public Queue queueProfitPut() {
        return new Queue(RabbitConstants.PROFITLOSS_PRICE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingProfitPut() {
        return BindingBuilder.bind(queueProfitPut()).to(defaultExchange_profit_put()).with(RabbitConstants.PROFITLOSS_PRICE_ROUTINGKEY_PUT);
    }

    @Bean
    public DirectExchange defaultExchange_Contract_Price() {
        return new DirectExchange(RabbitConstants.CONTRACT_PRICE_PUT);
    }

    @Bean
    public Queue queuePut_Contract_Price() {
        return new Queue(RabbitConstants.CONTRACT_PRICE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_Contract_Price() {
        return BindingBuilder.bind(queuePut_Contract_Price()).to(defaultExchange_Contract_Price()).with(RabbitConstants.CONTRACT_PRICE_ROUTINGKEY_PUT);
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

    @Bean
    public DirectExchange defaultExchange_put_1() {
        return new DirectExchange(RabbitConstants.HUOBI_MANAGE_PUT);
    }

    @Bean
    public Queue queuePut_1() {
        return new Queue(RabbitConstants.HUOBI_MANAGE_QUEUE_PUT, true); //队列持久
    }

    @Bean
    public Binding bindingPut_1() {
        return BindingBuilder.bind(queuePut_1()).to(defaultExchange_put_1()).with(RabbitConstants.HUOBI_MANAGE_ROUTINGKEY_PUT);
    }

}