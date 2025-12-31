package com.example.tax.application;

import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.port.out.DataProcessingEventPort;
import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.DataCollectionProcessorFactory;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.time.YearMonth;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class VatDataProcessingIntegrationTest {

    @Autowired
    private VatDataProcessor vatDataProcessor;

    @SpyBean
    private DataProcessingEventPort dataProcessingEventPort;

    @MockBean
    private DataCollectionProcessorFactory factory;

    @MockBean
    private DataCollectionProcessor processor;

    @MockBean
    private CollectionTaskPort collectionTaskPort;

    private final StoreId storeId = StoreId.of("0123456789");
    private final YearMonth targetMonth = YearMonth.now();

    @Test
    @DisplayName("성공 시 Completed 이벤트를 발행하고 두 핸들러가 호출된다")
    void testSuccessEventFlow() {
        given(factory.createDataCollectorTask(any(), any())).willReturn(processor);
        given(processor.process()).willReturn(null);
        given(collectionTaskPort.findLastestTaskByStoreId(storeId.getId(), targetMonth))
                .willReturn(Optional.of(CollectionTask.create(storeId, targetMonth)));

        vatDataProcessor.collectDataAndCalculateVat(storeId, targetMonth);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(dataProcessingEventPort, times(1)).publish(any(DataProcessingStartedEvent.class));
            verify(dataProcessingEventPort, times(1)).publish(any(DataProcessingCompletedEvent.class));
        });
    }

    @Test
    @DisplayName("실패 시 Failed 이벤트를 발행하고 실패 핸들러가 호출된다")
    void testFailureEventFlow() {
        given(factory.createDataCollectorTask(any(), any())).willReturn(processor);
        given(processor.process()).willThrow(new RuntimeException("Processing Error"));
        given(collectionTaskPort.findLastestTaskByStoreId(storeId.getId(), targetMonth))
                .willReturn(Optional.of(CollectionTask.create(storeId, targetMonth)));

        vatDataProcessor.collectDataAndCalculateVat(storeId, targetMonth);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(dataProcessingEventPort, times(1)).publish(any(DataProcessingStartedEvent.class));
            verify(dataProcessingEventPort, times(1)).publish(any(DataProcessingFailedEvent.class));
        });
    }
}
