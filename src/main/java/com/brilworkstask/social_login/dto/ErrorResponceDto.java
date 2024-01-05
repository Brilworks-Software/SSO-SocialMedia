package com.brilworkstask.social_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponceDto {

    private String errorMessage;
    private String errorCode;
    private String developerMessage;

}
