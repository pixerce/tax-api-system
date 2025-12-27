package com.example.tax.application;

import com.example.tax.adapter.in.event.DataProcessingEventAdapter;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.service.DataCollectionProcessor;
import com.example.tax.application.service.DataCollectionProcessorFactory;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.valueobject.StoreId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.time.Duration;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RecordApplicationEvents
public class VatDataProcessingIntegrationTest {

    @Autowired
    private VatDataProcessor vatDataProcessor;

    @Autowired
    private ApplicationEvents events;

    @MockitoSpyBean
    private DataProcessingEventAdapter eventAdapter;

    @MockitoBean
    private DataCollectionProcessorFactory factory;

    @MockitoBean
    private DataCollectionProcessor processor;

    @MockitoBean
    private CollectionTaskPort collectionTaskPort;

    private final StoreId storeId = StoreId.of("0123456789");
    private final YearMonth targetMonth = YearMonth.now();

    /**
     * 이벤트 발행 여부를 테스트 하는 다른 테스트 코드와 같이 실행하는 경우 실패하는 현상이 발생하여 스트림에 발행한 이벤트 수를 검증하는 대신
     * 실제 이벤트 핸들러의 호출 여부로 테스트 코드를 변경함
     * ApplicationEvents 를 같이 사용해서 발생하는 것으로 추정하고 있으며 테스트 시작하기 전에 ApplicationEvents를 초기화 해도 해결이 안된다.
     */
    @Test
    @DisplayName("성공 시 Completed 이벤트를 발행하고 두 핸들러가 호출된다")
    void testSuccessEventFlow() {
        given(factory.createDataCollectorTask(any(), any())).willReturn(processor);
        given(processor.process()).willReturn(null);

        vatDataProcessor.collectDataAndCalculateVat(storeId, targetMonth);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(eventAdapter, times(1)).handleCompletedEvent(any());
            verify(eventAdapter, times(1)).handleCompletedEventAndCalculate(any());
        });
    }

    @Test
    @DisplayName("실패 시 Failed 이벤트를 발행하고 실패 핸들러가 호출된다")
    void testFailureEventFlow() {
        given(factory.createDataCollectorTask(any(), any())).willReturn(processor);
        given(processor.process()).willThrow(new RuntimeException("Processing Error"));

        vatDataProcessor.collectDataAndCalculateVat(storeId, targetMonth);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            long eventCount = events.stream(DataProcessingFailedEvent.class).count();
            assertThat(eventCount).isEqualTo(1);

            verify(eventAdapter, times(1)).handleFailedEvent(any());
            verify(eventAdapter, times(1)).handleStartedEvent(any());
        });
    }
}
