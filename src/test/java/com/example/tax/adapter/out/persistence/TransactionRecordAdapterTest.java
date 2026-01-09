package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.TransactionRecordEntity;
import com.example.tax.adapter.out.persistence.mapper.TransactionRecordMapper;
import com.example.tax.adapter.out.persistence.repository.JdbcTransactionRepository;
import com.example.tax.adapter.out.persistence.repository.TransactionRecordRepository;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TransactionRecord;
import com.example.tax.domain.valueobject.TransactionRecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TransactionRecordAdapter.class, TransactionRecordMapper.class, JdbcTransactionRepository.class})
class TransactionRecordAdapterTest {

    @Autowired
    private TransactionRecordAdapter transactionRecordAdapter;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    private final StoreId storeId = StoreId.of("1234567890");
    private final YearMonth targetMonth = YearMonth.of(2025, 12);

    @Test
    @DisplayName("saveAll은 여러 개의 거래 기록을 한 번에 저장한다")
    void testSaveAll() {
        List<TransactionRecord> records = List.of(
                createDomainRecord(10000L, TransactionRecordType.SALE, LocalDate.of(2025, 12, 1)),
                createDomainRecord(5000L, TransactionRecordType.SALE, LocalDate.of(2025, 12, 2))
        );

        transactionRecordAdapter.saveAll(records);
        assertThat(transactionRecordRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("sumSalesAmountByMonth는 해당 월의 매출 합계만 계산한다")
    void testSumSalesAmountByMonth() {
        transactionRecordRepository.saveAll(List.of(
                createEntity(10000L, TransactionRecordType.SALE, LocalDate.of(2025, 12, 5)),
                createEntity(20000L, TransactionRecordType.SALE, LocalDate.of(2025, 12, 20)),
                createEntity(5000L, TransactionRecordType.PURCHASE, LocalDate.of(2025, 12, 10)), // 매입 제외
                createEntity(15000L, TransactionRecordType.SALE, LocalDate.of(2025, 11, 30))  // 이전 달 제외
        ));

        BigDecimal totalSales = transactionRecordAdapter.sumSalesAmountByMonth(storeId, targetMonth);
        assertThat(totalSales).isEqualByComparingTo(BigDecimal.valueOf(30000L));
    }

    @Test
    @DisplayName("sumPurchaseAmountByMonth는 해당 월의 매입 합계만 계산한다")
    void testSumPurchaseAmountByMonth() {
        transactionRecordRepository.saveAll(List.of(
                createEntity(7000L, TransactionRecordType.PURCHASE, LocalDate.of(2025, 12, 1)),
                createEntity(3000L, TransactionRecordType.PURCHASE, LocalDate.of(2025, 12, 31)),
                createEntity(10000L, TransactionRecordType.SALE, LocalDate.of(2025, 12, 15))
        ));

        BigDecimal totalPurchase = transactionRecordAdapter.sumPurchaseAmountByMonth(storeId, targetMonth);
        assertThat(totalPurchase).isEqualByComparingTo(BigDecimal.valueOf(10000L));
    }

    private TransactionRecord createDomainRecord(Long amount, TransactionRecordType type, LocalDate date) {
        return TransactionRecord.builder()
                .storeId(storeId)
                .amount(BigDecimal.valueOf(amount))
                .transactionType(type)
                .transactionDate(date)
                .build();
    }

    private TransactionRecordEntity createEntity(Long amount, TransactionRecordType type, LocalDate date) {
        return TransactionRecordEntity.builder()
                .storeId(storeId.getId())
                .amount(amount)
                .transactionType(type)
                .transactionDate(date)
                .build();
    }
}