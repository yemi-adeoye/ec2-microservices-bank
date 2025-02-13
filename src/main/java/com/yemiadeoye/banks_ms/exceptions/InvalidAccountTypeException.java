package com.yemiadeoye.banks_ms.exceptions;

public class InvalidAccountTypeException extends RuntimeException {
    public InvalidAccountTypeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public InvalidAccountTypeException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidAccountTypeException(Throwable cause) {
        super(cause);
    }
}
