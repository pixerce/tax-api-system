package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;

import java.time.YearMonth;

public interface StoreVatPort {

    void save(StoreVat storeVat);

    StoreVat findByStoreIdAndYearMonth(StoreId storeId, YearMonth yearMonth);
}
