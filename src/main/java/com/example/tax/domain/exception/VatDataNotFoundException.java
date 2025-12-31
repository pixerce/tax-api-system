package com.example.tax.domain.exception;

import java.time.YearMonth;

public class VatDataNotFoundException extends RuntimeException {
    public VatDataNotFoundException(String storeId, YearMonth yearMonth) {
        super(String.format("Vat data not found for storeId: %s, yearMonth: %s", storeId, yearMonth));
    }
}
