package com.cricket.lane.booking.management.api.controller.advice;

import com.cricket.lane.booking.management.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        log.error("Internal server error: ", ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", List.of("Internal Service Exception"));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value ={ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex) {
        log.error("Internal service error: ", ex);
        ApiError apiError = new ApiError(ex.getHttpStatus().value(), ex.getHeaderMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("Bad request: ", ex);
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format(fieldError.getDefaultMessage()))
                .toList();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad request", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiError {
        private int status;
        private String message;
        private List<String> errors;
    }

}

