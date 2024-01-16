package com.brilworkstask.social_login.controller;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.service.UserServiceImpl;
import com.brilworkstask.social_login.service.impl.AuthServiceImpl;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    OAuthUtils oAuthUtils;

    @GetMapping("/fetchdata")
    public ResponseEntity<?> getDataFromAccessToken(@RequestParam("token") String token,
                                                    @RequestParam("id") String registrationId) throws GeneralSecurityException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.fetchAndSaveDataGoogleSocialLogin(token,registrationId));
    }

    // Initiates the connection to Facebook for authentication
    @GetMapping("/facebook/connect")
    public String connectFacebook(){
        OAuth2Parameters parameters = oAuthUtils.getOauth2Parameters();
        return "redirect:" + userService.facebookConnectionFactory.getOAuthOperations().buildAuthorizeUrl(parameters);
    }

    // Callback URL after successful authentication with Facebook
    @GetMapping("/facebook/connect/callback")
    public ResponseEntity<String> connectFacebookCallback(@RequestParam(name = "code") String authenticationCode){
       String responce = userService.fetchUserDataUsingCallBackApi(authenticationCode);
       return ResponseEntity.ok(responce);
    }

    // API endpoint to fetch user data from Facebook using an access token
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/facebook/fetch-user-data")
    public ResponseEntity<?> fetchUserDataFromFacebook(@RequestParam(name = "token", required = false) String accessToken){
        SocialProfileDetailsTransfer userDetails = userService.fetchUserDataFromFacebookApi(accessToken);
        return ResponseEntity.ok(userDetails);
    }

    // API endpoint to fetch user data from LinkedIn using an access token
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/linked-in/fetch-user-data")
    public ResponseEntity<?> fetchUserDataFromLinkedIn(@RequestParam(name = "token", required = false) String accessToken){
        SocialProfileDetailsTransfer userDetails = userService.fetchUserDataFromLinkedinApi(accessToken);
        return ResponseEntity.ok(userDetails);
    }

}
