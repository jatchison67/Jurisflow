package com.jurisflow.membership.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantUserServiceTest {

    @Mock
    private TenantUserRepository tenantUserRepository;

    @Mock
    private TenantService tenantService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TenantUserService tenantUserService;

    @Test
    void assignsAnExistingUserToAnExistingTenant() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Tenant tenant = new Tenant("Atchison Law Group", "atchison-law");
        User user = new User("ada@example.com", "Ada", "Lovelace");
        when(tenantUserRepository.findByTenantIdAndUserId(tenantId, userId)).thenReturn(Optional.empty());
        when(tenantService.findById(tenantId)).thenReturn(tenant);
        when(userService.findById(userId)).thenReturn(user);
        when(tenantUserRepository.save(org.mockito.ArgumentMatchers.any(TenantUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TenantUser membership = tenantUserService.assignUserToTenant(tenantId, userId);

        assertThat(membership.getTenant()).isSameAs(tenant);
        assertThat(membership.getUser()).isSameAs(user);
        verify(tenantUserRepository).save(membership);
    }

    @Test
    void rejectsDuplicateMembershipBeforeLookingUpEntities() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(tenantUserRepository.findByTenantIdAndUserId(tenantId, userId))
                .thenReturn(Optional.of(mock(TenantUser.class)));

        assertThatThrownBy(() -> tenantUserService.assignUserToTenant(tenantId, userId))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("User is already a member of this tenant");

        verify(tenantService, never()).findById(tenantId);
        verify(userService, never()).findById(userId);
    }

    @Test
    void reportsAMissingMembershipAsNotFound() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(tenantUserRepository.findByTenantIdAndUserId(tenantId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tenantUserService.findMembership(tenantId, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User is not a member of this tenant");
    }
}
