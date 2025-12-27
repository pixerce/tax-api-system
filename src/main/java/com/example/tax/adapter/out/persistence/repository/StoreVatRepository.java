package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.adapter.out.persistence.entity.StoreVatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

/**
 * storeId와 targetYearMonth를 복합키로 사용
 */
public interface StoreVatRepository extends JpaRepository<StoreVatEntity, Long> {

    @Query("SELECT sv FROM StoreVatEntity sv JOIN FETCH sv.store s WHERE sv.store.storeId = :storeId AND sv.targetYearMonth = :targetYearMonth")
    Optional<StoreVatEntity> findByStoreIdAndTargetYearMonth(@Param("storeId") String storeId,
                                                             @Param("targetYearMonth") YearMonth targetYearMonth);
}
