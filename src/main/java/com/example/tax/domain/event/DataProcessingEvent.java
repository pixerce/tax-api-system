package com.example.tax.domain.event;

import com.example.tax.domain.valueobject.EventId;
import com.example.tax.domain.valueobject.StoreId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

@RequiredArgsConstructor
@Getter
public abstract class DataProcessingEvent {
    private final EventId eventId;
    private final LocalDateTime occurredAt;

    private final StoreId storeId;
    private final YearMonth targetYearMonth;

    protected DataProcessingEvent(StoreId storeId, YearMonth targetYearMonth) {
        this.eventId = EventId.create();
        this.occurredAt = LocalDateTime.now();
        this.storeId = storeId;
        this.targetYearMonth = targetYearMonth;
    }
}
