package com.brilworkstask.social_login.exception;

import java.util.Map;

public class NotAcceptableException extends BaseException {

    public NotAcceptableException(NotAcceptableExceptionMSG notAcceptableExceptionMSG) {
        super(406, notAcceptableExceptionMSG.getStatusCode(), notAcceptableExceptionMSG.getErrorMessage(),
                notAcceptableExceptionMSG.getDeveloperMessage(),notAcceptableExceptionMSG.getDefaultMessage(),notAcceptableExceptionMSG.getDefaultMessageParamMap());
    }

    public enum NotAcceptableExceptionMSG {
        REFERENCE_ID_SHOULD_NOT_BLANKED("4060001", "Reference id is null", "Reference id is null"),
        ACCESS_TOKEN_IS_NOT_NULL("406002", "Access token is null", "Access token is null"),
        TOKEN_INVALID_OR_EXPIRED("406003","Token is invalid or expired","Token is invalid or expired"),
        INVALID_RESPONCE("406003","Responce is invalid", "Responce is invalid"),
        SOCIAL_ID_IS_NOT_NULL("406004", "Social Id is null", "Id is null");

        private String statusCode;
        private String errorMessage;
        private String developerMessage;
        private String defaultMessage;
        private Map<String,String> defaultMessageParamMap;

        NotAcceptableExceptionMSG(String statusCode, String errorMessage, String developerMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.developerMessage = developerMessage;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public Map<String,String> getDefaultMessageParamMap() {
            return defaultMessageParamMap;
        }

        public void setDefaultMessageParamMap(Map<String,String> defaultMessageParamMap) {
            this.defaultMessageParamMap = defaultMessageParamMap;
        }
    }
}
