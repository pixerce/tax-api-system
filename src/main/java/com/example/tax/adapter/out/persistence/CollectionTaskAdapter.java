package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.adapter.out.persistence.mapper.CollectionTaskMapper;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CollectionTaskAdapter implements CollectionTaskPort {

    private final CollectionTaskRepository collectionTaskRepository;
    private final CollectionTaskMapper collectionTaskMapper;

    @Override
    public void save(CollectionTask collectionTask) {
        CollectionTaskEntity collectionTaskEntity = collectionTaskMapper.toEntity(collectionTask);
        CollectionTaskEntity savedEntity = collectionTaskRepository.save(collectionTaskEntity);
        if (collectionTask.getId() == null) {
            collectionTask.assignId(savedEntity.getId());
        }
    }

    @Override
    public Optional<CollectionTask> findLastestTaskByStoreId(final StoreId storeId, final YearMonth targetYearMonth) {
        Optional<CollectionTaskEntity> collectionTaskEntityOptional = this.collectionTaskRepository
                .findFirstByStoreIdAndTargetYearMonthOrderByStartedAtDesc(storeId, targetYearMonth);

        if (collectionTaskEntityOptional.isPresent())
            return Optional.of(this.collectionTaskMapper.toDomain(collectionTaskEntityOptional.get()));
        return Optional.empty();
    }
}
