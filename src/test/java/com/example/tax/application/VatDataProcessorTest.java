package com.example.tax.application;

import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.application.service.*;
import com.example.tax.domain.valueobject.StoreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.YearMonth;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VatDataProcessorTest {

    private VatDataProcessor vatDataProcessor;

    @Mock
    private DataSourceReaderPort dataSourceReaderPort;
    @Mock
    private TransactionRecordPort transactionRecordPort;
    @Mock
    private DataCollectionProcessorFactory dataCollectionProcessorFactory;
    @Mock
    private CollectionTaskHandler collectionTaskHandler;

    private final Executor executorService = Executors.newFixedThreadPool(1);

    @BeforeEach
    void setUp() {
        vatDataProcessor = new VatDataProcessor(executorService, dataCollectionProcessorFactory);
    }

    @Test
    @DisplayName("정상 수행 시 데이터 읽기, 저장, 플러시 후 상태가 COLLECTED로 변경된다")
    void successScenarioTest() {
        var storeId = StoreId.of("1234567890");
        var targetMonth = YearMonth.now();

        DataCollectionProcessor task = new ExecutionTimeGuarantorDecorator(ExcelDataCollectionProcessor.builder()
                .dataSourceReaderPort(dataSourceReaderPort)
                .transactionRecordPort(transactionRecordPort)
                .storeId(storeId)
                .collectionTaskHandler(collectionTaskHandler)
                .build(), 1000L);

        BDDMockito.given(dataCollectionProcessorFactory.createDataCollectorTask(any(), any())).willReturn(task);
        vatDataProcessor.collectDataAndCalculateVat(storeId, targetMonth);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(collectionTaskHandler).start();

            verify(dataSourceReaderPort).readData(eq(storeId), any());
            verify(transactionRecordPort).flush();

            verify(collectionTaskHandler).finish();
        });
    }

    @Test
    @DisplayName("perform 실행 중 예외 발생 시 상태가 FAILED로 변경된다")
    void failureScenarioTest() {
        var storeId = StoreId.of("1234567890");

        doThrow(new RuntimeException("파일 읽기 실패"))
                .when(dataSourceReaderPort).readData(any(), any());

        DataCollectionProcessor task = new ExecutionTimeGuarantorDecorator(ExcelDataCollectionProcessor.builder()
                .dataSourceReaderPort(dataSourceReaderPort)
                .transactionRecordPort(transactionRecordPort)
                .storeId(storeId)
                .collectionTaskHandler(collectionTaskHandler)
                .build(), 1000L);

        BDDMockito.given(dataCollectionProcessorFactory.createDataCollectorTask(any(), any())).willReturn(task);

        vatDataProcessor.collectDataAndCalculateVat(storeId, YearMonth.now());

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(collectionTaskHandler).start();
            verify(collectionTaskHandler).fail();

            verify(collectionTaskHandler, never()).finish();
        });
    }
}