package com.jurisflow.membership.repository;

import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.entity.TenantUserRole;
import com.jurisflow.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TenantUserRoleRepository
        extends JpaRepository<TenantUserRole, UUID> {

    List<TenantUserRole> findByTenantUser(
            TenantUser tenantUser);

    boolean existsByTenantUserAndRole(
            TenantUser tenantUser,
            Role role);

    void deleteByTenantUser(
            TenantUser tenantUser);
}