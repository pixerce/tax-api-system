package com.example.tax.adapter.out.file;

import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelReaderAdapter implements DataSourceReaderPort {

    @Override
    public void readData(final StoreId storeId, final Consumer<List<TransactionRecord>> recordBatchConsumer) {

    }
}
