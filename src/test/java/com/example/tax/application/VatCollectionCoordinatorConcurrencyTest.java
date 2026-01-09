package com.example.tax.application;

import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.out.persistence.repository.CollectionTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.YearMonth;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest // 실제 Spring Context 로드 (DB 연결)
class VatCollectionCoordinatorConcurrencyTest {

    @Autowired
    private VatCollectionCoordinator coordinator;

    @Autowired
    private CollectionTaskRepository collectionTaskRepository;

    @MockitoBean
    private VatDataProcessor vatDataProcessor;

    @BeforeEach
    void setUp() {
        collectionTaskRepository.deleteAll();
    }

    @Test
    @DisplayName("10개 스레드가 동시에 요청해도 수집 프로세스는 1번만 실행되어야 한다")
    void concurrent_request_handling() throws InterruptedException {

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        String storeId = "0123456789";
        YearMonth yearMonth = YearMonth.now();
        DataCollectionRequest request = new DataCollectionRequest(storeId, yearMonth);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    coordinator.requestDataProcess(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            });
        }

        latch.countDown();
        Thread.sleep(2000);

        long dbCount = collectionTaskRepository.count();
        assertThat(dbCount).isEqualTo(1);

        verify(vatDataProcessor, times(1)).collectDataAndCalculateVat(any());

        assertThat(successCount.get()).isEqualTo(threadCount);
    }
}