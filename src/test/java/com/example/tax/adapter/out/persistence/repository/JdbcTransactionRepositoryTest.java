package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import com.example.tax.domain.valueobject.TransactionRecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JdbcTransactionRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private JdbcTransactionRepository jdbcTransactionRepository;

    @Test
    @DisplayName("saveAll 호출 시 JdbcTemplate의 batchUpdate가 배치 크기 100으로 호출되어야 한다")
    void proveBatchUpdateCall() {

        StoreId storeId = StoreId.of("1234567890");
        List<TransactionRecord> transactions = List.of(
                createRecord(storeId, TransactionRecordType.SALE, 10000L),
                createRecord(storeId, TransactionRecordType.PURCHASE, 5000L),
                createRecord(storeId, TransactionRecordType.SALE, 20000L)
        );

        jdbcTransactionRepository.saveAll(transactions);

        verify(jdbcTemplate, times(1)).batchUpdate(
                anyString(),
                eq(transactions),
                eq(100),
                any(ParameterizedPreparedStatementSetter.class)
        );
    }

    private TransactionRecord createRecord(StoreId storeId, TransactionRecordType type, Long amount) {
        return TransactionRecord.builder()
                .storeId(storeId)
                .transactionType(type)
                .amount(BigDecimal.valueOf(amount))
                .transactionDate(LocalDate.now())
                .build();
    }
}