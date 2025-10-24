package com.hyejin.counselor.core.common.enums;

public enum UserType {

    ADMIN("A","관리자"),
    COUNSELOR("C","상담사"),
    USER("U","회원");

    private String code;
    private String description;

    UserType(String code, String description){
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