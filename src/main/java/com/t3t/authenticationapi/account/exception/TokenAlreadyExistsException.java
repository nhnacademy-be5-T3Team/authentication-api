package com.t3t.authenticationapi.account.exception;
/**
 * 새로운 토큰을 발급하는데, 이미 토큰이 존재하는 경우 발생하는 예외
 * @author joohyun1996 (이주현)
 */
public class TokenAlreadyExistsException extends RuntimeException{
    public TokenAlreadyExistsException(String e) {
        super(e);
    }
}
