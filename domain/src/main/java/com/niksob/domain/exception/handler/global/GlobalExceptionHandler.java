package com.niksob.domain.exception.handler.global;

import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.mapper.rest.response.error.ErrorDetailsMapper;
import com.niksob.domain.model.rest.response.error.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private ErrorDetailsMapper errorDetailsMapper;

    @ExceptionHandler(ControllerResponseException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatusException(ControllerResponseException e) {

        final HttpStatus httpStatus = e.getHttpStatus();
        final ErrorDetails errorDetails = errorDetailsMapper.fromResponseStatusException(e);

        return ResponseEntity.status(httpStatus).body(errorDetails);
    }
}
