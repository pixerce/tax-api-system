package com.example.tax.adapter.out.file;

import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExcelReaderAdapterTest {

    private ExcelReaderAdapter excelReaderAdapter;

    @Mock
    private ExcelProperties excelProperties;
    @Mock
    private Consumer<List<TransactionRecord>> recordBatchConsumer;

    @BeforeEach
    void setUp() {
        excelReaderAdapter = new ExcelReaderAdapter(excelProperties);
    }

    @Test
    @DisplayName("정상 엑셀 파일을 읽으면 Consumer가 데이터를 전달받는다")
    void readSuccessTest() {
        given(excelProperties.getLocation()).willReturn(new ClassPathResource("sample/excel/sample.xlsx"));
        var storeId = StoreId.of("1234567890");

        excelReaderAdapter.readData(storeId, recordBatchConsumer);

        ArgumentCaptor<List<TransactionRecord>> captor = ArgumentCaptor.forClass(List.class);
        verify(recordBatchConsumer, atLeastOnce()).accept(captor.capture());

        List<TransactionRecord> firstBatch = captor.getValue();
        assertThat(firstBatch).isNotEmpty();
        assertThat(firstBatch.get(0).getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("파일을 찾을 수 없는 경우 ExcelFileStatusException 발생")
    void fileNotFoundTest() {
        given(excelProperties.getLocation()).willReturn(new ClassPathResource("invalid/path.xlsx"));
        var storeId = StoreId.of("1234567890");

        assertThatThrownBy(() -> excelReaderAdapter.readData(storeId, recordBatchConsumer))
                .isInstanceOf(InvalidStateException.class)
                .hasMessageContaining("엑셀 파일을 읽을 수 없습니다.");
    }
}