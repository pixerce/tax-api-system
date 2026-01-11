package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public interface TransactionRecordPort {
    void saveAll(List<TransactionRecord> record);

    BigDecimal sumSalesAmountByMonth(StoreId storeId, YearMonth month);
    BigDecimal sumPurchaseAmountByMonth(StoreId storeId, YearMonth month);
}