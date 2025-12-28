package com.example.tax.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class ApiResponse<T> extends BaseResponse {

    private final List<T> data;

    @JsonCreator
    private ApiResponse(@JsonProperty("data") List<T> data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> of(T data) {
        if (data == null)
            return new ApiResponse<>(Collections.emptyList());

        return new ApiResponse<>(List.of(data));
    }

    public static <T> ApiResponse<T> of(List<T> data) {
        if (data == null)
            return new ApiResponse<>(Collections.emptyList());

        return new ApiResponse<>(data);
    }

    public List<T> getData() {
        return data;
    }
}