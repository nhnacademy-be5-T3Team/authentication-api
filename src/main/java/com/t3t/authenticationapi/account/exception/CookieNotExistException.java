package com.t3t.authenticationapi.account.exception;

public class CookieNotExistException extends NullPointerException{
    public CookieNotExistException(String s) {
        super(s);
    }
}
