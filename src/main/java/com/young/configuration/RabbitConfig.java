package com.young.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by young on 2017-4-26.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue zhihuQueue() {
        return new Queue("zhihuAnswer");
    }
}
