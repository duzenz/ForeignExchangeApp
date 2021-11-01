package com.openpayd.exchange.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiRuntimeException extends RuntimeException {

    private final String code;

    public ApiRuntimeException(String code, String msg, Exception e) {
        super(msg, e);
        this.code = code;
    }

    public ApiRuntimeException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
