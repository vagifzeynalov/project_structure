package com.sample.exceptions;

import java.util.function.Supplier;

public abstract class ServiceException extends Exception implements Supplier<ServiceException> {

    ServiceException() {
        super();
    }

    ServiceException(final String message) {
        super(message);
    }

    ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    ServiceException(final Throwable cause) {
        super(cause);
    }

    @Override
    public ServiceException get() {
        return null;
    }
}
