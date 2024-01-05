package com.brilworkstask.social_login.utils;

import org.springframework.stereotype.Component;

@Component
public interface Constants {
    String APP_ID_FOR_FACEBOOK = "884100219831453";
    String APP_SECRET_FOR_FACEBOOK = "d9346c04cea7ec356f0b53fc9cf397f4";
    String SUCCESS = "success";
    String FAIL = "fail";
    String LOGGED_IN_PROFILE = "me";
    String ALREADY_EXIST = "User Already Exist !!";
    Object NULL_CONSTANT = null;
    String FACEBOOK_API_URL = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=";
    String REDIRECT_URL = "http://localhost:8080/user/social-login/connect/facebook/callback";


}
