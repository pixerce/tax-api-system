package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.entity.UserEntity;
import com.example.tax.adapter.out.persistence.entity.UserStoreEntity;
import com.example.tax.adapter.out.persistence.mapper.StoreMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.adapter.out.persistence.repository.UserRepository;
import com.example.tax.adapter.out.persistence.repository.UserStoreRepository;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoreAdapterTest {

    @InjectMocks
    private UserStoreAdapter userStoreAdapter;

    @Mock
    private UserStoreRepository userStoreRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreMapper storeMapper;

    @Test
    @DisplayName("상점 접근 권한 저장 테스트 - 엔티티 간 연관관계가 설정되고 저장되어야 한다")
    void saveAccess_Success() {
        // given
        Long userSrl = 1L;
        Long storeSrl = 100L;

        UserEntity userEntity = spy(new UserEntity()); // 양방향 연관관계를 확인하기 위해 spy 사용
        StoreEntity storeEntity = spy(StoreEntity.builder().id(storeSrl).storeId("STORE-01").build());

        given(userRepository.getReferenceById(userSrl)).willReturn(userEntity);
        given(storeRepository.getReferenceById(storeSrl)).willReturn(storeEntity);

        // when
        userStoreAdapter.saveAccess(userSrl, storeSrl);

        // then
        // 1. userStoreRepository.save()가 호출되었는지 확인
        verify(userStoreRepository, times(1)).save(any(UserStoreEntity.class));

        // 2. 엔티티 내부 리스트에 서로 추가되었는지 확인 (비즈니스 로직 검증)
        assertThat(userEntity.getAccessibleStores()).hasSize(1);
        assertThat(storeEntity.getAssignedUsers()).hasSize(1);
    }

    @Test
    @DisplayName("상점 접근 권한 제거 테스트 - storeId로 상점을 찾아 권한을 삭제해야 한다")
    void removeAccess_Success() {
        // given
        Long userId = 1L;
        String storeIdStr = "0123456789";
        Long storeSrl = 100L;

        StoreEntity storeEntity = StoreEntity.builder().id(storeSrl).storeId(storeIdStr).build();
        // Domain 모델로 변환될 정보 설정
        Store storeDomain = Store.builder().srl(storeSrl).build();

        given(storeRepository.findByStoreId(any())).willReturn(Optional.of(storeEntity));
        given(storeMapper.toDomain(storeEntity)).willReturn(storeDomain);

        // when
        userStoreAdapter.removeAccess(userId, storeIdStr);

        // then
        // deleteByUserSrlAndStoreSrl이 올바른 파라미터로 호출되었는지 확인
        verify(userStoreRepository).deleteByUserSrlAndStoreSrl(userId, storeSrl);
    }

    @Test
    @DisplayName("상점 접근 권한 제거 실패 테스트 - 상점 정보가 없으면 예외가 발생한다")
    void removeAccess_Fail_NotFound() {
        // given
        Long userId = 1L;
        String invalidStoreId = "INVALID-ID";
        given(storeRepository.findByStoreId(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userStoreAdapter.removeAccess(userId, invalidStoreId))
                .isInstanceOf(InvalidStateException.class)
                .hasMessageContaining("상점 정보가 없습니다");
    }
}