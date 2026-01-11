package com.example.tax.application.service;

import com.example.tax.adapter.in.web.dto.UserRoleResponse;
import com.example.tax.adapter.in.web.dto.UserStoreAccessResponse;
import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.in.dto.AssignRoleCommand;
import com.example.tax.application.port.out.StorePort;
import com.example.tax.application.port.out.UserPort;
import com.example.tax.application.port.out.UserStorePort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.Store;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {
    private final UserPort userPort;
    private final StorePort storePort;
    private final UserStorePort userStorePort;

    @Override
    public UserRoleResponse checkPermission(final Long userSrl, final String storeId) {
        userPort.checkExistUser(userSrl);

        Boolean result = userStorePort.existsByUserSrlAndStoreId(userSrl, StoreId.of(storeId));
        return UserRoleResponse.builder()
                .storeId(StoreId.of(storeId))
                .hasAccessPermission(result)
                .build();
    }

    @Override
    public UserStoreAccessResponse assignStoreToManager(final AssignRoleCommand assignRoleCommand) {
        userPort.checkExistUser(assignRoleCommand.getManagerId().getId());
        final Long managerId = assignRoleCommand.getManagerId().getId();
        final Optional<Store> storeOptional = storePort.findByStoreId(assignRoleCommand.getStoreId());
        if (storeOptional.isEmpty())
            throw new InvalidStateException("해당 사업장이 존재하지 않습니다.");

        userStorePort.saveAccess(managerId, storeOptional.get().getSrl());

        return new UserStoreAccessResponse(userStorePort.getAccessibleStores(managerId));
    }

    @Override
    public UserStoreAccessResponse deleteStoreFromManager(String storeIdStr, Long userSrl) {
        userPort.checkExistUser(userSrl);

        final StoreId storeId = StoreId.of(storeIdStr);
        final Optional<Store> storeOptional = storePort.findByStoreId(storeId);
        if (storeOptional.isEmpty())
            throw new InvalidStateException("해당 사업장이 존재하지 않습니다.");

        userStorePort.removeAccess(userSrl, storeOptional.get().getSrl());
        return new UserStoreAccessResponse(userStorePort.getAccessibleStores(userSrl));
    }
}