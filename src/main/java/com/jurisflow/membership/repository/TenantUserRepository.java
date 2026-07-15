package com.jurisflow.membership.repository;

import com.jurisflow.membership.entity.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantUserRepository
        extends JpaRepository<TenantUser, UUID> {


    Optional<TenantUser> findByTenantIdAndUserId(
            UUID tenantId,
            UUID userId
    );

}