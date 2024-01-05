package com.brilworkstask.social_login.config;

import com.brilworkstask.social_login.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig {

    @Bean
    public FacebookConnectionFactory createConnectionFactory(){
        return new FacebookConnectionFactory(Constants.APP_ID_FOR_FACEBOOK, Constants.APP_SECRET_FOR_FACEBOOK);
    }

}
