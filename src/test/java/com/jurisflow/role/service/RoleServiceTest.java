package com.jurisflow.role.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.service.TenantUserService;
import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.repository.RoleRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TenantService tenantService;

    @Mock
    private TenantUserService tenantUserService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private RoleService roleService;

    @Test
    void rejectsADuplicateRoleNameWithinATenant() {
        UUID tenantId = UUID.randomUUID();
        when(roleRepository.findByTenantIdAndName(tenantId, "Attorney"))
                .thenReturn(Optional.of(org.mockito.Mockito.mock(Role.class)));

        assertThatThrownBy(() -> roleService.createRole(tenantId, "Attorney"))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("A role with this name already exists for this tenant");

        verify(tenantService, never()).findById(tenantId);
    }

    @Test
    void addsAPermissionToARoleInTheRequestedTenant() {
        UUID tenantId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        Tenant tenant = org.mockito.Mockito.mock(Tenant.class);
        Role role = org.mockito.Mockito.mock(Role.class);
        Permission permission = org.mockito.Mockito.mock(Permission.class);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(role.getTenant()).thenReturn(tenant);
        when(tenant.getId()).thenReturn(tenantId);
        when(permissionService.findByCode("matter:read")).thenReturn(permission);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.addPermission(tenantId, roleId, "matter:read");

        assertThat(result).isSameAs(role);
        verify(role).addPermission(permission);
        verify(roleRepository).save(role);
    }

    @Test
    void recognizesAPermissionGrantedThroughAMembershipRole() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        TenantUser membership = org.mockito.Mockito.mock(TenantUser.class);
        Role role = org.mockito.Mockito.mock(Role.class);
        Permission permission = org.mockito.Mockito.mock(Permission.class);
        when(tenantUserService.findMembership(tenantId, userId)).thenReturn(membership);
        when(membership.isActive()).thenReturn(true);
        when(membership.getRoles()).thenReturn(Set.of(role));
        when(role.getPermissions()).thenReturn(Set.of(permission));
        when(permission.getCode()).thenReturn("matter:read");

        boolean hasPermission = roleService.userHasPermission(tenantId, userId, "matter:read");

        assertThat(hasPermission).isTrue();
    }
}
