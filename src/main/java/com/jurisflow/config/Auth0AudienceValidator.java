package com.jurisflow.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class Auth0AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public Auth0AudienceValidator(String audience) {
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        if (token.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "The token does not contain the required audience",
                null
        );
        return OAuth2TokenValidatorResult.failure(error);
    }
}
