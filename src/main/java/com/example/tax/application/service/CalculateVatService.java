package com.example.tax.application.service;

import com.example.tax.application.port.in.CalculateVatUseCase;
import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.application.port.out.VatRateSourcePort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.Money;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import com.example.tax.domain.valueobject.VatRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class CalculateVatService implements CalculateVatUseCase {

    private final TransactionRecordPort transactionRecordPort;
    private final StoreVatPort storeVatPort;
    private final VatRateSourcePort vatRateSourcePort;

    @Override
    public void calculateAndStore(final StoreId storeId, final YearMonth targetYearMonth) {

        final VatRate vatRate = this.vatRateSourcePort.findVatRate(storeId, targetYearMonth)
                .orElseThrow(()
                        -> new InvalidStateException(String.format("부가세 세율 상점: %s, %s 을 찾을 수 없습니다."
                        , storeId, targetYearMonth)));

        final StoreVat storeVat = StoreVat.builder()
                .sales(new Money(this.transactionRecordPort.sumSalesAmountByMonth(storeId, targetYearMonth)))
                .purchase(new Money(this.transactionRecordPort.sumPurchaseAmountByMonth(storeId, targetYearMonth)))
                .targetYearMonth(targetYearMonth)
                .vatRate(vatRate)
                .storeId(storeId)
                .build();

        storeVat.calculateVat();

        this.storeVatPort.save(storeVat);
    }

}