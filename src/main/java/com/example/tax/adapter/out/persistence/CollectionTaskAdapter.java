package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.adapter.out.persistence.mapper.CollectionTaskMapper;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CollectionTaskAdapter implements CollectionTaskPort {

    private final CollectionTaskRepository collectionTaskRepository;
    private final CollectionTaskMapper collectionTaskMapper;

    @Transactional
    @Override
    public void save(final CollectionTask collectionTask) {
        Optional<CollectionTaskEntity> collectionTaskEntityOptional
                = collectionTaskRepository.findFirstByStoreIdAndTargetYearMonthOrderByStartedAtDesc(
                        collectionTask.getStoreId().getId(), collectionTask.getTargetYearMonth());

        if (collectionTaskEntityOptional.isPresent()) {
            CollectionTaskEntity savedCollectionTaskEntity = collectionTaskEntityOptional.get();
            collectionTask.assignId(savedCollectionTaskEntity.getSrl());
        }

        CollectionTaskEntity collectionTaskEntity = collectionTaskMapper.toEntity(collectionTask);
        CollectionTaskEntity savedEntity = collectionTaskRepository.save(collectionTaskEntity);
        if (collectionTask.getId() == null) {
            collectionTask.assignId(savedEntity.getSrl());
        }
    }

    @Override
    public Optional<CollectionTask> findLastestTaskByStoreId(final String storeId, final YearMonth targetYearMonth) {
        Optional<CollectionTaskEntity> collectionTaskEntityOptional = this.collectionTaskRepository
                .findFirstByStoreIdAndTargetYearMonthOrderByStartedAtDesc(storeId, targetYearMonth);

        if (collectionTaskEntityOptional.isPresent())
            return Optional.of(this.collectionTaskMapper.toDomain(collectionTaskEntityOptional.get()));
        return Optional.empty();
    }
}
