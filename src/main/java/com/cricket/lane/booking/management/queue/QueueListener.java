package com.cricket.lane.booking.management.queue;

public interface QueueListener {
    void execute(QueueMessage queueMessage);
}
