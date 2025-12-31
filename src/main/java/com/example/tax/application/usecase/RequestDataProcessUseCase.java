package com.example.tax.application.usecase;

import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.in.web.dto.DataCollectionResponse;

public interface RequestDataProcessUseCase {
    DataCollectionResponse requestDataProcess(DataCollectionRequest request);
}
