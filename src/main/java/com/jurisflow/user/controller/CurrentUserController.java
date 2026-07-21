package com.jurisflow.user.controller;

import com.jurisflow.security.CurrentUserService;
import com.jurisflow.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class CurrentUserController {

    private final CurrentUserService currentUserService;

    public CurrentUserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> currentUser() {
        return ResponseEntity.ok(UserResponse.from(currentUserService.getCurrentUser()));
    }

    @PostMapping("/link")
    public ResponseEntity<UserResponse> linkCurrentAuth0Identity(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserResponse response = UserResponse.from(
                currentUserService.linkAuth0IdentityByVerifiedEmail(
                        jwt.getSubject(),
                        jwt.getClaimAsString("https://jurisflow.com/email"),
                        Boolean.TRUE.equals(jwt.getClaim("https://jurisflow.com/email_verified"))
                )
        );
        return ResponseEntity.ok(response);
    }
}
