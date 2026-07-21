package com.jurisflow.matter.repository;

import com.jurisflow.matter.entity.Matter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatterRepository extends JpaRepository<Matter, UUID> {

    Optional<Matter> findByTenantIdAndReference(UUID tenantId, String reference);

    Optional<Matter> findByTenantIdAndId(UUID tenantId, UUID id);

    List<Matter> findAllByTenantId(UUID tenantId);
}
