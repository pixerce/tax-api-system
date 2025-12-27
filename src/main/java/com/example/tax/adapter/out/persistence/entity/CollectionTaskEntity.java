package com.example.tax.adapter.out.persistence.entity;

import com.example.tax.adapter.out.persistence.converter.YearMonthStringConverter;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "collection_task", schema = "tax")
public class CollectionTaskEntity extends AbstractBaseEntity {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "store_id"))
    private StoreId storeId;

    @Convert(converter = YearMonthStringConverter.class)
    @Column(name = "target_year_month")
    private YearMonth targetYearMonth;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private TaskStatus status;

    @Column(name="started_at")
    private LocalDateTime startedAt;

    @Column(name="ended_at")
    private LocalDateTime endedAt;

    @Column(name="error_message")
    private String errorMessage;

    @Builder
    private CollectionTaskEntity(Long id, StoreId storeId, TaskStatus status, LocalDateTime startedAt
            , LocalDateTime endedAt, String errorMessage, YearMonth targetYearMonth) {
        this.id = id;
        this.storeId = storeId;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.errorMessage = errorMessage;
        this.targetYearMonth = targetYearMonth;
    }
}
