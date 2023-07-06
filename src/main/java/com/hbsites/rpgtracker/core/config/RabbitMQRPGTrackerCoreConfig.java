package com.hbsites.rpgtracker.core.config;

import com.hbsites.hbsitescommons.queues.RabbitQueues;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JacksonConfig.class})
public class RabbitMQRPGTrackerCoreConfig {

    @Autowired
    Jackson2JsonMessageConverter converter;

    @Bean(name = "coreRequestQueue")
    Queue requestQueue() {
        return new Queue(RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_REQUEST);
    }

    @Bean(name = "coreResponseQueue")
    Queue responseQueue() {
        return new Queue(RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_RESPONSE);
    }

    @Bean(name = "coreTopicExchange")
    TopicExchange exchange() {
        return new TopicExchange(RabbitQueues.RPGTRACKER_CORE_EXCHANGE);
    }

    @Bean(name = "coreRequestBinding")
    Binding requestBinding() {
        return BindingBuilder.bind(requestQueue()).to(exchange()).with(RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_REQUEST);
    }

    @Bean(name = "coreResponseBinding")
    Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(exchange()).with(RabbitQueues.RPGTRACKER_CORE_CHARACTER_SHEET_RESPONSE);
    }

    @Bean(name = "coreRabbitTemplate")
    RabbitTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
