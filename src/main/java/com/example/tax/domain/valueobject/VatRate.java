package com.example.tax.domain.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@RequiredArgsConstructor
@Builder
public class VatRate {

    private final StoreId storeId;
    private final YearMonth targetYearMonth;
    private final BigDecimal rate;
}
