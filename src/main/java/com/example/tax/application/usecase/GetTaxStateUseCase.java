package com.example.tax.application.usecase;

import com.example.tax.adapter.in.web.dto.DataCollectionResponse;

import java.time.YearMonth;

public interface GetTaxStateUseCase {
    DataCollectionResponse getState(String storeId, YearMonth yearMonth);
}
