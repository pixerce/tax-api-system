package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@IdClass(UserStoreId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_store", schema = "tax")
public class UserStoreEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_srl")
    private UserEntity user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStoreEntity that)) return false;

        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getStore(), that.getStore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getStore());
    }

    @Override
    public String toString() {
        return "UserStoreEntity{" +
                "userSrl=" + (user != null ? user.getSrl() : null) +
                ", storeSrl=" + (store != null ? store.getSrl() : null) +
                ", createdAt=" + createdAt +
                '}';
    }
}
