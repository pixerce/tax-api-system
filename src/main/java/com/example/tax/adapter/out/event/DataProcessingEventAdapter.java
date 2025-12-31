package com.example.tax.adapter.out.event;

import com.example.tax.application.port.out.DataProcessingEventPort;
import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataProcessingEventAdapter implements DataProcessingEventPort {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(DataProcessingStartedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publish(DataProcessingCompletedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publish(DataProcessingFailedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
