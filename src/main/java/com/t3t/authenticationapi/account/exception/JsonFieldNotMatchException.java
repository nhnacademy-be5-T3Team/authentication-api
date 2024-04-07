package com.t3t.authenticationapi.account.exception;

import java.io.IOException;

public class JsonFieldNotMatchException extends RuntimeException{
    public JsonFieldNotMatchException(IOException e) {
        super(e);
    }
}
