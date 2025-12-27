package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.CollectionTaskEntity;
import com.example.tax.adapter.out.persistence.mapper.CollectionTaskMapper;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CollectionTaskAdapter.class, CollectionTaskMapper.class})
//@ActiveProfiles("test") // 테스트용 설정(h2 등) 사용 시
class CollectionTaskAdapterTest {

    @Autowired
    private CollectionTaskAdapter collectionTaskAdapter;

    @Autowired
    private CollectionTaskRepository collectionTaskRepository;

    private final StoreId storeId = new StoreId("0123456789");
    private final YearMonth targetMonth = YearMonth.of(2025, 12);

    @Test
    @DisplayName("save 메서드는 도메인 모델을 엔티티로 변환하여 저장하고 ID를 할당한다")
    void testSaveAndIdAssigned() {
        CollectionTask collectionTask = CollectionTask.create(storeId, targetMonth);
        collectionTask.started();

        collectionTaskAdapter.save(collectionTask);

        assertThat(collectionTask.getId()).isNotNull();

        CollectionTaskEntity savedEntity = collectionTaskRepository.findById(collectionTask.getId()).orElseThrow();
        assertThat(savedEntity.getStoreId()).isEqualTo(storeId);
        assertThat(savedEntity.getStatus()).isEqualTo(TaskStatus.COLLECTING);
    }

    @Test
    @DisplayName("findLastestTaskByStoreId는 가장 최근에 시작된 작업을 조회한다")
    void testFindLastestTaskByStoreId() {

        LocalDateTime earlier = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime later = LocalDateTime.of(2025, 12, 1, 11, 0);

        saveEntity(earlier);
        saveEntity(later);

        Optional<CollectionTask> result = collectionTaskAdapter.findLastestTaskByStoreId(storeId, targetMonth);

        assertThat(result).isPresent();
        assertThat(result.get().getStartedAt()).isEqualTo(later);
    }

    private void saveEntity(LocalDateTime startedAt) {
        collectionTaskRepository.save(CollectionTaskEntity.builder()
                .storeId(storeId)
                .targetYearMonth(targetMonth)
                .status(TaskStatus.COLLECTING)
                .startedAt(startedAt)
                .build());
    }
}