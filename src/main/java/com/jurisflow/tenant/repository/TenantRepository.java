package com.jurisflow.tenant.repository;

import com.jurisflow.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository
        extends JpaRepository<Tenant, UUID> {


    Optional<Tenant> findBySlug(String slug);
    Optional<Tenant> findByName(String name);

}