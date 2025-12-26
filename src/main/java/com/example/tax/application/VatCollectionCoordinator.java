package com.example.tax.application;

import com.example.tax.application.dto.DataCollectionRequest;
import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.application.mapper.DataCollectionMapper;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VatCollectionCoordinator {

    private final DataCollectionMapper dataCollectionMapper;
    private final VatDataProcessor vatDataProcessor;
    private final CollectionTaskPort collectionTaskPort;

    public DataCollectionResponse getOrInitiateCollection(final DataCollectionRequest dataCollectionRequest) {

        final StoreId storeId = StoreId.of(dataCollectionRequest.getStoreId());
        Optional<CollectionTask> collectionTaskOptional
                = collectionTaskPort.findLastestTaskByStoreId(storeId, dataCollectionRequest.getTargetYearMonth());

        if (collectionTaskOptional.isPresent()) {
            CollectionTask collectionTask = collectionTaskOptional.get();
            return dataCollectionMapper.toDataCollectionResponse(collectionTask);
        }

        return vatDataProcessor
                .collectDataAndCalculateVat(storeId, dataCollectionRequest.getTargetYearMonth());
    }
}
