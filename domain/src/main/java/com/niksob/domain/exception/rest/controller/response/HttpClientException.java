package com.niksob.domain.exception.rest.controller.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class HttpClientException extends RuntimeException {

    private final LocalDateTime timestamp;
    private final HttpStatus httpStatus;
    private final String path;

    public HttpClientException(LocalDateTime timestamp, HttpStatus httpStatus, String path) {
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(Throwable cause, HttpStatus httpStatus, String path) {
        super(cause.getMessage(), cause);
        this.timestamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(String message, HttpStatus httpStatus, String path) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(HttpStatus httpStatus, String message, LocalDateTime timestamp, String path) {
        super(message);
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(
            String message,
            Throwable cause,
            LocalDateTime timestamp,
            HttpStatus httpStatus,
            String path
    ) {
        super(message, cause);
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(Throwable cause, LocalDateTime timestamp, HttpStatus httpStatus, String path) {
        super(cause);
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public HttpClientException(
            String message,
            HttpStatus httpStatus,
            LocalDateTime timestamp,
            String path,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.path = path;
    }
}

