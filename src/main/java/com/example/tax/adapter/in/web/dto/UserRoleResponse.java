package com.example.tax.adapter.in.web.dto;

import com.example.tax.domain.valueobject.StoreId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserRoleResponse {
    private final StoreId storeId;
    private final Boolean hasAccessPermission;
}
