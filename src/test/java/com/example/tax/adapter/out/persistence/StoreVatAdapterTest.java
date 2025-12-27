package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.entity.StoreVatEntity;
import com.example.tax.adapter.out.persistence.mapper.StoreVatMapper;
import com.example.tax.adapter.out.persistence.repository.StoreVatRepository;
import com.example.tax.domain.valueobject.Money;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({StoreVatAdapter.class, StoreVatMapper.class})
class StoreVatAdapterTest {

    @Autowired
    private StoreVatAdapter storeVatAdapter;

    @Autowired
    private StoreVatRepository storeVatRepository;

    @Autowired
    private TestEntityManager entityManager;

    private StoreEntity storeEntity;
    private final StoreId storeId = StoreId.of("0123456789");
    private final YearMonth targetMonth = YearMonth.of(2025, 12);

    @BeforeEach
    void setUp() {
        // StoreVatEntity는 StoreEntity를 참조하므로 부모를 먼저 생성/저장해야 함
        storeEntity = StoreEntity.builder()
                .storeId(storeId.getId())
//                .name("테스트 상점")
                .build();
        entityManager.persist(storeEntity);
        entityManager.flush();
    }

    @Test
    @DisplayName("기존 데이터가 없는 경우 새로운 StoreVat 데이터를 저장한다 (Insert)")
    void testSaveNew() {
        // [Given]
        StoreVat storeVat = StoreVat.builder()
                .storeId(storeId)
                .targetYearMonth(targetMonth)
                .vat(new Money(BigDecimal.valueOf(1000L)))
                .sales(new Money(BigDecimal.valueOf(10000L)))
                .purchase(new Money(BigDecimal.valueOf(50000L)))
                .build();

        // [When]
        storeVatAdapter.save(storeVat);

        // [Then]
        Optional<StoreVatEntity> saved = storeVatRepository.findByStoreIdAndTargetYearMonth(storeId.getId(), targetMonth);
        assertThat(saved).isPresent();
        assertThat(saved.get().getVat()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("기존 데이터가 있는 경우 데이터를 업데이트한다 (Update)")
    void testSaveUpdate() {
        // [Given] 1. 기존 데이터 존재
        StoreVatEntity existing = StoreVatEntity.builder()
                .store(storeEntity)
                .targetYearMonth(targetMonth)
                .sales(10000L)
                .purchase(20000L)
                .vat(500L) // 초기값 500
                .build();
        storeVatRepository.save(existing);
        entityManager.flush();
        entityManager.clear();

        // 2. 새로운 도메인 객체 (동일한 storeId, targetYearMonth)
        StoreVat updateDomain = StoreVat.builder()
                .storeId(storeId)
                .targetYearMonth(targetMonth)
                .sales(new Money(BigDecimal.valueOf(10000L)))
                .purchase(new Money(BigDecimal.valueOf(20000L)))
                .vat(new Money(BigDecimal.valueOf(2000L))) // 변경할 값 2000
                .build();

        // [When]
        storeVatAdapter.save(updateDomain);

        // [Then]
        Optional<StoreVatEntity> result = storeVatRepository.findByStoreIdAndTargetYearMonth(storeId.getId(), targetMonth);
        assertThat(result).isPresent();
        assertThat(result.get().getVat()).isEqualTo(2000L); // 값이 업데이트 되었는지 확인

        // 데이터가 추가된 것이 아니라 기존 것이 변경되었는지 확인 (개수 유지)
        assertThat(storeVatRepository.count()).isEqualTo(1);
    }
}