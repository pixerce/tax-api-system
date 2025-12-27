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

    public void assignId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public static CollectionTask create(StoreId storeId, YearMonth targetYearMonth) {
        return CollectionTask.builder()
                .storeId(storeId)
                .targetYearMonth(targetYearMonth)
                .status(TaskStatus.NOT_REQUESTED)
                .build();
    }

    public void started() {
        this.startedAt = LocalDateTime.now();
        this.status = TaskStatus.COLLECTING;
    }

    public void finished() {
        this.updatedAt = LocalDateTime.now();
        this.status = TaskStatus.COLLECTED;
    }

    public void failed(final String errorMessage) {
        this.updatedAt = LocalDateTime.now();
        this.status = TaskStatus.FAILED;
        this.errorMessage = errorMessage;
    }
}
