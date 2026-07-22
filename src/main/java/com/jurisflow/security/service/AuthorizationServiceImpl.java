package com.jurisflow.security.service;

import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.entity.TenantUserRole;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.membership.repository.TenantUserRoleRepository;
import com.jurisflow.role.entity.RolePermission;
import com.jurisflow.role.repository.RolePermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthorizationServiceImpl
        implements AuthorizationService {

    private final TenantUserRepository tenantUserRepository;

    private final TenantUserRoleRepository tenantUserRoleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    public AuthorizationServiceImpl(
            TenantUserRepository tenantUserRepository,
            TenantUserRoleRepository tenantUserRoleRepository,
            RolePermissionRepository rolePermissionRepository) {

        this.tenantUserRepository = tenantUserRepository;
        this.tenantUserRoleRepository = tenantUserRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public boolean hasRole(
            UUID tenantId,
            UUID userId,
            String roleName) {

        return getRoles(
                tenantId,
                userId)
                .contains(roleName);
    }

    @Override
    public boolean hasPermission(
            UUID tenantId,
            UUID userId,
            String permissionCode) {

        return getPermissionCodes(
                tenantId,
                userId)
                .contains(permissionCode);
    }

    @Override
    public Set<String> getRoles(
            UUID tenantId,
            UUID userId) {

        TenantUser tenantUser =
                tenantUserRepository
                        .findByTenantIdAndUserId(
                                tenantId,
                                userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Tenant membership not found."));

        Set<String> roles = new HashSet<>();

        for (TenantUserRole assignment :
                tenantUserRoleRepository.findByTenantUser(
                        tenantUser)) {

            roles.add(
                    assignment.getRole().getName());
        }

        return roles;
    }

    @Override
    public Set<String> getPermissionCodes(
            UUID tenantId,
            UUID userId) {

        TenantUser tenantUser =
                tenantUserRepository
                        .findByTenantIdAndUserId(
                                tenantId,
                                userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Tenant membership not found."));

        Set<String> permissionCodes = new HashSet<>();

        for (TenantUserRole assignment :
                tenantUserRoleRepository.findByTenantUser(
                        tenantUser)) {

            for (RolePermission rolePermission :
                    rolePermissionRepository.findByRole(
                            assignment.getRole())) {

                permissionCodes.add(
                        rolePermission
                                .getPermission()
                                .getCode());
            }
        }

        return permissionCodes;
    }
}