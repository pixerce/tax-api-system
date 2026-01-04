package com.example.tax.application.service;

import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.CollectionTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataCollectionProcessorFactory {
    private final DataSourceReaderPort dataSourceReaderPort;
    private final TransactionRecordPort transactionRecordPort;

    public DataCollectionProcessor createDataCollectorTask(final CollectionTask collectionTask, final Long duration) {
        final ExcelDataCollectionProcessor task = ExcelDataCollectionProcessor.builder()
                .dataSourceReaderPort(dataSourceReaderPort)
                .transactionRecordPort(transactionRecordPort)
                .storeId(collectionTask.getStoreId())
                .build();

        return new ExecutionTimeGuarantorDecorator(task, duration);
    }

    public DataCollectionProcessor createDataCollectorTask(final CollectionTask collectionTask) {
        return createDataCollectorTask(collectionTask, ExecutionTimeGuarantorDecorator.DEFAULT_EXECUTION_TIME);
    }
}


