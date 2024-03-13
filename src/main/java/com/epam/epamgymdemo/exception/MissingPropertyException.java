package com.epam.epamgymdemo.exception;

public class MissingPropertyException extends RuntimeException {
    public MissingPropertyException(String message) {
        super(message);
    }
}
