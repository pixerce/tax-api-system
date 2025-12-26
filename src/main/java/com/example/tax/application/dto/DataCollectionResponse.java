package com.example.tax.application.dto;

import com.example.tax.domain.valueobject.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class DataCollectionResponse {
    private String storeId;
    private TaskStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static DataCollectionResponse createCollectingResponse(final String storeId) {
        return DataCollectionResponse.builder()
                .status(TaskStatus.COLLECTING)
                .storeId(storeId)
                .startedAt(LocalDateTime.now())
                .build();
    }
}
