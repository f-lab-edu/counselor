package com.hyejin.counselor.core.common.enums;

public enum ErrorCode {

    INVALID_CHAT_INFO("C003","메세지 형태가 잘못되었습니다."),
    INVALID_COUNSEL_INFO("C001","상담을 생성할 수 없습니다."),
    NULL_DATA("C002","데이터가 존재하지 않습니다.");

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