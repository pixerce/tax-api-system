package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_store", schema = "tax")
public class UserStoreEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_srl")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_srl")
    private StoreEntity store;

    public static UserStoreEntity createAccess(UserEntity user, StoreEntity store) {
        UserStoreEntity userStore = new UserStoreEntity();
        userStore.setUser(user);
        userStore.setStore(store);

        user.getAccessibleStores().add(userStore);
        store.getAssignedUsers().add(userStore);

        return userStore;
    }
}
