package com.brilworkstask.social_login.utils;

import org.springframework.stereotype.Component;

@Component
public interface Constants {
    String APP_ID_FOR_FACEBOOK = "884100219831453";
    String APP_SECRET_FOR_FACEBOOK = "d9346c04cea7ec356f0b53fc9cf397f4";
    String SUCCESS = "success";
    String LOGIN_WITH_DIFFERENT_PROVIDER = "Login with different provider";
    String FAIL = "fail";
    String LOGGED_IN_PROFILE = "me";
    String ALREADY_EXIST = "User Already Exist !!";
    Object NULL_CONSTANT = null;
    String FACEBOOK_API_URL = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=";
    String LINKEDIN_API_URL = "https://api.linkedin.com/v2/userinfo";
    String REDIRECT_URL = "http://localhost:8080/user/social-login/connect/facebook/callback";
    String FACEBOOK_ID = "id";
    String FACEBOOK_FIRSTNAME = "first_name";
    String FACEBOOK_LASTNAME = "last_name";
    String FACEBOOK_EMAIL = "email";
    String LINKEDIN_ID = "sub";
    String LINKEDIN_FIRSTNAME = "given_name";
    String LINKEDIN_LASTNAME = "family_name";
    String LINKEDIN_EMAIL = "email";




}
