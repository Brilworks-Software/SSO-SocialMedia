package com.brilworkstask.social_login.service;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.enums.UserStatus;
import com.brilworkstask.social_login.exception.NotAcceptableException;
import com.brilworkstask.social_login.model.User;
import com.brilworkstask.social_login.model.UserProvider;
import com.brilworkstask.social_login.repository.UserProviderRepository;
import com.brilworkstask.social_login.repository.UserRepository;
import com.brilworkstask.social_login.utils.Constants;
import com.brilworkstask.social_login.utils.OAuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProviderRepository userProviderRepository;

    @Autowired
    OAuthUtils oAuthUtils;

    public final FacebookConnectionFactory facebookConnectionFactory;

    public UserServiceImpl(FacebookConnectionFactory facebookConnectionFactory) {
        this.facebookConnectionFactory = facebookConnectionFactory;
    }

    @Override
    @Transactional
    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer) {
          if (null == socialProfileDetailsTransfer.getId()){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.REFERENCE_ID_SHOULD_NOT_BLANKED);
          }
//        List<String> referenceIds = userRepository.findReferenceIds(); // We can pass ORG_ID if needed
        // TODO - Jay - create query that return boolean based on passed Ref_ID
        // Check if the current social profile's ID already exists in the repository
        if (null == socialProfileDetailsTransfer.getEmail()){
            if (!userRepository.existBySocialId(socialProfileDetailsTransfer.getId())) {
                User user = new User(socialProfileDetailsTransfer.getFirstName(),
                        socialProfileDetailsTransfer.getLastName(),
                        socialProfileDetailsTransfer.getEmail(),
                        socialProfileDetailsTransfer.getId()); // TODO - Jay - Pass all the fields here
                userRepository.save(user);
                UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                userProviderRepository.save(userProvider);
                return Constants.SUCCESS;
            }else {
                User user = userRepository.findBySocialId(socialProfileDetailsTransfer.getId());
                if (!userProviderRepository.existsByUserIdAndProvider(user.getId(), socialProfileDetailsTransfer.getProvider())){
                    UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                    userProviderRepository.save(userProvider);
                    return Constants.LOGIN_WITH_DIFFERENT_PROVIDER;
                }
                return Constants.ALREADY_EXIST;
            }
        }

        if (!userRepository.existByEmailId(socialProfileDetailsTransfer.getEmail())) {
            User user = new User(socialProfileDetailsTransfer.getFirstName(),
                    socialProfileDetailsTransfer.getLastName(),
                    socialProfileDetailsTransfer.getEmail(),
                    socialProfileDetailsTransfer.getId()); // TODO - Jay - Pass all the fields here
                userRepository.save(user);
                UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                userProviderRepository.save(userProvider);
            return Constants.SUCCESS;
        }else {
            User user = userRepository.findByEmail(socialProfileDetailsTransfer.getEmail());
            if (!userProviderRepository.existsByUserIdAndProvider(user.getId(), socialProfileDetailsTransfer.getProvider())){
                UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                userProviderRepository.save(userProvider);
                return Constants.LOGIN_WITH_DIFFERENT_PROVIDER;
            }
            return Constants.ALREADY_EXIST;
        }
    }

    @Override
    public SocialProfileDetailsTransfer fetchUserDataFromFacebookApi(String accessToken){
            if (null == accessToken){
                throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
            }
           // Fetch user profile data using the access token
            SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getRestTemplateFacebook(accessToken);
            String responce = save(socialProfileDetailsTransfer);
            if (responce.equals(Constants.SUCCESS)){
                socialProfileDetailsTransfer.setStatus(UserStatus.SAVED);
            } else if (responce.equals(Constants.ALREADY_EXIST)) {
                socialProfileDetailsTransfer.setStatus(UserStatus.ALREADY_EXIST);
            } else if (responce.equals(Constants.LOGIN_WITH_DIFFERENT_PROVIDER)){
                socialProfileDetailsTransfer.setStatus(UserStatus.LOGIN_WITH_DIFFERENT_PROVIDER);
            } else {
                socialProfileDetailsTransfer.setStatus(UserStatus.FAIL);
            }
            return socialProfileDetailsTransfer;
    }

    @Override
    public SocialProfileDetailsTransfer fetchUserDataFromLinkedinApi(String accessToken) {
        if (null == accessToken){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
//         Fetch user profile data using the access token
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getRestTemplateLinkedIn(accessToken);
        String responce = save(socialProfileDetailsTransfer);
        if (responce.equals(Constants.SUCCESS)){
            socialProfileDetailsTransfer.setStatus(UserStatus.SAVED);
        } else if (responce.equals(Constants.ALREADY_EXIST)) {
            socialProfileDetailsTransfer.setStatus(UserStatus.ALREADY_EXIST);
        } else if (responce.equals(Constants.LOGIN_WITH_DIFFERENT_PROVIDER)){
            socialProfileDetailsTransfer.setStatus(UserStatus.LOGIN_WITH_DIFFERENT_PROVIDER);
        } else {
            socialProfileDetailsTransfer.setStatus(UserStatus.FAIL);
        }
        return socialProfileDetailsTransfer;
    }

    @Override
    public String fetchUserDataUsingCallBackApi(String authenticationCode){
        if (null == authenticationCode){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
        // Fetch user profile data using the access token
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getUserProfileFromFacebookUsingAuthCode(authenticationCode);
        return save(socialProfileDetailsTransfer);
    }
}
