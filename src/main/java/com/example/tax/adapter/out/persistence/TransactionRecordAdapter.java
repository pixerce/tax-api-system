package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.repository.JdbcTransactionRepository;
import com.example.tax.adapter.out.persistence.repository.TransactionRecordRepository;
import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import com.example.tax.domain.valueobject.TransactionRecordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TransactionRecordAdapter implements TransactionRecordPort {

    private final TransactionRecordRepository transactionRecordRepository;
    private final JdbcTransactionRepository jdbcTransactionRepository;

    @Override
    public void saveAll(final List<TransactionRecord> recordList) {
        if (recordList == null || recordList.isEmpty())
            return;

        jdbcTransactionRepository.saveAll(recordList);
    }

    @Override
    public void flush() {
        transactionRecordRepository.flush();
    }

    @Override
    public BigDecimal sumSalesAmountByMonth(final StoreId storeId, final YearMonth targetYearMonth) {
        LocalDate start = targetYearMonth.atDay(1);
        LocalDate end = targetYearMonth.atEndOfMonth();
        Long sum = this.transactionRecordRepository.sumAmountByStoreIdAndTransactionTypeAndTransactionDateBetween(
                storeId.getId(), TransactionRecordType.SALE, start, end);
        return BigDecimal.valueOf(sum);
    }

    @Override
    public BigDecimal sumPurchaseAmountByMonth(final StoreId storeId, final YearMonth targetYearMonth) {
        LocalDate start = targetYearMonth.atDay(1);
        LocalDate end = targetYearMonth.atEndOfMonth();
        Long sum = this.transactionRecordRepository.sumAmountByStoreIdAndTransactionTypeAndTransactionDateBetween(
                storeId.getId(), TransactionRecordType.PURCHASE, start, end);
        return BigDecimal.valueOf(sum);
    }
}
