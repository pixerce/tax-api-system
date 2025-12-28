package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
        this.srl = id;
        this.storeId = storeId;
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStoreEntity> assignedUsers = new ArrayList<>();

    public void assignUser(UserEntity user) {
        UserStoreEntity userStore = new UserStoreEntity();
        userStore.setStore(this);
        userStore.setUser(user);

        this.assignedUsers.add(userStore);
        user.getAccessibleStores().add(userStore); // 상대방 객체에도 추가
    }
}
