package com.marving.boot.entity.enums;

/**
 * @author Thanatos
 */
public enum UserTypeEnum {


    TEACHER("0"),


    SELLER("1"),

    BUSINESS("2");

    private String code;

    UserTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
