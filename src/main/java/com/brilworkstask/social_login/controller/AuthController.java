package com.brilworkstask.social_login.controller;

import com.brilworkstask.social_login.service.UserService;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/social-login")
public class AuthController {
    private final FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    UserService userService;

    @Autowired
    OAuthUtils oAuthUtils;

    public AuthController(FacebookConnectionFactory facebookConnectionFactory){
        this.facebookConnectionFactory = facebookConnectionFactory;
    }

    // Initiates the connection to Facebook for authentication
    @GetMapping("/facebook/connect")
    public String connectFacebook(){
        OAuth2Parameters parameters = oAuthUtils.getOauth2Parameters();
        return "redirect:" + facebookConnectionFactory.getOAuthOperations().buildAuthorizeUrl(parameters);
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
        String responce = userService.fetchUserDataFromFacebookApi(accessToken);
        return ResponseEntity.ok(responce);
    }

    // API endpoint to fetch user data from LinkedIn using an access token
    @PostMapping("/linked-in/fetch-user-data")
    public ResponseEntity<?> fetchUserDataFromLinkedIn(@RequestParam(name = "token", required = false) String accessToken){
        String responce = userService.fetchUserDataFromLinkedinApi(accessToken);
        return ResponseEntity.ok(responce);
    }

}
