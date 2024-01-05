package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.exception.NotAcceptableException;
import com.brilworkstask.social_login.model.User;
import com.brilworkstask.social_login.repository.UserRepository;
import com.brilworkstask.social_login.utils.Constants;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
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
    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer) {
          if (null == socialProfileDetailsTransfer.getId()){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.REFERENCE_ID_SHOULD_NOT_BLANKED);
          }
        List<Long> referenceIds = userRepository.findReferenceIds();
        // Check if the current social profile's ID already exists in the repository
        if (!referenceIds.contains(Long.valueOf(socialProfileDetailsTransfer.getId()))) {
            User user = new User();
            user.setEmail(socialProfileDetailsTransfer.getEmail());
            user.setFirstName(socialProfileDetailsTransfer.getFirstName());
            user.setLastName(socialProfileDetailsTransfer.getLastName());
            user.setReferenceId(Long.valueOf(socialProfileDetailsTransfer.getId()));
            user.setProviderEnum(ProviderEnum.FACEBOOK);
            userRepository.save(user);
            return Constants.SUCCESS;
        }
        return Constants.ALREADY_EXIST;
    }

    @Override
    public String fetchUserData(String accessToken) throws BadRequestException {
            if (null == accessToken){
                throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
            }
           // Fetch user profile data using the access token
            SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getRestTemplateFacebook(accessToken);
            String responce = save(socialProfileDetailsTransfer);
            return responce;
    }

    @Override
    public String fetchUserDataUsingCallBackApi(String authenticationCode){
        if (null == authenticationCode){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
        // Fetch user profile data using the access token
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getUserProfileFromFacebookUsingAuthCode(authenticationCode);
        String responce = save(socialProfileDetailsTransfer);
        return responce;
    }
}
