package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.Store;
import com.example.tax.domain.valueobject.StoreId;

import java.util.List;
import java.util.Optional;

public interface StorePort {
    List<Store> findAll();
    List<Store> findByIds(List<Long> ids);
    Optional<Store> findByStoreId(StoreId storeId);
}
