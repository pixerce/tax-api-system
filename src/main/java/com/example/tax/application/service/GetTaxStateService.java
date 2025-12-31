package com.example.tax.application.service;

import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.application.mapper.DataCollectionMapper;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.usecase.GetTaxStateUseCase;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class GetTaxStateService implements GetTaxStateUseCase {

    private final CollectionTaskPort collectionTaskPort;
    private final DataCollectionMapper dataCollectionMapper;

    @Override
    public DataCollectionResponse getState(String storeId, YearMonth yearMonth) {
        return collectionTaskPort.findLastestTaskByStoreId(storeId, yearMonth)
                .map(dataCollectionMapper::toDataCollectionResponse)
                .orElseGet(() -> buildNotRequestedResponse(StoreId.of(storeId), yearMonth));
    }

    private DataCollectionResponse buildNotRequestedResponse(StoreId storeId, YearMonth yearMonth) {
        return DataCollectionResponse.builder()
                .status(TaskStatus.NOT_REQUESTED)
                .storeId(storeId)
                .yearMonth(yearMonth)
                .build();
    }
}
