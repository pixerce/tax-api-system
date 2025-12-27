package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public class DataProcessingCompletedEvent extends DataProcessingEvent {
    public DataProcessingCompletedEvent(final StoreId storeId, final YearMonth yearMonth) {
        super(storeId, yearMonth);
    }
}
