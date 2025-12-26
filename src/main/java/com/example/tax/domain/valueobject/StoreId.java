package com.example.tax.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record StoreId(String value) {
    public static StoreId of(String value) {
        if (value == null || value.length() < 10) {
            throw new IllegalArgumentException("사업자 번호는 숫자 10자리여야 합니다.");
        }
        return new StoreId(value);
    }
}
