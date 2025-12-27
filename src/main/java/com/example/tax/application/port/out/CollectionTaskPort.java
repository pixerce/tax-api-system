package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;
import java.util.Optional;

public interface CollectionTaskPort {

    Optional<CollectionTask> findLastestTaskByStoreId(StoreId storeId, YearMonth targetYearMonth);

    void save(CollectionTask collectionTask);
}
