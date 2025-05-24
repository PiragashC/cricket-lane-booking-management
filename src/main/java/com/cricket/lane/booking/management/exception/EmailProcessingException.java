package com.cricket.lane.booking.management.exception;

public class EmailProcessingException extends RuntimeException{
    public EmailProcessingException(String message) {
        super(message);
    }

    public EmailProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
