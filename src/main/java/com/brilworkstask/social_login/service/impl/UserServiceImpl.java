package com.brilworkstask.social_login.service.impl;

import com.brilworkstask.social_login.dto.UserDto;
import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.model.User;
import com.brilworkstask.social_login.model.UserSocialHandleLogin;
import com.brilworkstask.social_login.repository.UserRepository;
import com.brilworkstask.social_login.repository.UserSocialLoginHandlesRepository;
import com.brilworkstask.social_login.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static  final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSocialLoginHandlesRepository userSocialLoginHandlesRepository;

    @Override
    public UserDto saveUserData(GoogleIdToken.Payload payload,String registrationId) {
        String email = payload.getEmail();

        if (null != email){ //if provider return non-null email
            // checking existence in database
            if(checkUserExistenceByEmail(email)){
                User existingUserByEmailId = userRepository.findByEmail(payload.getEmail());
                return new UserDto(existingUserByEmailId);
            } else {
                //if user not exists by email create new user in database
                User createdByEmail = createNewUser(payload,registrationId);
                return new UserDto(createdByEmail);
            }
        } else {
            //check user existence by social id
            String socialId = payload.getSubject();
            if (userRepository.existsBySocialId(socialId)){
                User existUserBySocialId = userRepository.findBySocialId(socialId);
                return new UserDto(existUserBySocialId);
            } else {
                // if user not exists by social id create new user in database
                User createdBySocialId = createNewUser(payload,registrationId);
                return new UserDto(createdBySocialId);
            }
        }
    }

    private User createNewUser(GoogleIdToken.Payload payload, String registrationId) {
        String[] fullName = getFirstNameOrLastname(payload.get("name"));
        User user = new User(fullName[0],fullName[1], payload.getEmail(),payload.getSubject());
        User newUser = userRepository.save(user);
        logger.info("user saved with id {}",newUser.getId());

        ProviderEnum providerEnum = ProviderEnum.getSocialLoginProvider(registrationId);
        UserSocialHandleLogin socialHandleLogin = new UserSocialHandleLogin(newUser.getId(),providerEnum);
        UserSocialHandleLogin socialHandle = userSocialLoginHandlesRepository.save(socialHandleLogin);
        logger.info("User social login handle saved with ID : {}",socialHandle.getId());

        return newUser;
    }

    private boolean checkUserExistenceByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private String[] getFirstNameOrLastname(Object object) {
        String name = (String) object;
        return name.split(" ");
    }
}
