package com.t3t.authenticationapi.account.common;

import com.t3t.authenticationapi.account.exception.CookieNotExistException;
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
    @ExceptionHandler(TokenNotExistsException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenNotExistsException(TokenNotExistsException tokenNotExistsException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<Void>().message(tokenNotExistsException.getMessage()));
    }

    @ExceptionHandler(TokenHasExpiredException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenHasExpiredException(TokenHasExpiredException tokenHasExpiredException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<Void>().message(tokenHasExpiredException.getMessage()));
    }

    @ExceptionHandler(TokenAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<Void>> handleTokenAlreadyExistsException(TokenAlreadyExistsException tokenAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<Void>().message(tokenAlreadyExistsException.getMessage()));
    }

    @ExceptionHandler(CookieNotExistException.class)
    public ResponseEntity<BaseResponse<Void>> handleCookieNotExistsException(CookieNotExistException cookieNotExistException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<Void>().message(cookieNotExistException.getMessage()));
    }
}
