package com.cricket.lane.booking.management.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueueMessage<T> {
    private T data;
}
