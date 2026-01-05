package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.domain.valueobject.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<TransactionRecord> transactions) {
        log.info("Saving transactions: {}", transactions);
        String sql = "INSERT INTO tax.transaction_record (transaction_type, amount, store_id, transaction_date, created_at) VALUES (?, ?, ?, ?, now())";

        jdbcTemplate.batchUpdate(sql,
                transactions,
                100,
                (PreparedStatement ps, TransactionRecord transaction) -> {
                    ps.setString(1, transaction.getTransactionType().name());
                    ps.setBigDecimal(2, transaction.getAmount());
                    ps.setString(3, transaction.getStoreId().getId());
                    ps.setObject(4, transaction.getTransactionDate());
                });
    }
}