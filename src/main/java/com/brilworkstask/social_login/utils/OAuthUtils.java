package com.brilworkstask.social_login.utils;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.exception.NotAcceptableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthUtils {

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    public OAuth2Parameters getOauth2Parameters(){
        OAuth2Parameters parameters = new OAuth2Parameters();
        // Set the redirect URI for the OAuth flow
        parameters.setRedirectUri(Constants.REDIRECT_URL);
        parameters.setScope("email, public_profile");
        return parameters;
    }


    public SocialProfileDetailsTransfer getUserProfileFromFacebookUsingAuthCode(String authenticationCode){
        // Exchange the authentication code for an access grant
        AccessGrant accessGrant = facebookConnectionFactory
                .getOAuthOperations()
                .exchangeForAccess(authenticationCode, Constants.REDIRECT_URL, null);
        // Create a connection to Facebook using the access grant
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        Facebook facebook = connection.getApi();
        String[] fields = {"id", "email", "first_name", "last_name"}; // Corrected field names
        // Fetch the user profile data from Facebook
        User userProfile = facebook.fetchObject("me", User.class, fields);
        if (null == userProfile.getId()){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.REFERENCE_ID_SHOULD_NOT_BLANKED);
        }
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = new SocialProfileDetailsTransfer();
        socialProfileDetailsTransfer.setId(userProfile.getId());
        socialProfileDetailsTransfer.setFirstName(userProfile.getFirstName());
        socialProfileDetailsTransfer.setLastName(userProfile.getLastName());
        socialProfileDetailsTransfer.setEmail(userProfile.getEmail());
        return socialProfileDetailsTransfer;
    }

    public SocialProfileDetailsTransfer getRestTemplateFacebook(String token) throws BadRequestException {
        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        // Construct the URL with the fields you want to retrieve
        String url = Constants.FACEBOOK_API_URL + token;

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        // Make a GET request to the Facebook Graph API
        try{
            ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String response = responseEntity.getBody();
                if (StringUtils.isEmpty(response)) {
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Parse the JSON response
                    JsonNode facebookResponse = mapper.readTree(response);
                    SocialProfileDetailsTransfer socialProfileDetails = new SocialProfileDetailsTransfer();
                    socialProfileDetails.setId(facebookResponse.get("id").asText());
                    socialProfileDetails.setEmail(facebookResponse.get("email").asText());
                    socialProfileDetails.setFirstName(facebookResponse.get("first_name").asText());
                    socialProfileDetails.setLastName(facebookResponse.get("last_name").asText());
                    return socialProfileDetails;
                } catch (NotAcceptableException ex) {
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
            }
        }catch (Exception e){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.TOKEN_INVALID_OR_EXPIRED);
        }
        return (SocialProfileDetailsTransfer) Constants.NULL_CONSTANT;
    }


}
