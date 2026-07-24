package com.jurisflow.security.service;

import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.entity.TenantUserRole;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.membership.repository.TenantUserRoleRepository;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.entity.RolePermission;
import com.jurisflow.role.repository.RolePermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public boolean hasRoleName(
            UUID tenantId,
            UUID userId,
            String roleName) {

        TenantUser tenantUser =
                tenantUserRepository
                        .findByTenantIdAndUserId(
                                tenantId,
                                userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Tenant membership not found."));

        return tenantUserRoleRepository
                .findByTenantUser(tenantUser)
                .stream()
                .anyMatch(role ->
                        role.getRole()
                                .getName()
                                .equals(roleName));
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
    public Set<String> getRoleNames(
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

        List<Role> roles =
                tenantUserRoleRepository.findByTenantUser(tenantUser)
                        .stream()
                        .map(TenantUserRole::getRole)
                        .toList();

        if (roles.isEmpty()) {
            return Set.of();
        }

        return rolePermissionRepository.findByRoleIn(roles)
                .stream()
                .map(rolePermission ->
                        rolePermission
                                .getPermission()
                                .getCode())
                .collect(Collectors.toSet());
    }
}