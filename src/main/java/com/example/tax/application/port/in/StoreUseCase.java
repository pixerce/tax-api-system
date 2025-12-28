package com.example.tax.application.port.in;

import com.example.tax.adapter.in.web.dto.UserRoleResponse;
import com.example.tax.adapter.in.web.dto.UserStoreAccessResponse;

public interface StoreUseCase {
    UserRoleResponse checkPermission(final Long userSrl, final String storeId);

    UserStoreAccessResponse assignStoreToManager(String storeId, Long userSrl);
    UserStoreAccessResponse deleteStoreFromManager(String storeId, Long userSrl);
}
