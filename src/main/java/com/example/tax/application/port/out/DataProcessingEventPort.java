package com.example.tax.application.port.out;

import com.example.tax.domain.event.DataProcessingCompletedEvent;
import com.example.tax.domain.event.DataProcessingFailedEvent;
import com.example.tax.domain.event.DataProcessingStartedEvent;

public interface DataProcessingEventPort {
    void publish(DataProcessingStartedEvent event);
    void publish(DataProcessingCompletedEvent event);
    void publish(DataProcessingFailedEvent event);
}
