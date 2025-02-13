package com.yemiadeoye.banks_ms.exceptions;

public class AccountDeletedException extends RuntimeException {
    public AccountDeletedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public AccountDeletedException(String errorMessage) {
        super(errorMessage);
    }

    public AccountDeletedException(Throwable cause) {
        super(cause);
    }
}
