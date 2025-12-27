package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;
import lombok.Getter;

import java.time.YearMonth;

@Getter
public class DataProcessingFailedEvent extends DataProcessingEvent {
    private final String errorMessage;
    private final Throwable throwable;

    public DataProcessingFailedEvent(final StoreId storeId, final YearMonth yearMonth, final String errorMessage) {
        this(storeId, yearMonth, errorMessage, null);
    }

    public DataProcessingFailedEvent(final StoreId storeId, final YearMonth yearMonth, final String errorMessage, final Throwable throwable) {
        super(storeId, yearMonth);
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }
}
