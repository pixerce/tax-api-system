package com.example.tax.domain.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    protected BusinessException(final String message) {
        super(message);
    }

    protected BusinessException(final String message, Throwable cause) {
        super(message, cause);
    }
}