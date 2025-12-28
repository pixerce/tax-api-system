package com.example.tax.adapter.out.persistence.repository;

import com.example.tax.adapter.out.persistence.entity.UserStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStoreRepository extends JpaRepository<UserStoreEntity, Long> {
    @Query("SELECT us FROM UserStoreEntity us JOIN FETCH us.store WHERE us.user.srl = :userSrl")
    List<UserStoreEntity> findAllByUserId(@Param("userSrl") Long userId);

    /**
     * 특정 사용자가 특정 사업장에 권한이 있는지 확인합니다. (MANAGER 권한 체크용)
     */
    @Query("SELECT COUNT(us) > 0 FROM UserStoreEntity us " +
            "WHERE us.user.srl = :userSrl " +
            "AND us.store.storeId = :storeId")
    Boolean existsByUserSrlAndStoreId(@Param("userSrl") Long userSrl, @Param("storeId") String storeId);

    /**
     * 권한 회수 시 사용: 특정 사용자와 사업장의 매핑 데이터를 삭제합니다.
     */
    void deleteByUserSrlAndStoreSrl(Long userSrl, Long storeSrl);

    @Query("SELECT us FROM UserStoreEntity us " +
            "JOIN FETCH us.store " +
            "WHERE us.user.srl = :userSrl")
    List<UserStoreEntity> findAllWithStoreByUserSrl(@Param("userSrl") Long userSrl);
}
