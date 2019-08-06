package com.sample.exceptions;

public final class BadRequestException extends ServiceException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }
}
