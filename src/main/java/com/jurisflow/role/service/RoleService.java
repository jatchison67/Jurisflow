package com.jurisflow.role.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.service.TenantUserService;
import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.repository.RoleRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final TenantService tenantService;
    private final TenantUserService tenantUserService;
    private final PermissionService permissionService;

    public RoleService(
            RoleRepository roleRepository,
            TenantService tenantService,
            TenantUserService tenantUserService,
            PermissionService permissionService
    ) {
        this.roleRepository = roleRepository;
        this.tenantService = tenantService;
        this.tenantUserService = tenantUserService;
        this.permissionService = permissionService;
    }

    public Role createRole(UUID tenantId, String name) {

        if (roleRepository.findByTenantIdAndName(tenantId, name).isPresent()) {
            throw new ResourceConflictException("A role with this name already exists for this tenant");
        }

        Tenant tenant = tenantService.findById(tenantId);
        return roleRepository.save(new Role(tenant, name, false));
    }

    @Transactional
    public Role addPermission(UUID tenantId, UUID roleId, String permissionCode) {

        Role role = findRoleInTenant(tenantId, roleId);
        Permission permission = permissionService.findByCode(permissionCode);
        role.addPermission(permission);
        return roleRepository.save(role);
    }

    @Transactional
    public TenantUser assignRoleToUser(UUID tenantId, UUID userId, UUID roleId) {

        Role role = findRoleInTenant(tenantId, roleId);
        TenantUser membership = tenantUserService.findMembership(tenantId, userId);
        membership.addRole(role);
        return membership;
    }

    @Transactional(readOnly = true)
    public boolean userHasPermission(UUID tenantId, UUID userId, String permissionCode) {

        TenantUser membership = tenantUserService.findMembership(tenantId, userId);

        return membership.isActive() && membership.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }

    private Role findRoleInTenant(UUID tenantId, UUID roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (!role.getTenant().getId().equals(tenantId)) {
            throw new ResourceNotFoundException("Role not found");
        }

        return role;
    }
}
