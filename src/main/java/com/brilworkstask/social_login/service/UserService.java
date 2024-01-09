package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import org.apache.coyote.BadRequestException;
import org.springframework.social.facebook.api.User;

public interface UserService {
    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer);
    public SocialProfileDetailsTransfer fetchUserDataFromFacebookApi(String accessToken) ;
    public SocialProfileDetailsTransfer fetchUserDataFromLinkedinApi(String accessToken);
    public String fetchUserDataUsingCallBackApi(String authenticationCode);
}
