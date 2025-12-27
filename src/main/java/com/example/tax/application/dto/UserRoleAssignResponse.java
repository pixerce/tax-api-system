package com.example.tax.application.dto;

import lombok.Getter;

@Getter
public class UserRoleAssignResponse {

    private Boolean success;

    private UserRoleAssignResponse(Boolean success) {
        this.success = success;
    }

    public static UserRoleAssignResponse success() {
        return new UserRoleAssignResponse(Boolean.TRUE);
    }

    public static UserRoleAssignResponse failure() {
        return new UserRoleAssignResponse(Boolean.FALSE);
    }
}
