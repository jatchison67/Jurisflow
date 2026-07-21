package com.jurisflow.security;

import com.jurisflow.role.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("tenantPermissionAuthorization")
public class TenantPermissionAuthorization {

    private final CurrentUserService currentUserService;
    private final RoleService roleService;

    public TenantPermissionAuthorization(
            CurrentUserService currentUserService,
            RoleService roleService
    ) {
        this.currentUserService = currentUserService;
        this.roleService = roleService;
    }

    public boolean hasPermission(UUID tenantId, String permissionCode) {

        return roleService.userHasPermission(
                tenantId,
                currentUserService.getCurrentUser().getId(),
                permissionCode
        );
    }
}
