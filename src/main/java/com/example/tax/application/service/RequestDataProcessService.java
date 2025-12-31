package com.example.tax.application.service;

import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.application.VatDataProcessor;
import com.example.tax.application.mapper.DataCollectionMapper;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.usecase.RequestDataProcessUseCase;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class RequestDataProcessService implements RequestDataProcessUseCase {

    private final CollectionTaskPort collectionTaskPort;
    private final DataCollectionMapper dataCollectionMapper;
    private final VatDataProcessor vatDataProcessor;

    @Override
    public DataCollectionResponse requestDataProcess(DataCollectionRequest request) {
        StoreId storeId = StoreId.of(request.getStoreId());
        YearMonth targetYearMonth = request.getTargetYearMonth();

        return collectionTaskPort.findLastestTaskByStoreId(
                        storeId.getId(),
                        targetYearMonth)
                .map(dataCollectionMapper::toDataCollectionResponse)
                .orElseGet(() -> {
                    CollectionTask newTask = CollectionTask.create(storeId, targetYearMonth);
                    collectionTaskPort.save(newTask);

                    vatDataProcessor.collectDataAndCalculateVat(storeId, targetYearMonth);

                    return DataCollectionResponse.createCollectingResponse(storeId, targetYearMonth);
                });
    }
}
