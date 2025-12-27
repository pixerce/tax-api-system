package com.example.tax.adapter.out.persistence.mapper;

import com.example.tax.adapter.out.persistence.entity.TransactionRecordEntity;
import com.example.tax.domain.valueobject.TransactionRecord;
import org.springframework.stereotype.Component;

@Component
public class TransactionRecordMapper {

    public TransactionRecordEntity toEntity(TransactionRecord transactionRecord) {
        return TransactionRecordEntity.builder()
                .transactionType(transactionRecord.getTransactionType())
                .amount(transactionRecord.getAmount().longValue())
                .storeId(transactionRecord.getStoreId().getId())
                .transactionDate(transactionRecord.getTransactionDate())
                .build();
    }
}
