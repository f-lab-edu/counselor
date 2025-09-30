package com.hyejin.counselor.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String resultCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS","요청이 성공적으로 처리되었습니다.",data);
    }
    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>("FAIL","요청에 실패했습니다.",data);
    }



}
