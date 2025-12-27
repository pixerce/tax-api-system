package com.example.tax.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public class StoreId {

    private final String id;

    public static StoreId of(final String value) {
        if (value == null || value.length() < 10) {
            throw new IllegalArgumentException("사업자 번호는 숫자 10자리여야 합니다.");
        }
        return new StoreId(value);
    }

    private StoreId(final String value) {
        this.id = value;
    }

    @JsonValue
    public String getStoreId() {
        return this.id;
    }
}
