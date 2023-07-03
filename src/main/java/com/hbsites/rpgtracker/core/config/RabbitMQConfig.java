package com.hbsites.rpgtracker.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_REQUEST_QUEUE = "user.request";
    public static final String USER_RESPONSE_QUEUE = "user.response";
    public static final String USER_EXCHANGE = "user-exchange";

    @Bean
    Queue msgQueue() {

        return new Queue(USER_REQUEST_QUEUE);
    }

    @Bean
    Queue replyQueue() {

        return new Queue(USER_RESPONSE_QUEUE);
    }

    @Bean
    TopicExchange exchange() {

        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    Binding msgBinding() {

        return BindingBuilder.bind(msgQueue()).to(exchange()).with(USER_REQUEST_QUEUE);
    }

    @Bean
    Binding replyBinding() {

        return BindingBuilder.bind(replyQueue()).to(exchange()).with(USER_RESPONSE_QUEUE);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyAddress(USER_RESPONSE_QUEUE);
        template.setReplyTimeout(6000);
        return template;
    }

    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(USER_RESPONSE_QUEUE);
        container.setMessageListener(rabbitTemplate(connectionFactory));
        return container;
    }
}
