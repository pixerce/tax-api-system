package com.example.tax.application.mapper;

import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.domain.valueobject.CollectionTask;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionMapper {

    public DataCollectionResponse toDataCollectionResponse(final CollectionTask collectionTask) {
        return DataCollectionResponse.builder()
                .storeId(collectionTask.getStoreId().value())
                .status(collectionTask.getStatus())
                .startedAt(collectionTask.getStartedAt())
                .endedAt(collectionTask.getUpdatedAt())
                .build();
    }
}
