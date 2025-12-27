package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.entity.UserEntity;
import com.example.tax.adapter.out.persistence.entity.UserStoreEntity;
import com.example.tax.adapter.out.persistence.mapper.StoreMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.adapter.out.persistence.repository.UserRepository;
import com.example.tax.adapter.out.persistence.repository.UserStoreRepository;
import com.example.tax.application.port.out.UserStorePort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.Store;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserStoreAdapter implements UserStorePort {

    private final UserStoreRepository userStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public void saveAccess(final Long userSrl, final Long storeSrl) {
        UserEntity userEntity = userRepository.getReferenceById(userSrl);
        StoreEntity storeEntity = storeRepository.getReferenceById(storeSrl);

        UserStoreEntity userStoreEntity = UserStoreEntity.createAccess(userEntity, storeEntity);
        userStoreRepository.save(userStoreEntity);
    }

    @Override
    public void removeAccess(final Long userId, final String storeId) {
        Optional<StoreEntity> storeOptional = storeRepository.findByStoreId(StoreId.of(storeId).getId());
        StoreEntity entity = storeOptional.orElseThrow(
                () -> new InvalidStateException("상점 정보가 없습니다. storeId: " + storeId));
        Store store = storeMapper.toDomain(entity);
        userStoreRepository.deleteByUserSrlAndStoreSrl(userId, store.getSrl());
    }

    @Override
    public Boolean existsByUserSrlAndStoreId(Long userSrl, StoreId storeId) {
        return userStoreRepository.existsByUserSrlAndStoreId(userSrl, storeId.getId());
    }
}
