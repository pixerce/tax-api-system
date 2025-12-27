package com.example.tax.adapter.out.persistence.entity;

import com.example.tax.adapter.out.persistence.converter.YearMonthStringConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_vat", schema = "tax")
public class StoreVatEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private StoreEntity store;

    @Column(name="vat")
    private Long vat;

    @Column(name="sales")
    private Long sales;

    @Column(name="purchase")
    private Long purchase;

    @Column(name="calculated_at")
    private LocalDateTime calculatedAt;

    @Convert(converter = YearMonthStringConverter.class)
    @Column(name="target_year_month")
    private YearMonth targetYearMonth;

    public void assignStore(StoreEntity store) {
        this.store = store;
    }

    public void assignId(final Long id) {
        this.srl = id;
    }

    @Builder
    private StoreVatEntity(final Long id, final StoreEntity store, final Long vat, final Long sales, final Long purchase
            , final LocalDateTime calculatedAt, final YearMonth targetYearMonth) {
        this.srl = id;
        this.store = store;
        this.vat = vat;
        this.sales = sales;
        this.purchase = purchase;
        this.calculatedAt = calculatedAt;
        this.targetYearMonth = targetYearMonth;
    }

    @Override
    public String toString() {
        return "StoreVatEntity{" +
                "store=" + store.getSrl() +
                ", vat=" + vat +
                ", sales=" + sales +
                ", purchase=" + purchase +
                ", calculatedAt=" + calculatedAt +
                ", targetYearMonth=" + targetYearMonth +
                '}';
    }
}
