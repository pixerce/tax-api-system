package com.example.tax.adapter.in.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ErrorResponse {

    private final ErrorBody error;

    public ErrorResponse(String message) {
        this.error = new ErrorBody(message);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorBody {
        private final String message;
    }
}