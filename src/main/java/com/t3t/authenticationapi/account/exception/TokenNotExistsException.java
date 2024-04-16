package com.t3t.authenticationapi.account.exception;
/**
 * auth - server로 접근하는 토큰에 대한 refresh토큰이 없는경우 발생하는 예외
 * @author joohyun1996 (이주현)
 */
public class TokenNotExistsException extends RuntimeException{
    public TokenNotExistsException(String s) {
        super(s);
    }
}
