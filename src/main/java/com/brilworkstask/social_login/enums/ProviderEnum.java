package com.brilworkstask.social_login.enums;

public enum ProviderEnum {
    FACEBOOK, GOOGLE, LINKEDIN;

    public static ProviderEnum getSocialLoginProvider(String issuer) {
        if(issuer.equalsIgnoreCase(ProviderEnum.GOOGLE.name()))
            return ProviderEnum.GOOGLE;
        else if (issuer.contains(ProviderEnum.FACEBOOK.name()))
            return ProviderEnum.FACEBOOK;
        else if (issuer.contains(ProviderEnum.LINKEDIN.name()))
            return ProviderEnum.LINKEDIN;
        return null;
    }
}
