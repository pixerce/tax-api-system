package com.example.tax.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class DataCollectionRequest {

    private String storeId;
    private YearMonth targetYearMonth;
}
