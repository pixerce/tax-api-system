package com.example.tax.application.service;

import com.example.tax.domain.valueobject.StoreId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExecutionTimeGuarantorDecorator implements DataCollectionProcessor {

    private final DataCollectionProcessor delegate;
    private final Long executionTime;

    protected static final Long DEFAULT_EXECUTION_TIME = 5 * 60 * 1000L;

    @Override
    public StoreId process() {
        long startTime = System.currentTimeMillis();
        StoreId result = this.delegate.process();
        long duration = System.currentTimeMillis() - startTime;
        long sleepTime = executionTime - duration;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }

    @Override
    public void started() {
        delegate.started();
    }

    @Override
    public void finished() {
        delegate.finished();
    }

    @Override
    public void failed() {
        delegate.failed();
    }
}
