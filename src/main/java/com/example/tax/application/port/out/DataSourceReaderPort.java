package com.example.tax.application.port.out;


import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;

import java.util.List;
import java.util.function.Consumer;

public interface DataSourceReaderPort {
    void readData(StoreId storeId, Consumer<List<TransactionRecord>> recordBatchConsumer);
}