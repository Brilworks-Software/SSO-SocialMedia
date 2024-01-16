package com.brilworkstask.social_login.controller;

import com.brilworkstask.social_login.dto.SocialProfileDetailsTransfer;
import com.brilworkstask.social_login.service.UserService;
import com.brilworkstask.social_login.service.UserServiceImpl;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // TODO - Jay - Move this to userService

    @Autowired
    UserServiceImpl userService;

    @Autowired
    OAuthUtils oAuthUtils;

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
