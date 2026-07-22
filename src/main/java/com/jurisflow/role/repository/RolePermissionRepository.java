package com.jurisflow.role.repository;

import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    List<RolePermission> findByRole(Role role);

    boolean existsByRoleAndPermission(Role role, Permission permission);

    void deleteByRole(Role role);

    void deleteByRoleAndPermission(Role role, Permission permission);

    List<RolePermission> findByRoleIn(List<Role> roles);

}