package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "user_info", schema = "tax")
public class UserEntity extends AbstractBaseEntity {
    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "user")
    private List<UserStoreEntity> accessibleStores = new ArrayList<>();

    public void addStoreAccess(StoreEntity store) {
        UserStoreEntity userStore = new UserStoreEntity();
        userStore.setUser(this);
        userStore.setStore(store);

        this.accessibleStores.add(userStore);
        store.getAssignedUsers().add(userStore);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "srl=" + srl +
                ", userName='" + userName +
                '}';
    }
}
