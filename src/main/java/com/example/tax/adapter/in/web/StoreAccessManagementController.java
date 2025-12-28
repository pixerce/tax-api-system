package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.ApiResponse;
import com.example.tax.adapter.in.web.dto.UserRoleResponse;
import com.example.tax.adapter.in.web.dto.UserStoreAccessResponse;
import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.in.security.AdminOnly;
import com.example.tax.application.port.in.security.CheckStoreAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreAccessManagementController {

    private final StoreUseCase storeUseCase;

    @ResponseStatus(HttpStatus.OK)
    @AdminOnly
    @PostMapping("/{storeId}/assignments")
    public ApiResponse<UserStoreAccessResponse> assignRole(@PathVariable("storeId") String storeId
            , @RequestParam(name = "userSrl") Long userSrl) {
        log.info("request: storeId={}, userSrl={}", storeId, userSrl);

        return ApiResponse.of(storeUseCase.assignStoreToManager(storeId, userSrl));
    }

    @ResponseStatus(HttpStatus.OK)
    @AdminOnly
    @DeleteMapping("/{storeId}/assignments")
    public ApiResponse<UserStoreAccessResponse> deleteRole(@PathVariable("storeId") String storeId
            , @RequestParam(name = "userSrl") Long userSrl) {
        log.info("request: storeId={}, userSrl={}", storeId, userSrl);

        return ApiResponse.of(storeUseCase.deleteStoreFromManager(storeId, userSrl));
    }

    // 특정 상점에 권한이 있는지 조회한다.
    @ResponseStatus(HttpStatus.OK)
    @CheckStoreAccess
    @GetMapping("/{storeId}/permissions/me")
    public ApiResponse<UserRoleResponse> checkPermission(@PathVariable(name = "storeId") String storeId
            , @RequestParam(name = "userSrl") Long userSrl) {
        log.info("request: storeId={}, userSrl={}", storeId, userSrl);

        return ApiResponse.of(storeUseCase.checkPermission(userSrl, storeId));
    }
}
