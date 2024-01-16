package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.dto.UserDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface UserService{
    UserDto saveUserData(GoogleIdToken.Payload payload,String registrationID);

    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer);
    public SocialProfileDetailsTransfer fetchUserDataFromFacebookApi(String accessToken) ;
    public SocialProfileDetailsTransfer fetchUserDataFromLinkedinApi(String accessToken);
    public String fetchUserDataUsingCallBackApi(String authenticationCode);
}
