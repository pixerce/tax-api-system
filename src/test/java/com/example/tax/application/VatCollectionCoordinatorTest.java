package com.example.tax.application;

import com.example.tax.application.dto.DataCollectionRequest;
import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.application.mapper.DataCollectionMapper;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VatCollectionCoordinatorTest {

    @InjectMocks
    private VatCollectionCoordinator vatCollectionCoordinator;
    @Mock
    private DataCollectionMapper dataCollectionMapper;
    @Mock
    private VatDataProcessor vatDataProcessor;
    @Mock
    private CollectionTaskPort collectionTaskPort;

    @Test
    @DisplayName("사업자 번호가 10자리 미만인 경우 IllegalArgumentException 발생")
    void validationTest() {
        var request = new DataCollectionRequest("123", YearMonth.now());

        assertThatThrownBy(() -> vatCollectionCoordinator.requestDataProcess(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사업자 번호는 숫자 10자리여야 합니다.");
    }

    @Test
    @DisplayName("기존 태스크가 존재하면 COLLECTING, COLLECTED, FAILED 중 하나의 상태를 반환")
    void existingTaskStatusTest() {
        var storeId = "1234567890";
        var request = new DataCollectionRequest(storeId, YearMonth.now());
        var existingTask = CollectionTask.builder().status(TaskStatus.COLLECTING).build();

        given(collectionTaskPort.findLastestTaskByStoreId(any(), any())).willReturn(Optional.of(existingTask));

        var mockResponse = DataCollectionResponse.createCollectingResponse(StoreId.of(storeId), YearMonth.now());
        given(dataCollectionMapper.toDataCollectionResponse(existingTask)).willReturn(mockResponse);

        var response = vatCollectionCoordinator.requestDataProcess(request);

        assertThat(response.getStatus()).isIn(Set.of(TaskStatus.COLLECTING, TaskStatus.COLLECTED, TaskStatus.FAILED));
        verifyNoInteractions(vatDataProcessor);
    }

    @Test
    @DisplayName("기존 태스크가 없는 경우 새로운 수집 유스케이스 호출")
    void initiateNewCollectionTest() {
        var storeId = "1234567890";
        var targetMonth = YearMonth.of(2025, 12);
        var request = new DataCollectionRequest(storeId, targetMonth);

        given(collectionTaskPort.findLastestTaskByStoreId(any(), any())).willReturn(Optional.empty());

        vatCollectionCoordinator.requestDataProcess(request);

        verify(vatDataProcessor, times(1))
                .collectDataAndCalculateVat(argThat(id -> id.getId().equals(storeId)), eq(targetMonth));
    }
}