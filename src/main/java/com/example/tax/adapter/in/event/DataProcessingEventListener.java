package com.example.tax.adapter.in.event;

import com.example.tax.application.port.out.CollectionTaskPort;
import com.example.tax.application.usecase.CalculateVatUseCase;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataProcessingEventListener {

    private final CollectionTaskPort collectionTaskPort;
    private final CalculateVatUseCase calculateVatUseCase;

    @EventListener
    public void handleStartedEvent(DataProcessingStartedEvent event) {
        var task = collectionTaskPort.findLastestTaskByStoreId(event.getStoreId().getId(), event.getTargetYearMonth())
                .orElseThrow(() -> new IllegalStateException("Task not found"));
        task.started();
        collectionTaskPort.save(task);
    }

    @EventListener
    public void handleCompletedEvent(DataProcessingCompletedEvent event) {
        var task = collectionTaskPort.findLastestTaskByStoreId(event.getStoreId().getId(), event.getTargetYearMonth())
                .orElseThrow(() -> new IllegalStateException("Task not found"));
        task.finished();
        collectionTaskPort.save(task);
        calculateVatUseCase.calculateVat(event.getStoreId(), event.getTargetYearMonth());
    }

    @EventListener
    public void handleFailedEvent(DataProcessingFailedEvent event) {
        var task = collectionTaskPort.findLastestTaskByStoreId(event.getStoreId().getId(), event.getTargetYearMonth())
                .orElseThrow(() -> new IllegalStateException("Task not found"));
        task.failed(event.getErrorMessage());
        collectionTaskPort.save(task);
    }
}
