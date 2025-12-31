package com.example.tax.application.service;

import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.application.usecase.CalculateVatUseCase;
import com.example.tax.domain.valueobject.Money;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.StoreVat;
import com.example.tax.domain.valueobject.VatRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class CalculateVatService implements CalculateVatUseCase {

    private final TransactionRecordPort transactionRecordPort;
    private final StoreVatPort storeVatPort;

    @Override
    public void calculateVat(StoreId storeId, YearMonth yearMonth) {
        var sales = transactionRecordPort.sumSalesAmountByMonth(storeId, yearMonth);
        var purchases = transactionRecordPort.sumPurchaseAmountByMonth(storeId, yearMonth);

        var storeVat = StoreVat.builder()
                .storeId(storeId)
                .targetYearMonth(yearMonth)
                .sales(new Money(sales))
                .purchase(new Money(purchases))
                .vatRate(new VatRate(storeId, yearMonth, new BigDecimal("0.10")))
                .build();

        storeVat.calculateVat();
        storeVatPort.save(storeVat);
    }
}
