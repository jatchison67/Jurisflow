package com.jurisflow.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@ConditionalOnProperty(name = "auth0.enabled", havingValue = "true")
public class Auth0JwtConfig {

    @Bean
    public NimbusJwtDecoder jwtDecoder(Auth0Properties properties) {

        String issuerUri = required("auth0.issuer-uri", properties.getIssuerUri());
        String audience = required("auth0.audience", properties.getAudience());
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuerUri);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuerUri),
                new Auth0AudienceValidator(audience)
        );
        decoder.setJwtValidator(validator);
        return decoder;
    }

    private String required(String property, String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(property + " must be configured when auth0.enabled=true");
        }
        return value;
    }
}
