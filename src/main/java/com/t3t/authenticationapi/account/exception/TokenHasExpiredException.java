package com.t3t.authenticationapi.account.exception;

public class TokenHasExpiredException extends RuntimeException{
    public TokenHasExpiredException(String s) {
        super(s);
    }
}
