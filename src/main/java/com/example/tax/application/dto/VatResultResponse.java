package com.example.tax.application.dto;

import com.example.tax.domain.valueobject.StoreId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@Builder
public class VatResultResponse {
    private Long vat;
    private StoreId storeId;
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth yearMonth;
}
