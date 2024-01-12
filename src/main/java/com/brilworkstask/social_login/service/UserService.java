package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.UserDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface UserService{
    UserDto saveUserData(GoogleIdToken.Payload payload,String registrationID);
}
