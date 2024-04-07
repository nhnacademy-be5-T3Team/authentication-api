package com.t3t.authenticationapi.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class BaseResponse<T> {
    private String message;
    private T data;

    @Builder
    public BaseResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return BaseResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public BaseResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public BaseResponse<T> message(String message) {
        this.message = message;
        return this;
    }

}