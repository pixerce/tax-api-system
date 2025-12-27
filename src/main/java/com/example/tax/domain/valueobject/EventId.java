package com.example.tax.domain.valueobject;

import java.util.UUID;

public class EventId {
    private final UUID id;

    private EventId(final UUID id) {
        this.id = id;
    }

    public static EventId create() {
        return new EventId(UUID.randomUUID());
    }
}
