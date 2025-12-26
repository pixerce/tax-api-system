package com.example.tax.application;

import com.example.tax.application.service.TaskMonitor;
import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseTaskMonitor implements TaskMonitor {

    @Override
    public void updateStatus(StoreId storeId, TaskStatus status) {
    }

    @Override
    public void reportFailure(StoreId storeId, String message) {
    }
}