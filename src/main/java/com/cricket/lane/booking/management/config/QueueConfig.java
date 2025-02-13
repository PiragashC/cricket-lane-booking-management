package com.cricket.lane.booking.management.config;

import com.cricket.lane.booking.management.queue.Queue;
import com.cricket.lane.booking.management.service.listener.EmailSenderQueueListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue transceiver(EmailSenderQueueListener listener) {
        Queue queue = Queue.builder()
                .listener(listener)
                .consumerCount(5)
                .build();
        return queue;
    }
}
