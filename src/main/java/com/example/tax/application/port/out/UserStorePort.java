package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.StoreId;

public interface UserStorePort {
    void saveAccess(Long userSrl, Long storeSrl);
    void removeAccess(Long userSrl, String storeId);

    Boolean existsByUserSrlAndStoreId(Long userSrl, StoreId storeId);
}
