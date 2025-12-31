package com.example.tax.application.usecase;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public interface CalculateVatUseCase {
    void calculateVat(StoreId storeId, YearMonth yearMonth);
}
