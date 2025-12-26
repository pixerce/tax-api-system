package com.example.tax.application.service;

import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.domain.valueobject.CollectionTask;
import com.example.tax.domain.valueobject.StoreId;

import java.time.YearMonth;

public class PersistentCollectionTaskHandler implements CollectionTaskHandler {

    private final CollectionTaskPort collectionTaskPort;
    private final CollectionTask collectionTask;

    public PersistentCollectionTaskHandler(CollectionTaskPort collectionTaskPort, final StoreId storeId, final YearMonth targetYearMonth) {
        this.collectionTaskPort = collectionTaskPort;
        this.collectionTask = CollectionTask.create(storeId, targetYearMonth);
    }

    @Override
    public void start() {
        this.collectionTask.started();
        this.collectionTaskPort.save(this.collectionTask);
    }

    @Override
    public void finish() {
        this.collectionTask.finished();
        this.collectionTaskPort.save(this.collectionTask);
    }

    @Override
    public void fail() {
        this.collectionTask.failed();
        this.collectionTaskPort.save(this.collectionTask);
    }
}