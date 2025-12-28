package com.example.tax.adapter.out.persistence.entity;

import com.example.tax.config.JpaConfiguration;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DataJpaTest
@Import(JpaConfiguration.class)
class UserStoreEntityTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("복합키를 가진 UserStoreEntity가 정상 저장 및 조회되어야 한다")
    void userStoreEntityPersistenceTest() {
        UserEntity user = new UserEntity();
        StoreEntity store = StoreEntity.builder().storeId("0987654321").build();

        em.persist(user);
        em.persist(store);

        log.info("user = {}", user);
        log.info("store = {}", store);

        UserStoreEntity userStore = UserStoreEntity.createAccess(user, store);
        em.persist(userStore);

        em.flush();
        em.clear();

        UserStoreId id = new UserStoreId(user.getSrl(), store.getSrl());
        UserStoreEntity found = em.find(UserStoreEntity.class, id);
        log.info("userStoreEntity: {}", found);

        assertThat(found).isNotNull();
        assertThat(found.getUser().getSrl()).isEqualTo(user.getSrl());
        assertThat(found.getStore().getStoreId()).isEqualTo("0987654321");

        UserEntity foundUser = em.find(UserEntity.class, user.getSrl());
        assertThat(foundUser.getAccessibleStores()).hasSize(1);
    }

    @Test
    @DisplayName("동일한 유저-상점 조합을 저장하면 제약조건 위반이 발생해야 한다")
    void duplicateAssignmentFailTest() {
        UserEntity user = new UserEntity();
        StoreEntity store = StoreEntity.builder().storeId("0123456789").build();
        em.persist(user);
        em.persist(store);

        UserStoreEntity first = UserStoreEntity.createAccess(user, store);
        em.persist(first);
        em.flush();

        UserStoreEntity second = UserStoreEntity.createAccess(user, store);
        assertThatThrownBy(() -> {
            em.persist(second);
            em.flush();
        }).isInstanceOf(RuntimeException.class);
    }
}