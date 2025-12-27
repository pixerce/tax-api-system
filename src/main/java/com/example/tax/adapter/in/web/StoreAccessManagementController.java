package com.example.tax.adapter.in.web;

import com.example.tax.application.dto.UserRoleAssignResponse;
import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.in.security.AdminOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreAccessManagementController {

    private final StoreUseCase storeUseCase;

    @AdminOnly
    @PostMapping("/{storeId}/assignments")
    public ResponseEntity<UserRoleAssignResponse> assignRole(@PathVariable("storeId") String storeId
            , @RequestParam(name = "userSrl") Long userSrl) {
        storeUseCase.assignStoreToManager(storeId, userSrl);

        return ResponseEntity.ok(UserRoleAssignResponse.success());
    }

    @AdminOnly
    @DeleteMapping("/{storeId}/assignments")
    public ResponseEntity<UserRoleAssignResponse> deleteRole(@PathVariable("storeId") String storeId
            , @RequestParam(name = "userSrl") Long userSrl) {
        storeUseCase.assignStoreToManager(storeId, userSrl);

        return ResponseEntity.ok(UserRoleAssignResponse.success());
    }
}
