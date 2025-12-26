package com.example.tax.application.service;

import com.example.tax.domain.valueobject.StoreId;

public interface DataCollectionProcessor {

    void started();
    StoreId process();
    void finished();
    void failed();
}
