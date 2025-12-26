package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@Component
public interface CollectionTaskPort {

    Optional<CollectionTask> findLastestTaskByStoreId(StoreId storeId, YearMonth targetYearMonth);
}
