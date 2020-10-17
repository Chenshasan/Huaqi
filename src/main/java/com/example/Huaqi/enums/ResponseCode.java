package com.example.Huaqi.enums;

public enum ResponseCode {
    Success(200),Failure(404);

    private final Integer code;
    ResponseCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
