package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.CollectionTask;

import java.time.YearMonth;
import java.util.Optional;

public interface CollectionTaskPort {

    Optional<CollectionTask> findLastestTaskByStoreId(String storeId, YearMonth targetYearMonth);

    void upsert(CollectionTask collectionTask);

    void save(CollectionTask collectionTask);
}
