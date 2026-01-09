package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public class DataProcessingCompletedEvent extends DataProcessingEvent {
    public DataProcessingCompletedEvent(final Long id, final StoreId storeId, final YearMonth yearMonth) {
        super(id, storeId, yearMonth);
    }
}
