package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.UserRoleResponse;
import com.example.tax.adapter.in.web.dto.UserStoreAccessResponse;
import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.in.dto.AssignRoleCommand;
import com.example.tax.application.port.out.UserStorePort;
import com.example.tax.config.adapter.in.security.SecurityAspect;
import com.example.tax.domain.valueobject.ManagerId;
import com.example.tax.domain.valueobject.StoreId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(
        controllers = StoreAccessManagementController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                ManagementWebSecurityAutoConfiguration.class
        }
)
@Import({SecurityAspect.class, AopAutoConfiguration.class})
class StoreAccessManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoreUseCase storeUseCase;

    @MockitoBean
    private UserStorePort userStorePort;

    @Test
    @DisplayName("상점 권한 할당 - 기존 권한이 있는 상태에서 신규 상점 추가 성공")
    void assignRole_Success_WithExistingStores() throws Exception {
        // Given
        Long userSrl = 123L;
        String newStoreId = "3456789012";

        // 기존에 보유하고 있던 상점들 + 새로 추가된 상점을 포함한 응답 리스트
        UserStoreAccessResponse response = new UserStoreAccessResponse(List.of("0123456789", "1234567890", "3456789012"));

        given(storeUseCase.assignStoreToManager(new AssignRoleCommand(ManagerId.of("123"), newStoreId)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/stores/{storeId}/assignments", newStoreId)
                        .header("X-Admin-Role", "ADMIN")
                        .param("managerId", userSrl.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].accessibleStoreIds").isArray())
                .andExpect(jsonPath("$.data[0].accessibleStoreIds.length()").value(3))
                .andExpect(jsonPath("$.data[0].accessibleStoreIds[0]").value("0123456789"))
                .andExpect(jsonPath("$.data[0].accessibleStoreIds[1]").value("1234567890"))
                .andExpect(jsonPath("$.data[0].accessibleStoreIds[2]").value("3456789012"));
    }

    @Test
    @DisplayName("특정 상점의 권한 조회 - 성공")
    void checkRoles_Success() throws Exception {
        String userSrl = "123";
        String storeId = "1234567891";
        UserRoleResponse roleResponse = new UserRoleResponse(StoreId.of(storeId), true); // 예시 객체

        given(storeUseCase.checkPermission(anyLong(), anyString())).willReturn(roleResponse);
        given(userStorePort.existsByUserSrlAndStoreId(anyLong(), any())).willReturn(true);

        mockMvc.perform(get("/api/v1/stores/{storeId}/permissions/me", storeId)
                        .param("userSrl", userSrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data[0].storeId").value("1234567891"))
                .andExpect(jsonPath("$.data[0].hasAccessPermission").value(true));
    }

}