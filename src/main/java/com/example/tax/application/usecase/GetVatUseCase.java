package com.example.tax.application.usecase;

import com.example.tax.domain.valueobject.StoreVat;

import java.time.YearMonth;

public interface GetVatUseCase {
    StoreVat getVat(String storeId, YearMonth yearMonth);
}
