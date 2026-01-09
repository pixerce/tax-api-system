package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.StoreId;

import java.util.List;

public interface UserStorePort {
    void saveAccess(Long userSrl, Long storeSrl);
    void removeAccess(Long userSrl, Long storeSrl);

    Boolean existsByUserSrlAndStoreId(Long userSrl, StoreId storeId);

    List<String> getAccessibleStores(Long userSrl);
}
