package com.example.tax.adapter.out.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.tax.adapter.out.file.dto.ExcelRow;
import com.example.tax.application.port.out.DataSourceReaderPort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import com.example.tax.domain.valueobject.TransactionRecordType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelReaderAdapter implements DataSourceReaderPort {

    private final ExcelProperties excelProperties;
    /**
     * PageReadListener 는 100개 단위로 데이터를 읽기 때문에 파라미터로 받는 recordBatchConsumer에 100개의 데이터를 전달한다.
     */
    @Override
    public void readData(final StoreId storeId, final Consumer<List<TransactionRecord>> recordBatchConsumer) {
        Resource resource = this.excelProperties.getLocation();
        AtomicBoolean hasData = new AtomicBoolean(false);

        try (InputStream is = resource.getInputStream()) {
            ExcelReader excelReader = EasyExcel.read(is).build();

            ReadSheet revenueSheet = EasyExcel.readSheet("매출")
                    .head(ExcelRow.class)
                    .headRowNumber(0)
                    .registerReadListener(new PageReadListener<ExcelRow>(dataList -> {
                        if (!dataList.isEmpty()) {
                            hasData.set(true);
                            List<TransactionRecord> batch = dataList.stream()
                                    .map(row -> new TransactionRecord(TransactionRecordType.SALE
                                            , row.getAmount(), row.getDate(), storeId))
                                    .toList();
                            recordBatchConsumer.accept(batch);
                        }
                    })).build();

            ReadSheet expenseSheet = EasyExcel.readSheet("매입")
                    .head(ExcelRow.class)
                    .headRowNumber(0)
                    .registerReadListener(new PageReadListener<ExcelRow>(dataList -> {
                        if (!dataList.isEmpty()) {
                            hasData.set(true);
                            List<TransactionRecord> batch = dataList.stream()
                                    .map(row -> new TransactionRecord(TransactionRecordType.PURCHASE
                                            , row.getAmount(), row.getDate(), storeId))
                                    .toList();
                            recordBatchConsumer.accept(batch);
                        }
                    })).build();

            excelReader.read(revenueSheet, expenseSheet);
            excelReader.finish();

            if (!hasData.get()) {
                throw new InvalidStateException("엑셀 파일에 처리할 데이터가 없습니다.");
            }
        } catch (IOException e) {
            log.error("엑셀 리소스 파일을 읽는 중 오류 발생. 경로: {}", resource.getDescription(), e);
            throw new InvalidStateException("엑셀 파일을 읽을 수 없습니다.", e);
        }
    }
}