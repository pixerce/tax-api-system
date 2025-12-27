package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.entity.StoreVatEntity;
import com.example.tax.adapter.out.persistence.mapper.StoreVatMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.adapter.out.persistence.repository.StoreVatRepository;
import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StoreVatAdapter implements StoreVatPort {
    private final StoreVatRepository storeVatRepository;
    private final StoreRepository storeRepository;
    private final StoreVatMapper storeVatMapper;

    @Override
    public void save(final StoreVat storeVat) {
        Optional<StoreVatEntity> storeVatEntityOptional
                = this.storeVatRepository.findByStoreIdAndTargetYearMonth(storeVat.getStoreId().getId()
                , storeVat.getTargetYearMonth());

        StoreVatEntity storeVatEntity = this.storeVatMapper.toEntity(storeVat);

        final String storeId = storeVat.getStoreId().getId();
        StoreEntity storeEntity = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new InvalidStateException("상점 데이터 없음 storeId: " + storeId));

        storeVatEntity.assignStore(storeEntity);

        storeVatEntityOptional.ifPresent(entity -> storeVatEntity.assignId(entity.getId()));

        this.storeVatRepository.save(storeVatEntity);
    }

    @Override
    public StoreVat findByStoreIdAndYearMonth(StoreId storeId, YearMonth yearMonth) {

        Optional<StoreVatEntity> storeVatEntityOptional
                = this.storeVatRepository.findByStoreIdAndTargetYearMonth(storeId.getId()
                , yearMonth);
        return storeVatMapper.toStoreVat(storeVatEntityOptional.orElseThrow(
                () -> new InvalidStateException(String.format("부가세 정보 없음 storeId: %s, yearMonth: %s"
                , storeId.getId(), yearMonth.toString()))));
    }
}
