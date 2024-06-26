package com.authservice.authservice.exceptions;

public class UniquenessError extends RuntimeException {

    public UniquenessError(String message) {
        super(message);
    }

}
