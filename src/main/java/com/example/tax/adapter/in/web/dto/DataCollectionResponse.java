package com.example.tax.adapter.in.web.dto;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Builder
@Getter
public class DataCollectionResponse {
    private StoreId storeId;
    private TaskStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endedAt;
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth yearMonth;

    public static DataCollectionResponse createCollectingResponse(final StoreId storeId, final YearMonth yearMonth ) {
        return DataCollectionResponse.builder()
                .status(TaskStatus.COLLECTING)
                .storeId(storeId)
                .startedAt(LocalDateTime.now())
                .yearMonth(yearMonth)
                .build();
    }
}
