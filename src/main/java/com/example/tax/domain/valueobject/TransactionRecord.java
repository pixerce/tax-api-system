package com.example.tax.domain.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@Getter
@Builder
@RequiredArgsConstructor
public class TransactionRecord {
    private final TransactionRecordType transactionType;
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final StoreId storeId;
}
