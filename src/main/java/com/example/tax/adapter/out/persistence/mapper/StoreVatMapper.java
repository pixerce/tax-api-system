package com.example.tax.adapter.out.persistence.mapper;

import com.example.tax.adapter.out.persistence.entity.StoreVatEntity;
import com.example.tax.domain.valueobject.Money;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StoreVatMapper {

    public StoreVatEntity toEntity(final StoreVat storeVat) {
        return StoreVatEntity.builder()
                .sales(storeVat.getSales().getAmount().longValue())
                .targetYearMonth(storeVat.getTargetYearMonth())
                .purchase(storeVat.getPurchase().getAmount().longValue())
                .vat(storeVat.getVat().getAmount().longValue())
                .id(storeVat.getId())
                .build();
    }

    public StoreVat toStoreVat(final StoreVatEntity storeVatEntity) {
        return StoreVat.builder()
                .storeId(StoreId.of(storeVatEntity.getStore().getStoreId()))
                .vat(new Money(BigDecimal.valueOf(storeVatEntity.getVat())))
                .sales(new Money(BigDecimal.valueOf(storeVatEntity.getSales())))
                .purchase(new Money(BigDecimal.valueOf(storeVatEntity.getPurchase())))
                .targetYearMonth(storeVatEntity.getTargetYearMonth())
                .id(storeVatEntity.getId())
                .build();
    }
}
