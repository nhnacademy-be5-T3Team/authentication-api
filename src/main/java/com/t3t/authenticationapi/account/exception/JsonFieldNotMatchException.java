package com.t3t.authenticationapi.account.exception;

import java.io.IOException;
/**
 * 로그인시 회원의 id, pw에 해당하는 Json 객체가 올바르지 않은경우 발생하는 예외
 * @author joohyun1996 (이주현)
 */
public class JsonFieldNotMatchException extends RuntimeException{
    public JsonFieldNotMatchException(IOException e) {
        super(e);
    }
}
