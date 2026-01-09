package com.example.tax.application.port.in.dto;

import com.example.tax.domain.valueobject.ManagerId;

public record AssignRoleCommand (ManagerId managerId, String storeId){
    public AssignRoleCommand(String managerId, String storeId){
        this(ManagerId.of(managerId), storeId);
    }
}
