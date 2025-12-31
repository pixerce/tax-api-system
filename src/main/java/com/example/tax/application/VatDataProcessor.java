package com.example.tax.application;

import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.application.port.out.DataProcessingEventPort;
import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.DataCollectionProcessorFactory;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;
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
            , DataCollectionProcessorFactory dataCollectionProcessorFactory, DataProcessingEventPort dataProcessingEventPort) {
        this.executorService = executorService;
        this.dataCollectionProcessorFactory = dataCollectionProcessorFactory;
        this.dataProcessingEventPort = dataProcessingEventPort;
    }

    private final Executor executorService;
    private final DataCollectionProcessorFactory dataCollectionProcessorFactory;
    private final DataProcessingEventPort dataProcessingEventPort;

    public DataCollectionResponse collectDataAndCalculateVat(final StoreId storeId, final YearMonth targetYearMonth) {

        final DataCollectionProcessor dataCollectionProcessor = this.dataCollectionProcessorFactory.createDataCollectorTask(storeId, targetYearMonth);
        dataProcessingEventPort.publish(new DataProcessingStartedEvent(storeId, targetYearMonth));

        CompletableFuture.supplyAsync(dataCollectionProcessor::process, executorService)
                .thenRun(() -> {
                    dataCollectionProcessor.done();
                    dataProcessingEventPort.publish(new DataProcessingCompletedEvent(storeId, targetYearMonth));
                })
                .exceptionally(ex -> {
                    dataProcessingEventPort.publish(new DataProcessingFailedEvent(storeId, targetYearMonth
                            , "데이터 처리에 실패 했습니다. storeId: %s, targetYearMonth: %s".formatted(storeId, targetYearMonth), ex));
                    return null;
                });

        return DataCollectionResponse.createCollectingResponse(storeId, targetYearMonth);
    }
}
