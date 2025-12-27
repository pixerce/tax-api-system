package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.adapter.out.persistence.entity.TransactionRecordEntity;
import com.example.tax.domain.valueobject.TransactionRecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecordEntity, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionRecordEntity t " +
            "WHERE t.storeId = :storeId " +
            "AND t.transactionType = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    Long sumAmountByStoreIdAndTransactionTypeAndTransactionDateBetween(
            @Param("storeId") String storeId,
            @Param("type") TransactionRecordType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
