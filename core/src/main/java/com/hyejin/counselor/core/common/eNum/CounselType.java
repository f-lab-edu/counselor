package com.hyejin.counselor.core.common.eNum;

public enum CounselType {

    READY("R","대기중"),
    IN_PROGRESS("I","진행중"),
    END("E","종료");

    private String code;
    private String description;

    CounselType(String code, String description){
        this.code = code;
        this.description = description;
    }
    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}