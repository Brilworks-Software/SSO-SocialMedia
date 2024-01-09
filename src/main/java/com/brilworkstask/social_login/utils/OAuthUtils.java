package com.brilworkstask.social_login.utils;

import com.brilworkstask.social_login.dto.FieldsTransfer;
import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.exception.NotAcceptableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.apache.coyote.BadRequestException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OAuthUtils {

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;
    private static final Logger logger = Logger.getLogger(OAuthUtils.class.getName());

    public OAuth2Parameters getOauth2Parameters(){
        logger.info("Retrieving OAuth2Parameters");
        OAuth2Parameters parameters = new OAuth2Parameters();
        // Set the redirect URI for the OAuth flow
        parameters.setRedirectUri(Constants.REDIRECT_URL);
        parameters.setScope("email, public_profile");
        return parameters;
    }


    public SocialProfileDetailsTransfer getUserProfileFromFacebookUsingAuthCode(String authenticationCode){
        logger.info("Retrieving user profile from Facebook using authentication code");
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
        socialProfileDetailsTransfer.setProvider(ProviderEnum.FACEBOOK);
        return socialProfileDetailsTransfer;
    }

    public SocialProfileDetailsTransfer getRestTemplateFacebook(String token) {
        logger.info("Retrieving user profile from Facebook using RestTemplate");
        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        // Construct the URL with the fields you want to retrieve
        String url = Constants.FACEBOOK_API_URL + token;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        try{
            // Make a GET request to the Facebook Graph API
            ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String response = responseEntity.getBody();
                if (StringUtils.isEmpty(response)) {
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
                // Set responce field in FieldsTransfer to reduce code length & improve readability
                FieldsTransfer fields = new FieldsTransfer();
                fields.setId("id");
                fields.setEmail("email");
                fields.setFirstName("first_name");
                fields.setLastName("last_name");
                SocialProfileDetailsTransfer socialProfileDetails = getProfileDetailsFromResponse(response, fields);
                socialProfileDetails.setProvider(ProviderEnum.FACEBOOK);
                return socialProfileDetails;
            }
        }catch (Exception e){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.TOKEN_INVALID_OR_EXPIRED);
        }
        return (SocialProfileDetailsTransfer) Constants.NULL_CONSTANT;
    }

    public SocialProfileDetailsTransfer getRestTemplateLinkedInII(String token){
        logger.info("Retrieving user profile from LinkedIn using RestTemplate");
        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        try{
            // Make a GET request to the LinkedIn API
            ResponseEntity<String> responseEntity = template.exchange(Constants.LINKEDIN_API_URL, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
                String response = responseEntity.getBody();
                if (response.isEmpty()){
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
             // Set responce field in FieldsTransfer to reduce code length & improve readability
                FieldsTransfer fields = new FieldsTransfer();
                fields.setId("sub");
                fields.setFirstName("given_name");
                fields.setLastName("family_name");
                fields.setEmail("email");
                SocialProfileDetailsTransfer socialProfileDetailsTransfer = getProfileDetailsFromResponse(response, fields);
                socialProfileDetailsTransfer.setProvider(ProviderEnum.LINKEDIN);
                return socialProfileDetailsTransfer;
            }
        }catch (Exception e){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.TOKEN_INVALID_OR_EXPIRED);
        }
        return (SocialProfileDetailsTransfer) Constants.NULL_CONSTANT;
    }

    public SocialProfileDetailsTransfer getProfileDetailsFromResponse(String response, FieldsTransfer fields) {
        logger.info("Retrieving user profile details from API response");
        ObjectMapper mapper = new ObjectMapper();
        SocialProfileDetailsTransfer socialProfileDetails = null;
        try {
            socialProfileDetails = new SocialProfileDetailsTransfer();
            // Parse the API response as JSON
            JsonNode jsonNode = mapper.readTree(response);
            // Retrieve and set the user ID
            JsonNode idNode = jsonNode.get(fields.getId());
            socialProfileDetails.setId(Optional.ofNullable(jsonNode.get(fields.getId()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .orElseThrow(() -> new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ID_IS_NOT_NULL)));
            // Retrieve and set the email if available
            Optional.ofNullable(jsonNode.get(fields.getEmail()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setEmail);
            // Retrieve and set the first name if available
            Optional.ofNullable(jsonNode.get(fields.getFirstName()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setFirstName);
            // Retrieve and set the last name if available
            Optional.ofNullable(jsonNode.get(fields.getLastName()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setLastName);

        } catch (Exception ex) {
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
        }
        return socialProfileDetails;
    }



}
