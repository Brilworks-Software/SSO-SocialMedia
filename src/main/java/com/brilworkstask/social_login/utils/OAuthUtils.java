package com.brilworkstask.social_login.utils;

import com.brilworkstask.social_login.exception.NotAcceptableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class OAuthUtils {

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    public OAuth2Parameters getOauth2Parameters(){
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri("http://localhost:8080/user/social-login/connect/facebook/callback");
        parameters.setScope("email, public_profile");
        return parameters;
    }

    public User getUserProfileFromFacebookUsingAccessToken(String accessToken){
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"id", "email", "first_name", "last_name"}; // Fields to retrieve
        User userProfile = facebook.fetchObject("me", User.class, fields);
        return userProfile;
    }

    public User getUserProfileFromFacebookUsingAuthCode(String authenticationCode){
        AccessGrant accessGrant = facebookConnectionFactory
                .getOAuthOperations()
                .exchangeForAccess(authenticationCode, "http://localhost:8080/user/social-login/connect/facebook/callback", null);
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        Facebook facebook = connection.getApi();
        String[] fields = {"id", "email", "first_name", "last_name"}; // Corrected field names
        User userProfile = facebook.fetchObject("me", User.class, fields);
        return userProfile;
    }

}
