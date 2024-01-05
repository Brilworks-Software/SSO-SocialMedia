package com.brilworkstask.social_login.exception;

import com.brilworkstask.social_login.dto.ErrorResponceDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorResponceDto webApplicationException(BaseException baseException, HttpServletResponse response) {
        return new ErrorResponceDto(baseException.getErrorMessage(), baseException.getErrorCode(),
                baseException.getDeveloperMessage());
    }

}
