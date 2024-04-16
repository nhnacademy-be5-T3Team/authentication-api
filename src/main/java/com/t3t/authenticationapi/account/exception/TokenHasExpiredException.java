package com.t3t.authenticationapi.account.exception;
/**
 * 만료된 토큰으로 접근시 발생하는 예외
 * @author joohyun1996 (이주현)
 */
public class TokenHasExpiredException extends RuntimeException{
    public TokenHasExpiredException(String s) {
        super(s);
    }
}
