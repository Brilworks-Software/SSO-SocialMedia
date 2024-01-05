package com.brilworkstask.social_login.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BaseException extends RuntimeException{

    private final int status;
    private final String errorCode;
    private final String errorMessage;
    private final String developerMessage;
    private final String defaultMessage;
    private final Map<String, String> defaultMessageParamMap;

    public BaseException(int httpStatus, String errorCode,
                         String errorMessage, String developerMessage, String defaultMessage, Map<String, String> defaultMessageParamMap) {
        super(developerMessage);
        this.status = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
        this.defaultMessage = defaultMessage;
        this.defaultMessageParamMap = defaultMessageParamMap;

    }

}
