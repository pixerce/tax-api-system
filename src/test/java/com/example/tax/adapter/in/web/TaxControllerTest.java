package com.example.tax.adapter.in.web;

import com.example.tax.application.VatCollectionCoordinator;
import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = TaxController.class) // 실제 컨트롤러 클래스명 기입
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VatCollectionCoordinator vatCollectionCoordinator;

    @Test
    @DisplayName("상점 데이터 수집 상태 조회 테스트 - JSON 파싱 검증")
    void getTaxDataProcessCurrentState_Success() throws Exception {
        // Given
        String storeIdStr = "1234567890";
        YearMonth targetMonth = YearMonth.of(2025, 12);

        DataCollectionResponse mockResponse = DataCollectionResponse.builder()
                .storeId(StoreId.of(storeIdStr))
                .status(TaskStatus.COLLECTED)
                .yearMonth(targetMonth)
                .startedAt(LocalDateTime.now())
                .build();

        given(vatCollectionCoordinator.getState(storeIdStr, targetMonth))
                .willReturn(mockResponse);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tax/{storeId}/state", storeIdStr)
                        .param("yearMonth", "2025-12") // 쿼리 파라미터 전달
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeIdStr))
                .andExpect(jsonPath("$.status").value("COLLECTED"))
                .andExpect(jsonPath("$.yearMonth").value("2025-12"))
                .andDo(print());
    }
}