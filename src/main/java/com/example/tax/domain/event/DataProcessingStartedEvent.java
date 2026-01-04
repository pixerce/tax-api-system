package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;


public class DataProcessingStartedEvent extends DataProcessingEvent {
    public DataProcessingStartedEvent(final Long id, final StoreId storeId, final YearMonth yearMonth) {
        super(id, storeId, yearMonth);
    }
}
