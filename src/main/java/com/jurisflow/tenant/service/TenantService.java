package com.jurisflow.tenant.service;

import com.jurisflow.common.event.TenantCreatedEvent;
import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.repository.TenantRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class TenantService {


    private final TenantRepository tenantRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TenantService(
            TenantRepository tenantRepository,
            ApplicationEventPublisher eventPublisher
    ) {

        this.tenantRepository = tenantRepository;
        this.eventPublisher = eventPublisher;

    }

    public Tenant createTenant(String name, String slug) {

        if (tenantRepository.findByName(name).isPresent()) {
            throw new ResourceConflictException(
                    "A tenant with this name already exists"
            );
        }

        if (tenantRepository.findBySlug(slug).isPresent()) {
            throw new ResourceConflictException(
                    "A tenant with this slug already exists"
            );
        }

        Tenant tenant = new Tenant(name, slug);

        Tenant savedTenant =
                tenantRepository.save(tenant);


        eventPublisher.publishEvent(
                new TenantCreatedEvent(
                        savedTenant.getId(),
                        savedTenant.getName()
                )
        );


        return savedTenant;    }

    public Tenant findBySlug(String slug) {

        return tenantRepository.findBySlug(slug)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Tenant not found"
                        )
                );

    }

    public List<Tenant> findAll() {

        return tenantRepository.findAll();

    }

    public Tenant findById(UUID id) {

        return tenantRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Tenant not found"
                        )
                );

    }

    public void deleteTenant(UUID id) {

        Tenant tenant = findById(id);

        tenant.deactivate();

        tenantRepository.save(tenant);

    }


}