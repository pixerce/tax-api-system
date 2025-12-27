package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.domain.valueobject.StoreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface CollectionTaskRepository extends JpaRepository<CollectionTaskEntity, Long> {

    Optional<CollectionTaskEntity> findFirstByStoreIdAndTargetYearMonthOrderByStartedAtDesc(
            StoreId storeId, YearMonth targetYearMonth
    );

}
