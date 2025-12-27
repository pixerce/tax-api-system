package com.example.tax.adapter.out.persistence.entity;

import com.example.tax.domain.valueobject.TransactionRecordType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "transaction_record", schema = "tax")
public class TransactionRecordEntity extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type")
    private TransactionRecordType transactionType;

    @Column(name="amount")
    private Long amount;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="transaction_date")
    private LocalDate transactionDate;

    @Column(name = "store_id")
    private String storeId;

    @Builder
    public TransactionRecordEntity(final TransactionRecordType transactionType, final Long amount
            , final LocalDate transactionDate, final String storeId) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.storeId = storeId;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TransactionRecordEntity(" +
                "id=" + this.getId() +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", storeId=" + storeId +
                ", createdAt=" + createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ")";
    }
}
