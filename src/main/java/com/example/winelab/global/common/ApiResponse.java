package com.example.winelab.global.common;

import lombok.Getter;
import com.example.winelab.global.exception.ErrorCode;

@Getter
public class ApiResponse<T> {


    private final int status;
    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(int status, boolean success, String message, T data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, true, "성공", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, true, message, data);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus().value(), false, errorCode.getMessage(), null);
    }
}