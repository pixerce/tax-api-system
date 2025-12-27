package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;


public class DataProcessingStartedEvent extends DataProcessingEvent {
    public DataProcessingStartedEvent(final StoreId storeId, final YearMonth yearMonth) {
        super(storeId, yearMonth);
    }
}
