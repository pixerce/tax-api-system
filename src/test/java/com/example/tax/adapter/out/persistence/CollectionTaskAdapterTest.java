package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.adapter.out.persistence.mapper.CollectionTaskMapper;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import com.example.tax.config.JpaConfiguration;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import({CollectionTaskAdapter.class, CollectionTaskMapper.class, JpaConfiguration.class})
class CollectionTaskAdapterTest {

    @Autowired
    private CollectionTaskAdapter collectionTaskAdapter;

    @Autowired
    private CollectionTaskRepository collectionTaskRepository;

    private final StoreId storeId = StoreId.of("0123456789");
    private final YearMonth targetMonth = YearMonth.of(2025, 12);

    @Test
    @Transactional
    @DisplayName("save 메서드는 도메인 모델을 엔티티로 변환하여 저장하고 ID를 할당한다")
    void testUpsertAndIdAssigned() {

        CollectionTaskEntity entity = CollectionTaskEntity.builder()
                .targetYearMonth(targetMonth)
                .storeId(storeId.getId())
                .status(TaskStatus.COLLECTING)
                .build();

        collectionTaskRepository.saveAndFlush(entity);
        CollectionTask collectionTask = CollectionTask.create(storeId, targetMonth);
        collectionTask.assignId(entity.getSrl());
        collectionTask.started();

        collectionTaskAdapter.upsert(collectionTask);

        assertThat(collectionTask.getId()).isNotNull();

        CollectionTaskEntity savedEntity = collectionTaskRepository.findById(collectionTask.getId()).orElseThrow();
        assertThat(savedEntity.getStoreId()).isEqualTo(storeId.getId());
        assertThat(savedEntity.getStatus()).isEqualTo(TaskStatus.COLLECTING);
    }

    @Transactional
    @Test
    @DisplayName("findLastestTaskByStoreId는 가장 최근에 시작된 작업을 조회한다")
    void testFindLastestTaskByStoreId() {

        LocalDateTime earlier = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime later = LocalDateTime.of(2025, 12, 1, 11, 0);

        upsertEntity(earlier, targetMonth.minusMonths(2));
        upsertEntity(later, targetMonth);

        Optional<CollectionTask> result = collectionTaskAdapter.findLastestTaskByStoreId(storeId.getId(), targetMonth);

        assertThat(result).isPresent();
        assertThat(result.get().getStartedAt()).isAfter(later);
    }

    private void upsertEntity(final LocalDateTime startedAt, final YearMonth targetMonth) {
        CollectionTaskEntity entity = CollectionTaskEntity.builder()
                .storeId(storeId.getId())
                .targetYearMonth(targetMonth)
                .status(TaskStatus.COLLECTING)
                .startedAt(startedAt)
                .build();
        collectionTaskRepository.save(entity);
    }
}