package com.hbsites.rpgtracker.core.config;

import com.hbsites.hbsitescommons.queues.RabbitQueues;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class RabbitMQConfig {

    // Exchanges

    @Bean(name = "userExchange")
    DirectExchange exchange() {
        return new DirectExchange(RabbitQueues.USER_EXCHANGE);
    }
    @Bean(name = "coreExchange")
    DirectExchange coreExchange() {
        return new DirectExchange(RabbitQueues.RPGTRACKER_CORE_EXCHANGE);
    }
    @Bean(name = "cocExchange")
    DirectExchange cocExchange () {
        return new DirectExchange(RabbitQueues.RPGTRACKER_COC_EXCHANGE);
    }

    // User
    @Bean(name = "userRequestQueue")
    Queue userRequestQueue() {
        return new Queue(RabbitQueues.USER_REQUEST_QUEUE);
    }
    @Bean(name = "coreUserResponseQueue")
    Queue coreUserResponseQueue() {
        return new Queue(RabbitQueues.CORE_USER_RESPONSE_QUEUE);
    }
    @Bean(name = "userRequestBinding")
    Binding userRequestBinding() {
        return BindingBuilder.bind(userRequestQueue()).to(exchange()).with(RabbitQueues.USER_REQUEST_QUEUE);
    }
    @Bean(name = "coreUserResponseBinding")
    Binding coreUserResponseBinding() {
        return BindingBuilder.bind(coreUserResponseQueue()).to(coreExchange()).with(RabbitQueues.CORE_USER_RESPONSE_QUEUE);
    }

    // Character - Sheet

    @Bean(name = "characterSheetRequestQueue")
    Queue characterSheetRequestQueue() {
        return new Queue(RabbitQueues.CORE_CHARACTER_SHEET_REQUEST_QUEUE);
    }
    @Bean(name = "coCCharacterSheetResponseQueue")
    Queue coCCharacterSheetResponseQueue() {
        return new Queue(RabbitQueues.COC_CHARACTER_SHEET_RESPONSE_QUEUE);
    }
    @Bean(name = "characterSheetRequestBinding")
    Binding characterSheetRequestBinding() {
        return BindingBuilder.bind(characterSheetRequestQueue()).to(coreExchange()).with(RabbitQueues.CORE_CHARACTER_SHEET_REQUEST_QUEUE);
    }
    @Bean(name = "coCCharacterSheetResponseBinding")
    Binding coCCharacterSheetResponseBinding() {
        return BindingBuilder.bind(coCCharacterSheetResponseQueue()).to(cocExchange()).with(RabbitQueues.COC_CHARACTER_SHEET_RESPONSE_QUEUE);
    }

    // Session
    @Bean(name = "sessionRequestQueue")
    Queue sessionRequestQueue() {
        return new Queue(RabbitQueues.CORE_SESSION_REQUEST_QUEUE);
    }
    @Bean(name = "coCSessionResponseQueue")
    Queue coCSessionResponseQueue() {
        return new Queue(RabbitQueues.COC_SESSION_RESPONSE_QUEUE);
    }
    @Bean(name = "sessionRequestBinding")
    Binding sessionRequestBinding() {
        return BindingBuilder.bind(sessionRequestQueue()).to(coreExchange()).with(RabbitQueues.CORE_SESSION_REQUEST_QUEUE);
    }
    @Bean(name = "coCSessionResponseBinding")
    Binding coCSessionResponseBinding() {
        return BindingBuilder.bind(coCSessionResponseQueue()).to(cocExchange()).with(RabbitQueues.COC_SESSION_RESPONSE_QUEUE);
    }

    // Template

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter c = new Jackson2JsonMessageConverter();
        c.setCreateMessageIds(true);
        return c;
    }
    @Bean
    RabbitTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
