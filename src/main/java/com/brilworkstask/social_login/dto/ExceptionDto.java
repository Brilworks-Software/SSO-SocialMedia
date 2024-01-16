package com.brilworkstask.social_login.dto;

public class ExceptionDto {

    private Integer status;
    private String errorMessage;

    private String developerMessage;

    public ExceptionDto(Integer status, String errorMessage, String developerMessage) {
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
