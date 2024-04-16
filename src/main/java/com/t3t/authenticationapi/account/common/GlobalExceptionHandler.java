package com.t3t.authenticationapi.account.common;

import com.t3t.authenticationapi.account.exception.TokenAlreadyExistsException;
import com.t3t.authenticationapi.account.exception.TokenHasExpiredException;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import com.t3t.authenticationapi.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * access, refresh 토큰이 없을 경우에 대한 예외 처리 핸들러
     * @return 403 Forbidden - 예외 메시지 반한
     * @author joohyun1996 (이주현)
     */
    @ExceptionHandler(TokenNotExistsException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenNotExistsException(TokenNotExistsException tokenNotExistsException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<Void>().message(tokenNotExistsException.getMessage()));
    }
    /**
     * access 토큰이 만료된 경우에 대한 예외 처리 핸들러
     * @return 403 Forbidden - 예외 메시지 반한
     * @author joohyun1996 (이주현)
     */
    @ExceptionHandler(TokenHasExpiredException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenHasExpiredException(TokenHasExpiredException tokenHasExpiredException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<Void>().message(tokenHasExpiredException.getMessage()));
    }
    /**
     * refresh, blacklist 토큰이 이미 Redis에 저장되어 있을 경우에 대한 예외 처리 핸들러
     * @return 400 Forbidden - 예외 메시지 반한
     * @author joohyun1996 (이주현)
     */
    @ExceptionHandler(TokenAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenAlreadyExistsException(TokenAlreadyExistsException tokenAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<Void>().message(tokenAlreadyExistsException.getMessage()));
    }
}
