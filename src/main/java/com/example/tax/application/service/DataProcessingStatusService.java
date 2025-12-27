package com.example.tax.application.service;

import com.example.tax.application.port.in.DataProcessingStatusUseCase;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@RequiredArgsConstructor
@Component
public class DataProcessingStatusService implements DataProcessingStatusUseCase {

    private final CollectionTaskPort collectionTaskPort;

    @Override
    public void updateStatus(StoreId storeId, YearMonth targetYearMonth) {
    }

    @Override
    public void updateFailedStatus(StoreId storeId, YearMonth targetYearMonth, String errorMessage, Throwable throwable) {
    }
}
