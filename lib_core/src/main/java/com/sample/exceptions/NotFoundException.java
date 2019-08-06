package com.sample.exceptions;

public final class NotFoundException extends ServiceException {

    public NotFoundException(final String message) {
        super(message);
    }
}
