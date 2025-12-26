package com.example.tax.application.service;

import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@RequiredArgsConstructor
@Component
public class DataCollectionProcessorFactory {
    private final DataSourceReaderPort dataSourceReaderPort;
    private final TransactionRecordPort transactionRecordPort;

    public DataCollectionProcessor createDataCollectorTask(final StoreId storeId, final YearMonth targetYearMonth, final Long duration) {
        final ExcelDataCollectionProcessor task = ExcelDataCollectionProcessor.builder()
                .dataSourceReaderPort(dataSourceReaderPort)
                .transactionRecordPort(transactionRecordPort)
                .storeId(storeId)
                .build();

        return new ExecutionTimeGuarantorDecorator(task, duration);
    }

    public DataCollectionProcessor createDataCollectorTask(final StoreId storeId, final YearMonth targetYearMonth) {
        return createDataCollectorTask(storeId, targetYearMonth, ExecutionTimeGuarantorDecorator.DEFAULT_EXECUTION_TIME);
    }
}


