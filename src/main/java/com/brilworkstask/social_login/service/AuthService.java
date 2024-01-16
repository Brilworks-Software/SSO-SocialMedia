package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.UserDto;
import com.brilworkstask.social_login.model.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {
    UserDto fetchAndSaveDataGoogleSocialLogin(String token,String registrationId) throws GeneralSecurityException, IOException;
}
