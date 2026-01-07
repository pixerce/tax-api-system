package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.entity.UserEntity;
import com.example.tax.adapter.out.persistence.entity.UserStoreEntity;
import com.example.tax.adapter.out.persistence.entity.UserStoreId;
import com.example.tax.adapter.out.persistence.mapper.StoreMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.adapter.out.persistence.repository.UserRepository;
import com.example.tax.adapter.out.persistence.repository.UserStoreRepository;
import com.example.tax.application.port.out.UserStorePort;
import com.example.tax.domain.exception.InvalidStateException;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStoreAdapter implements UserStorePort {

    private final UserStoreRepository userStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Transactional
    @Override
    public void saveAccess(final Long userSrl, final Long storeSrl) {
        final UserStoreId id = new UserStoreId(userSrl, storeSrl);
        if (userStoreRepository.existsById(id))
            return;

        final UserEntity userEntity = userRepository.getReferenceById(userSrl);
        final StoreEntity storeEntity = storeRepository.getReferenceById(storeSrl);

        UserStoreEntity userStoreEntity = UserStoreEntity.createAccess(userEntity, storeEntity);
        userStoreRepository.save(userStoreEntity);
    }

    @Transactional
    @Override
    public void removeAccess(final Long userSrl, final Long storeSrl) {
        final UserStoreId id = new UserStoreId(userSrl, storeSrl);
        if (!userStoreRepository.existsById(id))
            throw new InvalidStateException(String.format("권한이 없습니다. userSrl={}, storeSrl={}", userSrl, storeSrl));

        userStoreRepository.deleteById(id);
    }

    @Override
    public Boolean existsByUserSrlAndStoreId(Long userSrl, StoreId storeId) {
        return userStoreRepository.existsByUserSrlAndStoreId(userSrl, storeId.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAccessibleStores(Long userSrl) {
        List<UserStoreEntity> accessList = userStoreRepository.findAllWithStoreByUserSrl(userSrl);

        return accessList.stream()
                .map(userStore -> userStore.getStore().getStoreId())
                .toList();
    }
}
