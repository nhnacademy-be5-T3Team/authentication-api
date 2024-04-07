package com.t3t.authenticationapi.account.exception;

public class TokenAlreadyExistsException extends RuntimeException{
    public TokenAlreadyExistsException(String e) {
        super(e);
    }
}
