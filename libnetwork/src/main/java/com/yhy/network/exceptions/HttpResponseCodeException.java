package com.yhy.network.exceptions;

public class HttpResponseCodeException extends RuntimeException{

    private int code = 0;

    public HttpResponseCodeException(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
