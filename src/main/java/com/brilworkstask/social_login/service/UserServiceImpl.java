package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.exception.NotAcceptableException;
import com.brilworkstask.social_login.repository.UserRepository;
import com.brilworkstask.social_login.utils.Constants;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    OAuthUtils oAuthUtils;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @Override
    public String save(User userProfile) {
          if (null == userProfile.getId()){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.REFERENCE_ID_SHOULD_NOT_BLANKED);
          }
        List<Long> referenceIds = userRepository.findReferenceIds();
        if (!referenceIds.contains(Long.valueOf(userProfile.getId()))) {
            com.brilworkstask.social_login.model.User user =new com.brilworkstask.social_login.model.User();
            user.setEmail(userProfile.getEmail());
            user.setFirstName(userProfile.getFirstName());
            user.setLastName(userProfile.getLastName());
            user.setReferenceId(Long.valueOf(userProfile.getId()));
            user.setProviderEnum(ProviderEnum.FACEBOOK);
            userRepository.save(user);
            return Constants.SUCCESS;
        }
        return Constants.ALREADY_EXIST;
    }

    @Override
    public String fetchUserData(String accessToken) {
        if (null == accessToken){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
        User userProfile = oAuthUtils.getUserProfileFromFacebookUsingAccessToken(accessToken);
        String responce = save(userProfile);
        return responce;
    }

    @Override
    public String fetchUserDataUsingCallBackApi(String authenticationCode){
        if (null == authenticationCode){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
        User userProfile = oAuthUtils.getUserProfileFromFacebookUsingAuthCode(authenticationCode);
        String responce = save(userProfile);
        return responce;
    }
}
