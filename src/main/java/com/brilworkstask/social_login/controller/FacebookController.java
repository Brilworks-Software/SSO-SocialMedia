package com.brilworkstask.social_login.controller;

import com.brilworkstask.social_login.service.UserService;
import com.brilworkstask.social_login.utils.OAuthUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/social-login")
public class FacebookController {
    private final FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    UserService userService;

    @Autowired
    OAuthUtils oAuthUtils;

    public FacebookController(FacebookConnectionFactory facebookConnectionFactory){
        this.facebookConnectionFactory = facebookConnectionFactory;
    }


    // Initiates the connection to Facebook for authentication
    @GetMapping("/connect/facebook")
    public String connectFacebook(){
        OAuth2Parameters parameters = oAuthUtils.getOauth2Parameters();
        return "redirect:" + facebookConnectionFactory.getOAuthOperations().buildAuthorizeUrl(parameters);
    }

    // Callback URL after successful authentication with Facebook
    @GetMapping("/connect/facebook/callback")
    public ResponseEntity<String> connectFacebookCallback(@RequestParam(name = "code") String authenticationCode){
       String responce = userService.fetchUserDataUsingCallBackApi(authenticationCode);
       return ResponseEntity.ok(responce);
    }

    // API endpoint to fetch user data from Facebook using an access token
    @PostMapping("/facebook/fetch-user-data")
    public ResponseEntity<?> fetchUserData(@RequestParam(name = "token", required = false) String accessToken) throws BadRequestException {
        String responce = userService.fetchUserData(accessToken);
        return ResponseEntity.ok(responce);
    }

}
