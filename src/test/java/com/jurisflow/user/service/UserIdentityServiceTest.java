package com.jurisflow.user.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.entity.UserIdentity;
import com.jurisflow.user.repository.UserIdentityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserIdentityServiceTest {

    @Mock
    private UserIdentityRepository userIdentityRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserIdentityService userIdentityService;

    @Test
    void linksAnAvailableAuth0IdentityToAUser() {
        UUID userId = UUID.randomUUID();
        User user = new User("ada@example.com", "Ada", "Lovelace");
        when(userIdentityRepository.findByProviderAndProviderUserId("auth0", "auth0|123"))
                .thenReturn(Optional.empty());
        when(userService.findById(userId)).thenReturn(user);
        when(userIdentityRepository.save(org.mockito.ArgumentMatchers.any(UserIdentity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserIdentity identity = userIdentityService.linkAuth0Identity(userId, "auth0|123");

        assertThat(identity.getUser()).isSameAs(user);
        assertThat(identity.getProvider()).isEqualTo("auth0");
        assertThat(identity.getProviderUserId()).isEqualTo("auth0|123");
        verify(userIdentityRepository).save(identity);
    }

    @Test
    void rejectsAnAuth0IdentityAlreadyLinkedToAnotherUser() {
        when(userIdentityRepository.findByProviderAndProviderUserId("auth0", "auth0|123"))
                .thenReturn(Optional.of(org.mockito.Mockito.mock(UserIdentity.class)));

        assertThatThrownBy(() -> userIdentityService.linkAuth0Identity(UUID.randomUUID(), "auth0|123"))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("This Auth0 identity is already linked to a user");
    }

    @Test
    void linksTheUserWithTheMatchingVerifiedEmail() {
        User user = new User("ada@example.com", "Ada", "Lovelace");
        when(userService.findByEmail("ada@example.com")).thenReturn(user);
        when(userIdentityRepository.findByProviderAndProviderUserId("auth0", "auth0|123"))
                .thenReturn(Optional.empty());
        when(userIdentityRepository.save(org.mockito.ArgumentMatchers.any(UserIdentity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userIdentityService.linkAuth0IdentityByVerifiedEmail(
                "auth0|123",
                "ada@example.com",
                true
        );

        assertThat(result).isSameAs(user);
    }
}
