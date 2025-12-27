package com.example.tax.application.port.in;

import com.example.tax.application.dto.UserRoleResponse;

public interface StoreUseCase {
    UserRoleResponse checkRoles(final Long userSrl, final String storeId);

    void assignStoreToManager(String storeId, Long userSrl);
    void deleteStoreFromManager(String storeId, Long userSrl);
}
