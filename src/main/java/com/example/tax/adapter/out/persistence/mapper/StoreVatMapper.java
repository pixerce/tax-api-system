package com.example.tax.adapter.out.persistence.mapper;

import com.example.tax.adapter.out.persistence.entity.StoreVatEntity;
import com.example.tax.domain.valueobject.StoreVat;
import org.springframework.stereotype.Component;

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
}
