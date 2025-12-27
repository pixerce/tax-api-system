package com.example.tax.application.port.in;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public interface DataProcessingStatusUseCase {

    void updateStatus(StoreId storeId, YearMonth targetYearMonth);
    void updateFailedStatus(StoreId storeId, YearMonth targetYearMonth, String errorMessage, Throwable throwable);
}
