package com.example.tax.domain.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Store {
    private final Long srl;
    private final String storeId;
}
