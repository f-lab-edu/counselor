package com.hyejin.counselor.app.common.eNum;

import org.springframework.http.HttpStatus;

public enum ResCode {

    SUCCESS(HttpStatus.OK.value(),"요청이 성공적으로 처리되었습니다."),
    FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(),"요청에 실패하였습니다.");

    private int code;
    private String message;

    ResCode(int code, String message){
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
