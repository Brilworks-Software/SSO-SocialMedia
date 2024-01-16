package com.brilworkstask.social_login.exceptions;

import com.brilworkstask.social_login.dto.ExceptionDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenNotValidException.class)
    @ResponseBody
    public ExceptionDto tokenNotValidExceptionClass(TokenNotValidException tokenNotValidException){
        return new ExceptionDto(tokenNotValidException.getStatus(),tokenNotValidException.getErrorMessage(),tokenNotValidException.getDeveloperMessage());
    }
}
