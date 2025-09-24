package com.hyejin.counselor.app.common;

import com.hyejin.counselor.app.common.eNum.ResCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String resultCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> makeResponse(ResCode resCode, T data) {
        return new ApiResponse<>(resCode.name(), resCode.getMessage(),data);
    }
    public static <T> ApiResponse<T> makeResponse(ResCode resCode) {
        return new ApiResponse<>(resCode.name(), resCode.getMessage(),null);
    }




}
