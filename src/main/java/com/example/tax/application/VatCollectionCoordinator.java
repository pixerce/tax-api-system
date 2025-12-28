package com.example.tax.application;

import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.adapter.in.web.dto.VatResultResponse;
import com.example.tax.application.mapper.DataCollectionMapper;
import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import com.example.tax.domain.valueobject.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class VatCollectionCoordinator {

    private final DataCollectionMapper dataCollectionMapper;
    private final VatDataProcessor vatDataProcessor;
    private final CollectionTaskPort collectionTaskPort;
    private final StoreVatPort storeVatPort;

    public VatResultResponse getVat(final String storeIdStr,  final YearMonth yearMonth) {
        final StoreId storeId = StoreId.of(storeIdStr);

        final StoreVat storeVat = storeVatPort.findByStoreIdAndYearMonth(storeId, yearMonth);
        return VatResultResponse.builder()
                .vat(storeVat.getVat().getAmount().longValue())
                .storeId(storeVat.getStoreId())
                .yearMonth(storeVat.getTargetYearMonth())
                .build();
    }

    public DataCollectionResponse getState(final String storeIdStr,  final YearMonth yearMonth) {
        final StoreId storeId = StoreId.of(storeIdStr);
        return collectionTaskPort.findLastestTaskByStoreId(storeId.getId(), yearMonth)
                .map(dataCollectionMapper::toDataCollectionResponse)
                .orElseGet(() -> buildNotRequestedResponse(storeId, yearMonth));
    }

    private DataCollectionResponse buildNotRequestedResponse(final StoreId storeId,  final YearMonth yearMonth) {
        return DataCollectionResponse.builder()
                .status(TaskStatus.NOT_REQUESTED)
                .storeId(storeId)
                .yearMonth(yearMonth)
                .build();
    }

    public DataCollectionResponse requestDataProcess(final DataCollectionRequest dataCollectionRequest) {
        return collectionTaskPort.findLastestTaskByStoreId(
                        dataCollectionRequest.getStoreId(),
                        dataCollectionRequest.getTargetYearMonth())
                .map(dataCollectionMapper::toDataCollectionResponse)
                .orElseGet(() -> vatDataProcessor.collectDataAndCalculateVat(
                        StoreId.of(dataCollectionRequest.getStoreId()), dataCollectionRequest.getTargetYearMonth()));
    }


}
