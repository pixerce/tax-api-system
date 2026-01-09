package com.example.tax.adapter.in.event;

import com.example.tax.application.port.in.CalculateVatUseCase;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingEventAdapter {

    private final CollectionTaskPort collectionTaskPort;
    private final CalculateVatUseCase calculateVatUseCase;

    @Async
    @EventListener
    public void handleStartedEvent(final DataProcessingStartedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        log.info("data processing started, storedId={}, targetYearMonth={}", storeId, targetYearMonth);
    }

    @Async
    @EventListener
    public void handleCompletedEvent(final DataProcessingCompletedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        log.info("data processing completed, id={}, storedId={}, targetYearMonth={}", event.getId(), storeId, targetYearMonth);
        final CollectionTask collectionTask = CollectionTask.create(event.getId(), storeId, targetYearMonth);
        collectionTask.finished();
        this.collectionTaskPort.upsert(collectionTask);
    }

    @Async
    @EventListener
    public void handleCompletedEventAndCalculate(final DataProcessingCompletedEvent event) {
        log.info("calculate vat, id={}, storedId={}, targetYearMonth={}", event.getId(), event.getStoreId(), event.getTargetYearMonth());
        calculateVatUseCase.calculateAndStore(event.getStoreId(), event.getTargetYearMonth());
    }

    @Async
    @EventListener
    public void handleFailedEvent(final DataProcessingFailedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        log.info("data processing failed, id={}, storedId={}, targetYearMonth={}", event.getId(), storeId, targetYearMonth);
        final CollectionTask collectionTask = CollectionTask.create(event.getId(), storeId, targetYearMonth);
        collectionTask.failed(event.getErrorMessage());
        this.collectionTaskPort.upsert(collectionTask);
    }
}
