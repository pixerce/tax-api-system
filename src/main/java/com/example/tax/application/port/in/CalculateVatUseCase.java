package com.example.tax.application.port.in;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public interface CalculateVatUseCase {

    void calculateAndStore(StoreId storeId, YearMonth yearMonth);
}
