package com.example.tax.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;

@Builder
@AllArgsConstructor
@Getter
public class StoreVat {

    private Long id;
    private StoreId storeId;

    private Money sales;
    private Money purchase;
    private Money vat;

    private YearMonth targetYearMonth;

    private VatRate vatRate;

    public void assignId(Long id) {
        this.id = id;
    }

    public void calculateVat() {
        Money profit = this.sales.subtract(this.purchase);
        this.vat = profit.multiply(vatRate.getRate()).scale();
    }

}
