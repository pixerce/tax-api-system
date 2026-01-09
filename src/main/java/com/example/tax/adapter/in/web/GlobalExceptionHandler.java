package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.ErrorResponse;
import com.example.tax.application.port.in.security.exception.AccessDeniedException;
import com.example.tax.domain.exception.InvalidStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDenied(AccessDeniedException e) {
        log.error("message={}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleBadRequest(IllegalArgumentException e) {
        log.error("message={}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidStateException.class)
    public ErrorResponse handleInvalidStatus(InvalidStateException e) {
        log.error("message={}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("message={}", e.getMessage(), e);
        return new ErrorResponse("시스템 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }

}