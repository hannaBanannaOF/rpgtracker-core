package com.hbsites.rpgtracker.core.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter c = new Jackson2JsonMessageConverter();
        c.setCreateMessageIds(true);
        return c;
    }
}
