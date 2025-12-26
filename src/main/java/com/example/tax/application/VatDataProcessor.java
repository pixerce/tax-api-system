package com.example.tax.application;

import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.ExcelDataCollectionProcessor;
import com.example.tax.application.service.TaskMonitor;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
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
            , DataSourceReaderPort dataSourceReaderPort, TransactionRecordPort transactionRecordPort
            , TaskMonitor taskMonitor) {
        this.executorService = executorService;
        this.dataSourceReaderPort = dataSourceReaderPort;
        this.transactionRecordPort = transactionRecordPort;
        this.taskMonitor = taskMonitor;
    }

    private final Executor executorService;
    private final DataSourceReaderPort dataSourceReaderPort;
    private final TransactionRecordPort transactionRecordPort;
    private final TaskMonitor taskMonitor;

    public DataCollectionResponse collectDataAndCalculateVat(final StoreId storeId, final YearMonth targetYearMonth) {

        taskMonitor.updateStatus(storeId, TaskStatus.COLLECTING);

        final DataCollectionProcessor dataCollectionProcessor = ExcelDataCollectionProcessor.builder()
                .dataSourceReaderPort(dataSourceReaderPort)
                .transactionRecordPort(transactionRecordPort)
                .storeId(storeId)
                .build();

        CompletableFuture.supplyAsync(dataCollectionProcessor::process, executorService)
                .thenRun(() -> {
                    taskMonitor.updateStatus(storeId, TaskStatus.COLLECTED);
                })
                .exceptionally(ex -> {
                    taskMonitor.updateStatus(storeId, TaskStatus.FAILED);
                    return null;
                });

        return DataCollectionResponse.createCollectingResponse(storeId.value());
    }
}
