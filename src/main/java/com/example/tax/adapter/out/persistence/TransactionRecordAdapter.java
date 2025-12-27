package com.example.tax.adapter.out.persistence;

import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TransactionRecordAdapter implements TransactionRecordPort {

    @Override
    public void saveAll(final List<TransactionRecord> record) {
    }

    @Override
    public void flush() {
    }

    @Override
    public BigDecimal sumSalesAmountByMonth(final StoreId storeId, final YearMonth month) {
        return null;
    }

    @Override
    public BigDecimal sumPurchaseAmountByMonth(final StoreId storeId, final YearMonth month) {
        return null;
    }
}
