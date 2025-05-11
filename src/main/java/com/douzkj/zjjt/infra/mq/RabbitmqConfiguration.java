package com.douzkj.zjjt.infra.mq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.douzkj.zjjt.task.RecognizerQueue.*;

/**
 * @author ranger dong
 * @date 14:55 2025/3/30
 * @descrption
 * @copyright Spotter.ink
 */
@Configuration
public class RabbitmqConfiguration {

    @Bean
    public Queue taskQueue() {
        return new Queue(RECOGNIZER_QUEUE, true);
    }

    @Bean
    public Queue collectionQueue() {
        return new Queue(COLLECTION_QUEUE, true);
    }

    @Bean
    public Queue collectionQueueV2() {
        return new Queue(COLLECTION_QUEUE_V2, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
