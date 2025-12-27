package com.example.tax.adapter.out.persistence;

import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@Component
public class CollectionTaskAdapter implements CollectionTaskPort {

    @Override
    public void save(CollectionTask collectionTask) {
    }

    @Override
    public Optional<CollectionTask> findLastestTaskByStoreId(final StoreId storeId, final YearMonth targetYearMonth) {
        return Optional.empty();
    }
}
