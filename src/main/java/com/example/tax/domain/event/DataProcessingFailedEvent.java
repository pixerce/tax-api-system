package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.StoreId;
import lombok.Getter;

import java.time.YearMonth;

@Getter
public class DataProcessingFailedEvent extends DataProcessingEvent {
    private final String errorMessage;
    private final Throwable throwable;

    public DataProcessingFailedEvent(final Long id, final StoreId storeId, final YearMonth yearMonth, final String errorMessage) {
        this(id, storeId, yearMonth, errorMessage, null);
    }

    public DataProcessingFailedEvent(final Long id, final StoreId storeId, final YearMonth yearMonth, final String errorMessage, final Throwable throwable) {
        super(id, storeId, yearMonth);
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }
}
