package com.jurisflow.security;

import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.service.UserIdentityService;
import com.jurisflow.user.service.UserIdentityService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserIdentityService userIdentityService;

    public CurrentUserService(UserIdentityService userIdentityService) {
        this.userIdentityService = userIdentityService;
    }

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new AccessDeniedException("An Auth0 access token is required");
        }

        String subject = jwtAuthentication.getToken().getSubject();

        try {
            return userIdentityService.findUserByAuth0Subject(subject);
        } catch (ResourceNotFoundException exception) {
            throw new AccessDeniedException("This Auth0 identity is not linked to a JurisFlow user");
        }
    }

    public User linkAuth0IdentityByVerifiedEmail(
            String subject,
            String email,
            boolean emailVerified
    ) {
        return userIdentityService.linkAuth0IdentityByVerifiedEmail(
                subject,
                email,
                emailVerified
        );
    }
}
