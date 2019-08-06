package com.sample.exceptions;

public final class AlreadyExistsException extends ServiceException {

    public AlreadyExistsException(final String message) {
        super(message);
    }
}
