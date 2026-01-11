package com.example.tax.application.port.in.dto;

import com.example.tax.domain.valueobject.ManagerId;
import com.example.tax.domain.valueobject.StoreId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AssignRoleCommand {
    private final ManagerId managerId;
    private final StoreId storeId;

    private AssignRoleCommand(String managerId, String storeId) {
        this.managerId = ManagerId.of(managerId);
        this.storeId = StoreId.of(storeId);
    }

    public static AssignRoleCommand of(String managerId, String storeId) {
        return new AssignRoleCommand(managerId, storeId);
    }
}
