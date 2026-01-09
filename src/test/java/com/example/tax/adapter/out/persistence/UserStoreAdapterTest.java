package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.UserStoreId;
import com.example.tax.adapter.out.persistence.mapper.StoreMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.adapter.out.persistence.repository.UserRepository;
import com.example.tax.adapter.out.persistence.repository.UserStoreRepository;
import com.example.tax.domain.exception.InvalidStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

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
    @DisplayName("상점 접근 권한 제거 테스트 - storeId로 상점을 찾아 권한을 삭제해야 한다")
    void removeAccess_Success() {
        Long userSrl = 1L;
        Long storeSrl = 100L;

        UserStoreId id = new UserStoreId(userSrl, storeSrl);
        given(userStoreRepository.existsById(id)).willReturn(true);

        userStoreAdapter.removeAccess(userSrl, storeSrl);

        verify(userStoreRepository).deleteById(id);
    }

    @Test
    @DisplayName("상점 접근 권한 제거 실패 테스트 - 상점 정보가 없으면 예외가 발생한다")
    void removeAccess_Fail_NotFound() {
        Long userSrl = 1L;
        Long invalidStoreSrl = 100L;
        given(userStoreRepository.existsById(any())).willReturn(false);

        assertThatThrownBy(() -> userStoreAdapter.removeAccess(userSrl, invalidStoreSrl))
                .isInstanceOf(InvalidStateException.class)
                .hasMessageContaining(String.format("권한이 없습니다. userSrl={}, storeSrl={}", userSrl, invalidStoreSrl));
    }
}