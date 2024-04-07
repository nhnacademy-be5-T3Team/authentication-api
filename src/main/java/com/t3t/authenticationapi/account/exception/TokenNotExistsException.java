package com.t3t.authenticationapi.account.exception;

public class TokenNotExistsException extends RuntimeException{
    public TokenNotExistsException(String s) {
        super(s);
    }
}
