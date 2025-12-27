package com.example.tax.adapter.out.persistence.mapper;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.domain.valueobject.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toDomain(StoreEntity entity) {
        return new Store(
                entity.getSrl(),
                entity.getStoreId()
        );
    }
}
