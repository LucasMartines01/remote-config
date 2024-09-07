package com.ezliv.domain.exceptions;

public class ServerError extends RuntimeException {
    public ServerError(String message, Throwable cause) {
        super(message, cause);
    }
}
