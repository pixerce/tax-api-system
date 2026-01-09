package com.example.tax.domain.exception;

public class DuplicateCollectionTaskException extends BusinessException {

    public DuplicateCollectionTaskException(final String message) {
        super(message);
    }

    public DuplicateCollectionTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
