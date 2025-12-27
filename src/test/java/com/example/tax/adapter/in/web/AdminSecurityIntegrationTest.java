package com.example.tax.adapter.in.web;

import com.example.tax.application.port.in.StoreUseCase;
import com.example.tax.application.port.out.UserStorePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class AdminSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoreUseCase storeUseCase;

    @MockitoBean
    private UserStorePort userStorePort;

    @Test
    @DisplayName("CheckStoreAccess: ADMIN 권한자는 매니저 할당 여부와 상관없이 조회 가능하다")
    void checkRoles_AdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/1/roles")
                        .param("storeId", "STORE-01")
                        .header("X-Admin-Role", "ADMIN"))
                .andExpect(status().isOk());

        // Port 조회가 발생하지 않아야 함 (ADMIN은 pass 로직 때문)
        verify(userStorePort, never ()).existsByUserSrlAndStoreId(anyLong(), any());
    }

    @Test
    @DisplayName("CheckStoreAccess: MANAGER는 본인에게 할당된 상점인 경우 조회 가능하다")
    void checkRoles_ManagerSuccess() throws Exception {
        // given
        given(userStorePort.existsByUserSrlAndStoreId(anyLong(), any())).willReturn(true);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/roles")
                        .param("storeId", "0123456789")
                        .param("userSrl", "1")
                        .header("X-Admin-Role", "MANAGER")
                        .header("X-Admin-Id", "admin-01"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CheckStoreAccess: MANAGER라도 할당되지 않은 상점이면 403 Forbidden을 반환한다")
    void checkRoles_ManagerFail() throws Exception {
        // given
        given(userStorePort.existsByUserSrlAndStoreId(anyLong(), any())).willReturn(false);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/1/roles")
                        .param("storeId", "0123456789")
                        .header("X-Admin-Role", "MANAGER")
                        .header("X-Admin-Id", "admin-01"))
                .andExpect(status().isForbidden()); // AccessDeniedException 발생 시
    }

    // --- StoreAccessManagementController (@AdminOnly) 테스트 ---

    @Test
    @DisplayName("AdminOnly: ADMIN이 아닌 사용자가 할당 요청을 하면 실패한다")
    void assignRole_NotAdminFail() throws Exception {
        mockMvc.perform(post("/api/v1/stores/1023456789/assignments")
                        .param("userSrl", "1")
                        .header("X-Admin-Role", "MANAGER"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("AdminOnly: ADMIN 권한자는 상점 할당이 가능하다")
    void assignRole_AdminSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/stores/1203456789/assignments")
                        .param("userSrl", "1")
                        .header("X-Admin-Role", "ADMIN"))
                .andExpect(status().isOk());

        verify(storeUseCase, times(1)).assignStoreToManager(eq("1203456789"), eq(1L));
    }
}