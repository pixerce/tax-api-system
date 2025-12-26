package com.example.tax.application.service;

import com.example.tax.domain.valueobject.StoreId;
import com.example.tax.domain.valueobject.TaskStatus;

public interface TaskMonitor {
    void updateStatus(StoreId storeId, TaskStatus status);
    void reportFailure(StoreId storeId, String message);
}

