package com.example.tax.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Getter
@Builder
@AllArgsConstructor
public class CollectionTask {

    private Long id;
    private StoreId storeId;
    private YearMonth targetYearMonth;
    private TaskStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private String errorMessage;

}
