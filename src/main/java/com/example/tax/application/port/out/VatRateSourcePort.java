package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.VatRate;

import java.time.YearMonth;
import java.util.Optional;

public interface VatRateSourcePort {

    Optional<VatRate> findVatRate(StoreId storeId, YearMonth yearMonth);
}
