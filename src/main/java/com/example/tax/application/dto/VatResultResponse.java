package com.example.tax.application.dto;

import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public class VatResultResponse {
    private String vat;
    private StoreId storeId;
    private YearMonth yearMonth;
}
