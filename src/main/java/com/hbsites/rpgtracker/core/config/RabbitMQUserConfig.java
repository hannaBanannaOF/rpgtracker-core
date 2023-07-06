package com.hbsites.rpgtracker.core.config;

import com.hbsites.hbsitescommons.queues.RabbitQueues;
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
public class RabbitMQUserConfig {

    @Autowired
    Jackson2JsonMessageConverter converter;

    @Bean(name = "userRequestQueue")
    Queue requestQueue() {
        return new Queue(RabbitQueues.USER_REQUEST_QUEUE);
    }

    @Bean(name = "userResponseQueue")
    Queue responseQueue() {
        return new Queue(RabbitQueues.USER_RESPONSE_QUEUE);
    }

    @Bean(name = "userTopicExchange")
    TopicExchange exchange() {
        return new TopicExchange(RabbitQueues.USER_EXCHANGE);
    }

    @Bean(name = "userRequestBinding")
    Binding requestBinding() {
        return BindingBuilder.bind(requestQueue()).to(exchange()).with(RabbitQueues.USER_REQUEST_QUEUE);
    }

    @Bean(name = "userResponseBinding")
    Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(exchange()).with(RabbitQueues.USER_RESPONSE_QUEUE);
    }

    @Bean(name = "userRabbitTemplate")
    RabbitTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyAddress(RabbitQueues.USER_RESPONSE_QUEUE);
        template.setReplyTimeout(6000);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean(name = "userReplyContainer")
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitQueues.USER_RESPONSE_QUEUE);
        container.setMessageListener(template(connectionFactory));
        return container;
    }
}
