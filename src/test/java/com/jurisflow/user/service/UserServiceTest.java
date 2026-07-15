package com.jurisflow.user.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createsAUserWhenEmailIsAvailable() {
        when(userRepository.findByEmail("ada@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.createUser("ada@example.com", "Ada", "Lovelace");

        assertThat(user.getEmail()).isEqualTo("ada@example.com");
        assertThat(user.getFirstName()).isEqualTo("Ada");
        verify(userRepository).save(user);
    }

    @Test
    void rejectsAnExistingEmail() {
        when(userRepository.findByEmail("ada@example.com"))
                .thenReturn(Optional.of(new User("ada@example.com", "Ada", "Lovelace")));

        assertThatThrownBy(() -> userService.createUser("ada@example.com", "Ada", "Lovelace"))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("A user with this email already exists");

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
