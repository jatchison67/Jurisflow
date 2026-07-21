package com.jurisflow.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Auth0AudienceValidatorTest {

    private final Auth0AudienceValidator validator = new Auth0AudienceValidator("https://api.jurisflow.com");

    @Test
    void acceptsATokenForTheConfiguredAudience() {
        Jwt token = tokenWithAudience("https://api.jurisflow.com");

        assertThat(validator.validate(token).hasErrors()).isFalse();
    }

    @Test
    void rejectsATokenForAnotherAudience() {
        Jwt token = tokenWithAudience("https://other.example.com");

        assertThat(validator.validate(token).hasErrors()).isTrue();
    }

    private Jwt tokenWithAudience(String audience) {
        Instant now = Instant.now();
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .issuer("https://jurisflow.us.auth0.com/")
                .subject("auth0|user-123")
                .audience(List.of(audience))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300))
                .build();
    }
}
