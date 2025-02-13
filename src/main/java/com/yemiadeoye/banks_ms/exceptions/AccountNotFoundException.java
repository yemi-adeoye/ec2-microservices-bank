package com.yemiadeoye.banks_ms.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }
}
