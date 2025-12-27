package com.example.tax.adapter.out.persistence;

import com.example.tax.adapter.out.persistence.entity.StoreEntity;
import com.example.tax.adapter.out.persistence.mapper.StoreMapper;
import com.example.tax.adapter.out.persistence.repository.StoreRepository;
import com.example.tax.application.port.out.StorePort;
import com.example.tax.domain.valueobject.Store;
import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StorePersistenceAdapter implements StorePort {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public List<Store> findAll() {
        List<StoreEntity> entities = storeRepository.findAll();

        return entities.stream()
                .map(this.storeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByIds(List<Long> ids) {
        List<StoreEntity> entities = storeRepository.findAllById(ids);

        return entities.stream()
                .map(this.storeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Store> findByStoreId(final StoreId storeId) {
        Optional<StoreEntity> storeEntityOptional = this.storeRepository.findByStoreId(storeId.getId());
        if (storeEntityOptional.isPresent()) {
            return Optional.of(storeMapper.toDomain(storeEntityOptional.get()));
        }
        return Optional.empty();
    }
}