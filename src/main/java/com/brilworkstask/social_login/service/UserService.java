package com.brilworkstask.social_login.service;

import org.springframework.social.facebook.api.User;

public interface UserService {
    public String save(User userProfile);
    public String fetchUserData(String accessToken);
    public String fetchUserDataUsingCallBackApi(String authenticationCode);
}
