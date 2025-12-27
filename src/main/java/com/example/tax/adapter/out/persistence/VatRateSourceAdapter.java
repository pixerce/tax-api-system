package com.example.tax.adapter.out.persistence;

import com.example.tax.application.port.out.VatRateSourcePort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.VatRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Optional;

@Component
public class VatRateSourceAdapter implements VatRateSourcePort {

    /**
     * 수수료율까지 db 에 보관하여 필요에 따라서 읽는 것은 시간이 부족하여 미구현 함.
     * 과제에 주어진 수수료율만 하드코딩하여 사용.
     */
    @Override
    public Optional<VatRate> findVatRate(final StoreId storeId, final YearMonth yearMonth) {
        return Optional.of(VatRate.builder()
                .storeId(storeId)
                .rate(BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(11), 5, RoundingMode.HALF_UP))
                .targetYearMonth(yearMonth)
                .build());

    }
}
