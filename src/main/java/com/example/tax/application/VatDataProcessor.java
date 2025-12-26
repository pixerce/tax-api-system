package com.example.tax.application;

import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.DataCollectionProcessorFactory;
import com.example.tax.domain.valueobject.StoreId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class VatDataProcessor {

    public VatDataProcessor(@Qualifier("collection-executor") Executor executorService
            , DataCollectionProcessorFactory dataCollectionProcessorFactory) {
        this.executorService = executorService;
        this.dataCollectionProcessorFactory = dataCollectionProcessorFactory;
    }

    private final Executor executorService;
    private final DataCollectionProcessorFactory dataCollectionProcessorFactory;

    public DataCollectionResponse collectDataAndCalculateVat(final StoreId storeId, final YearMonth targetYearMonth) {

        final DataCollectionProcessor dataCollectionProcessor = this.dataCollectionProcessorFactory.createDataCollectorTask(storeId, targetYearMonth);
        dataCollectionProcessor.started();

        CompletableFuture.supplyAsync(dataCollectionProcessor::process, executorService)
                .thenRun(() -> {
                    dataCollectionProcessor.finished();
                })
                .exceptionally(ex -> {
                    dataCollectionProcessor.failed();
                    return null;
                });

        return DataCollectionResponse.createCollectingResponse(storeId.value());
    }
}
