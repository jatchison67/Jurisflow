package com.jurisflow.role.service;

import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.entity.RolePermission;
import com.jurisflow.role.repository.PermissionRepository;
import com.jurisflow.role.repository.RolePermissionRepository;
import com.jurisflow.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PermissionAssignmentService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionAssignmentService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {

        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public void assignPermission(UUID roleId, UUID permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found."));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Permission not found."));

        if (!rolePermissionRepository.existsByRoleAndPermission(role, permission)) {

            rolePermissionRepository.save(
                    new RolePermission(role, permission));
        }
    }

    public void removePermission(UUID roleId, UUID permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found."));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Permission not found."));

        rolePermissionRepository.deleteByRoleAndPermission(role, permission);
    }

    public List<RolePermission> getAssignedPermissions(UUID roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found."));

        return rolePermissionRepository.findByRole(role);
    }
}