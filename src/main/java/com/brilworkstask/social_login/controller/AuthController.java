package com.brilworkstask.social_login.controller;

import com.brilworkstask.social_login.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;
    @GetMapping("/fetchdata")
    public ResponseEntity<?> getDataFromAccessToken(@RequestParam("token") String token,
                                                    @RequestParam("id") String registrationId) throws GeneralSecurityException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.fetchAndSaveDataGoogleSocialLogin(token,registrationId));
    }
}
