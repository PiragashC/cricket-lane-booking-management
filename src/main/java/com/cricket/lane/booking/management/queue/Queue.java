package com.cricket.lane.booking.management.queue;

import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Builder
@Slf4j
public class Queue {
    private final BlockingQueue<QueueMessage> queue = new LinkedBlockingQueue<>();
    private ExecutorService executor;
    private final QueueListener listener;
    private int consumerCount;

    @PostConstruct
    public void afterPropertiesSet() {
        validate();
        executor = Executors.newFixedThreadPool(10);
        for (int count = 1; count <= consumerCount; count++) {
            log.info("Consumer " + count + " is scheduled to be invoked");
            Consumer consumer = new Consumer(queue, "Consumer " + count, listener);
            executor.submit(consumer);
        }
    }

    private void validate() {
        if (consumerCount == 0) {
            log.warn("Transceiver is configured with ZERO consumers & no listener will be invoked");
        }
    }

    public void submit(QueueMessage queueMessage) {
        queue.add(queueMessage);
    }

    @RequiredArgsConstructor
    private class Consumer implements Runnable {
        private final BlockingQueue<QueueMessage> queue;
        private final String consumerName;
        private final QueueListener listener;

        @Override
        public void run() {
            try {
                while (true) {
                    QueueMessage queueMessage = queue.take();
                    if (queueMessage != null) {
                        executor.submit(() -> listener.execute(queueMessage));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
