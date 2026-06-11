package com.example.demo.exception;

import com.example.demo.response.ErrorCode;

public class ValidationException extends RuntimeException {
    private ErrorCode errorCode;
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
