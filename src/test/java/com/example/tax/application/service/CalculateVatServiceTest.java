package com.example.tax.application.service;

import com.example.tax.adapter.out.persistence.VatRateSourceAdapter;
import com.example.tax.application.port.in.CalculateVatUseCase;
import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalculateVatServiceTest {

    @Mock
    private TransactionRecordPort transactionRecordPort;
    @Mock
    private StoreVatPort storeVatPort;

    @Test
    @DisplayName("상점 ID와 년월이 주어지면 매출/매입을 합산하고 부가세를 계산하여 저장한다")
    void calculateAndStore_success() {
        StoreId storeId = StoreId.of("0123456789");
        YearMonth targetYearMonth = YearMonth.of(2025, 12);

        CalculateVatUseCase calculateVatUseCase = new CalculateVatService(transactionRecordPort, storeVatPort, new VatRateSourceAdapter());

        given(transactionRecordPort.sumSalesAmountByMonth(storeId, targetYearMonth)).willReturn(BigDecimal.valueOf(110000L));    // 매출
        given(transactionRecordPort.sumPurchaseAmountByMonth(storeId, targetYearMonth)).willReturn(BigDecimal.valueOf(55000L)); // 매입

        ArgumentCaptor<StoreVat> storeVatCaptor = ArgumentCaptor.forClass(StoreVat.class);

        calculateVatUseCase.calculateAndStore(storeId, targetYearMonth);

        verify(storeVatPort).save(storeVatCaptor.capture());
        StoreVat savedStoreVat = storeVatCaptor.getValue();

        assertThat(savedStoreVat.getStoreId()).isEqualTo(storeId);
        assertThat(savedStoreVat.getTargetYearMonth()).isEqualTo(targetYearMonth);
        assertThat(savedStoreVat.getSales().getAmount()).isEqualByComparingTo("110000");
        assertThat(savedStoreVat.getPurchase().getAmount()).isEqualByComparingTo("55000");

        assertThat(savedStoreVat.getVat().getAmount()).isEqualByComparingTo("5000");
    }
}