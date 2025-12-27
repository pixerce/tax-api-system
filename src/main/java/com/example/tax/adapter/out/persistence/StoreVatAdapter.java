package com.example.tax.adapter.out.persistence;

import com.example.tax.application.port.out.StoreVatPort;
import com.example.tax.domain.valueobject.StoreVat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreVatAdapter implements StoreVatPort {

    @Override
    public void save(final StoreVat storeVat) {
    }
}
