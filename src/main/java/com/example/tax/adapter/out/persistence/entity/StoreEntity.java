package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store", schema = "tax")
public class StoreEntity extends AbstractBaseEntity {

    @Column(name = "store_id")
    private String storeId;

    @Builder
    private StoreEntity(final Long id, final String storeId) {
        this.id = id;
        this.storeId = storeId;
    }
}
