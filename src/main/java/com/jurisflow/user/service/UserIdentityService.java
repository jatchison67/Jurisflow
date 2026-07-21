package com.jurisflow.user.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.entity.UserIdentity;
import com.jurisflow.user.repository.UserIdentityRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserIdentityService {

    public static final String AUTH0_PROVIDER = "auth0";

    private final UserIdentityRepository userIdentityRepository;
    private final UserService userService;

    public UserIdentityService(
            UserIdentityRepository userIdentityRepository,
            UserService userService
    ) {
        this.userIdentityRepository = userIdentityRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public User findUserByAuth0Subject(String subject) {

        return userIdentityRepository
                .findByProviderAndProviderUserId(AUTH0_PROVIDER, subject)
                .map(UserIdentity::getUser)
                .orElseThrow(() -> new ResourceNotFoundException("No JurisFlow user is linked to this Auth0 identity"));
    }

    public UserIdentity linkAuth0Identity(UUID userId, String subject) {

        User user = userService.findById(userId);
        return linkAuth0Identity(user, subject);
    }

    public User linkAuth0IdentityByVerifiedEmail(
            String subject,
            String email,
            boolean emailVerified
    ) {

        if (!emailVerified || email == null || email.isBlank()) {
            throw new AccessDeniedException("Auth0 must provide a verified email address to link an account");
        }

        User user = userService.findByEmail(email);
        linkAuth0Identity(user, subject);
        return user;
    }

    private UserIdentity linkAuth0Identity(User user, String subject) {

        if (userIdentityRepository.findByProviderAndProviderUserId(AUTH0_PROVIDER, subject).isPresent()) {
            throw new ResourceConflictException("This Auth0 identity is already linked to a user");
        }

        return userIdentityRepository.save(new UserIdentity(user, AUTH0_PROVIDER, subject));
    }
}
