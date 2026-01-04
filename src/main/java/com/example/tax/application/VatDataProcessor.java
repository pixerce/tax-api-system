package com.example.tax.application;

import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.DataCollectionProcessorFactory;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class VatDataProcessor {

    public VatDataProcessor(@Qualifier("collection-executor") Executor executorService
            , DataCollectionProcessorFactory dataCollectionProcessorFactory, ApplicationEventPublisher eventPublisher) {
        this.executorService = executorService;
        this.dataCollectionProcessorFactory = dataCollectionProcessorFactory;
        this.eventPublisher = eventPublisher;
    }

    private final Executor executorService;
    private final DataCollectionProcessorFactory dataCollectionProcessorFactory;
    private final ApplicationEventPublisher eventPublisher;

    public void collectDataAndCalculateVat(final CollectionTask collectionTask) {

        final DataCollectionProcessor dataCollectionProcessor = this.dataCollectionProcessorFactory
                .createDataCollectorTask(collectionTask);

        final StoreId storeId = collectionTask.getStoreId();
        final YearMonth targetYearMonth = collectionTask.getTargetYearMonth();
        CompletableFuture.supplyAsync(dataCollectionProcessor::process, executorService)
                .thenRun(() -> {
                    dataCollectionProcessor.done();
                    eventPublisher.publishEvent(
                            new DataProcessingCompletedEvent(collectionTask.getId(), storeId, targetYearMonth));
                })
                .exceptionally(ex -> {
                    eventPublisher.publishEvent(
                            new DataProcessingFailedEvent(collectionTask.getId(), storeId, targetYearMonth
                            , "데이터 처리에 실패 했습니다. storeId: %s, targetYearMonth: %s".formatted(storeId, targetYearMonth), ex));
                    return null;
                });
    }
}
