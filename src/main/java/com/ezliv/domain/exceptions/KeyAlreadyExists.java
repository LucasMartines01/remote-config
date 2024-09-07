package com.ezliv.domain.exceptions;

public class KeyAlreadyExists extends RuntimeException{
    public KeyAlreadyExists(String message) {
        super(message);
    }
}
