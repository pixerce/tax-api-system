package com.example.tax.adapter.in.web;

import com.example.tax.application.dto.UserRoleResponse;
import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.in.security.CheckStoreAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminRolesController {

    private final StoreUseCase storeUseCase;

    /**
     * 특정 상점에 권한이 있는지 조회한다.
     */
    @CheckStoreAccess
    @GetMapping("/{userSrl}/roles")
    public ResponseEntity<UserRoleResponse> checkRoles(@PathVariable Long userSrl, @RequestParam(name = "storeId") String storeId) {
        UserRoleResponse response = storeUseCase.checkRoles(userSrl, storeId);
        return ResponseEntity.ok(response);
    }

}
