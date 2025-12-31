package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.application.usecase.GetTaxStateUseCase;
import com.example.tax.application.usecase.GetVatUseCase;
import com.example.tax.application.usecase.RequestDataProcessUseCase;
import com.example.tax.domain.valueobject.Money;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import com.example.tax.domain.valueobject.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = TaxController.class)
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetVatUseCase getVatUseCase;

    @MockBean
    private GetTaxStateUseCase getTaxStateUseCase;

    @MockBean
    private RequestDataProcessUseCase requestDataProcessUseCase;

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

        given(getTaxStateUseCase.getState(storeIdStr, targetMonth))
                .willReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tax/{storeId}/state", storeIdStr)
                        .param("yearMonth", "2025-12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].storeId").value(storeIdStr))
                .andExpect(jsonPath("$.data[0].status").value("COLLECTED"))
                .andExpect(jsonPath("$.data[0].yearMonth").value("2025-12"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("상점의 부가세 결과 조회 테스트 - 형식 및 값 검증")
    void testGetVat() throws Exception {
        String storeIdStr = "0123456789";
        YearMonth targetMonth = YearMonth.of(2025, 12);
        long expectedVat = 1500000L;

        StoreVat mockResponse = StoreVat.builder()
                .vat(new Money(new BigDecimal(expectedVat)))
                .storeId(StoreId.of(storeIdStr))
                .targetYearMonth(targetMonth)
                .build();

        given(getVatUseCase.getVat(storeIdStr, targetMonth))
                .willReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tax/{storeId}/vat", storeIdStr)
                        .param("yearMonth", "2025-12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].vat").value(expectedVat))
                .andExpect(jsonPath("$.data[0].storeId").value(storeIdStr))
                .andExpect(jsonPath("$.data[0].yearMonth").value("2025-12"))
                .andDo(print());
    }
}
