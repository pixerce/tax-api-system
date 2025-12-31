package com.example.tax.application.service;

import com.example.tax.application.usecase.GetVatUseCase;
import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import com.example.tax.domain.exception.VatDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetVatService implements GetVatUseCase {

    private final StoreVatPort storeVatPort;

    @Override
    public StoreVat getVat(String storeId, YearMonth yearMonth) {
        return Optional.ofNullable(storeVatPort.findByStoreIdAndYearMonth(StoreId.of(storeId), yearMonth))
                .orElseThrow(() -> new VatDataNotFoundException(storeId, yearMonth));
    }
}
