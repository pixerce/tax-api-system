package com.example.tax.application.service;

import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.StoreId;
import lombok.Builder;

@Builder
public class ExcelDataCollectionProcessor implements DataCollectionProcessor {
    private final DataSourceReaderPort dataSourceReaderPort;
    private final TransactionRecordPort transactionRecordPort;
    private final StoreId storeId;

    public StoreId process() {
        dataSourceReaderPort.readData(storeId, transactionRecordPort::saveAll);
        transactionRecordPort.flush();
        return storeId;
    }

    @Override
    public void done() {
    }
}