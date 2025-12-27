package com.example.tax.adapter.out.persistence.mapper;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.domain.valueobject.CollectionTask;
import org.springframework.stereotype.Component;

@Component
public class CollectionTaskMapper {
    public CollectionTask toDomain(CollectionTaskEntity collectionTaskEntity) {
        return CollectionTask.builder()
                .id(collectionTaskEntity.getId())
                .errorMessage(collectionTaskEntity.getErrorMessage())
                .updatedAt(collectionTaskEntity.getEndedAt())
                .status(collectionTaskEntity.getStatus())
                .startedAt(collectionTaskEntity.getStartedAt())
                .storeId(collectionTaskEntity.getStoreId())
                .build();
    }

    public CollectionTaskEntity toEntity(CollectionTask collectionTask) {
        return CollectionTaskEntity.builder()
                .endedAt(collectionTask.getUpdatedAt())
                .status(collectionTask.getStatus())
                .errorMessage(collectionTask.getErrorMessage())
                .startedAt(collectionTask.getStartedAt())
                .storeId(collectionTask.getStoreId())
                .id(collectionTask.getId())
                .build();
    }

}
