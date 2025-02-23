package com.cricket.lane.booking.management.config;

import com.cricket.lane.booking.management.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getHttpStatus().value());
        response.put("message", ex.getMessage());
        response.put("errors", Collections.singletonList(ex.getHeaderMessage()));

        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
}