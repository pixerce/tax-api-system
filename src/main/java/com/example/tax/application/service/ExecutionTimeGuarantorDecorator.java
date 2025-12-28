package com.example.tax.application.service;

import com.example.tax.domain.valueobject.StoreId;

public class ExecutionTimeGuarantorDecorator implements DataCollectionProcessor {

    private final DataCollectionProcessor delegate;
    private final Long executionTime;

    private final Long startTime;
    protected static final Long DEFAULT_EXECUTION_TIME = 5 * 60 * 1000L;

    public ExecutionTimeGuarantorDecorator(DataCollectionProcessor delegate, Long executionTime) {
        this.delegate = delegate;
        this.executionTime = executionTime;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public StoreId process() {
        return this.delegate.process();
    }

    @Override
    public void done() {
        long duration = System.currentTimeMillis() - startTime;
        long sleepTime = executionTime - duration;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
