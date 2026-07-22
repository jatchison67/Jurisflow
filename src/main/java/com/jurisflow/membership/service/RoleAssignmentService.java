package com.jurisflow.membership.service;

import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.entity.TenantUserRole;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.membership.repository.TenantUserRoleRepository;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RoleAssignmentService {

    private final TenantUserRepository tenantUserRepository;
    private final RoleRepository roleRepository;
    private final TenantUserRoleRepository tenantUserRoleRepository;

    public RoleAssignmentService(
            TenantUserRepository tenantUserRepository,
            RoleRepository roleRepository,
            TenantUserRoleRepository tenantUserRoleRepository) {

        this.tenantUserRepository = tenantUserRepository;
        this.roleRepository = roleRepository;
        this.tenantUserRoleRepository = tenantUserRoleRepository;
    }

    public void assignRole(UUID tenantUserId, UUID roleId) {

        TenantUser tenantUser =
                tenantUserRepository.findById(tenantUserId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("TenantUser not found."));

        Role role =
                roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Role not found."));

        if (!tenantUserRoleRepository.existsByTenantUserAndRole(tenantUser, role)) {

            tenantUserRoleRepository.save(
                    new TenantUserRole(tenantUser, role));
        }
    }

    public void removeRole(UUID tenantUserId, UUID roleId) {

        TenantUser tenantUser =
                tenantUserRepository.findById(tenantUserId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("TenantUser not found."));

        Role role =
                roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Role not found."));

        tenantUserRoleRepository.findByTenantUser(tenantUser)
                .stream()
                .filter(tur -> tur.getRole().equals(role))
                .findFirst()
                .ifPresent(tenantUserRoleRepository::delete);
    }

    public List<TenantUserRole> getAssignedRoles(UUID tenantUserId) {

        TenantUser tenantUser =
                tenantUserRepository.findById(tenantUserId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("TenantUser not found."));

        return tenantUserRoleRepository.findByTenantUser(tenantUser);
    }

}