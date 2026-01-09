package com.example.tax.application.port.in;

import com.example.tax.adapter.in.web.dto.UserRoleResponse;
import com.example.tax.adapter.in.web.dto.UserStoreAccessResponse;
import com.example.tax.application.port.in.dto.AssignRoleCommand;

public interface StoreUseCase {
    UserRoleResponse checkPermission(final Long userSrl, final String storeId);

    UserStoreAccessResponse assignStoreToManager(AssignRoleCommand assignRoleCommand);
    UserStoreAccessResponse deleteStoreFromManager(String storeId, Long userSrl);
}
