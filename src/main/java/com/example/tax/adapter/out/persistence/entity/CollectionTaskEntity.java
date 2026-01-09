package com.example.tax.adapter.out.persistence.entity;

import com.example.tax.adapter.out.persistence.converter.YearMonthStringConverter;
import com.example.tax.domain.valueobject.TaskStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.YearMonth;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
@Table(name = "collection_task", schema = "tax")
public class CollectionTaskEntity extends AbstractBaseEntity {

    @Column(name = "store_id")
    private String storeId;

    @Convert(converter = YearMonthStringConverter.class)
    @Column(name = "target_year_month")
    private YearMonth targetYearMonth;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private TaskStatus status;

    @CreatedDate
    @Column(name="started_at", updatable = false)
    private LocalDateTime startedAt;

    @LastModifiedDate
    @Column(name="ended_at")
    private LocalDateTime endedAt;

    @Column(name="error_message")
    private String errorMessage;

    @Builder
    private CollectionTaskEntity(Long srl, String storeId, TaskStatus status, LocalDateTime startedAt
            , LocalDateTime endedAt, String errorMessage, YearMonth targetYearMonth) {
        this.srl = srl;
        this.storeId = storeId;
        this.status = status;
        this.errorMessage = errorMessage;
        this.targetYearMonth = targetYearMonth;
    }

    @Override
    public String toString() {
        return "CollectionTaskEntity{" +
                "endedAt=" + endedAt +
                ", storeId='" + storeId + '\'' +
                ", targetYearMonth=" + targetYearMonth +
                ", status=" + status +
                ", startedAt=" + startedAt +
                ", errorMessage='" + errorMessage + '\'' +
                ", srl=" + srl +
                '}';
    }
}
