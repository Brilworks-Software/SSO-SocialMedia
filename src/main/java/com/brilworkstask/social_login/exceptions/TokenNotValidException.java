package com.brilworkstask.social_login.exceptions;

public class TokenNotValidException extends RuntimeException{
    private Integer status;
    private String errorMessage;

    private String developerMessage;

    public TokenNotValidException(Integer status, String errorMessage, String developerMessage) {
        super(errorMessage);
        this.status = status;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}
