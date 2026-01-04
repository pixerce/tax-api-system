package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.adapter.out.persistence.mapper.CollectionTaskMapper;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.exception.DuplicateCollectionTaskException;
import com.example.tax.domain.valueobject.CollectionTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CollectionTaskAdapter implements CollectionTaskPort {

    private final CollectionTaskRepository collectionTaskRepository;
    private final CollectionTaskMapper collectionTaskMapper;

    @Override
    public void save(CollectionTask collectionTask) {
        CollectionTaskEntity collectionTaskEntity = collectionTaskMapper.toEntity(collectionTask);
        try {
            this.collectionTaskRepository.saveAndFlush(collectionTaskEntity);
            collectionTask.assignId(collectionTaskEntity.getSrl());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCollectionTaskException("이미 처리 중인 수집 작업이 존재합니다.", e);
        }
    }

    @Transactional
    @Override
    public void upsert(final CollectionTask collectionTask) {
        Optional<CollectionTaskEntity> collectionTaskEntityOptional
                = collectionTaskRepository.findById(collectionTask.getId());

        if (collectionTaskEntityOptional.isPresent()) {
            CollectionTaskEntity savedCollectionTaskEntity = collectionTaskEntityOptional.get();
            collectionTask.assignId(savedCollectionTaskEntity.getSrl());
        }

        final CollectionTaskEntity collectionTaskEntity = collectionTaskMapper.toEntity(collectionTask);
        final CollectionTaskEntity savedEntity = collectionTaskRepository.save(collectionTaskEntity);
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
