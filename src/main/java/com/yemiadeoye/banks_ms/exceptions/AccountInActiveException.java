package com.yemiadeoye.banks_ms.exceptions;

public class AccountInActiveException extends RuntimeException {
    public AccountInActiveException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public AccountInActiveException(String errorMessage) {
        super(errorMessage);
    }

    public AccountInActiveException(Throwable cause) {
        super(cause);
    }
}
