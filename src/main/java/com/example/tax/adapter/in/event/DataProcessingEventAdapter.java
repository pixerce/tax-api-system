package com.example.tax.adapter.in.event;

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

    @Async
    @EventListener
    public void handleStartedEvent(final DataProcessingStartedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        final CollectionTask collectionTask = CollectionTask.create(storeId, targetYearMonth);
        collectionTask.started();
        this.collectionTaskPort.save(collectionTask);
    }

    @Async
    @EventListener
    public void handleCompletedEvent(final DataProcessingCompletedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        final CollectionTask collectionTask = CollectionTask.create(storeId, targetYearMonth);
        collectionTask.finished();
        this.collectionTaskPort.save(collectionTask);
    }

    @Async
    @EventListener
    public void handleCompletedEventAndCalculate(final DataProcessingCompletedEvent event) {
    }

    @Async
    @EventListener
    public void handleFailedEvent(final DataProcessingFailedEvent event) {
        final StoreId storeId = event.getStoreId();
        final YearMonth targetYearMonth = event.getTargetYearMonth();

        final CollectionTask collectionTask = CollectionTask.create(storeId, targetYearMonth);
        collectionTask.failed(event.getErrorMessage());
        this.collectionTaskPort.save(collectionTask);
    }
}
