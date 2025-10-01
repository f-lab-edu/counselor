package com.hyejin.counselor.core.common.eNum;

public enum ErrorCode {

    INVALID_COUNSEL_INFO("C001","상담을 생성할 수 없습니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}