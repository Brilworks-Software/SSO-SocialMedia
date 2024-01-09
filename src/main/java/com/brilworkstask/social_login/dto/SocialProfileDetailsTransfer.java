package com.brilworkstask.social_login.dto;

import com.brilworkstask.social_login.enums.ProviderEnum;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class SocialProfileDetailsTransfer {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private ProviderEnum provider;
}
