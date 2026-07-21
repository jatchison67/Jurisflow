package com.jurisflow.matter.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.matter.entity.Matter;
import com.jurisflow.matter.entity.MatterStatus;
import com.jurisflow.matter.repository.MatterRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MatterService {

    private final MatterRepository matterRepository;
    private final TenantService tenantService;

    public MatterService(MatterRepository matterRepository, TenantService tenantService) {
        this.matterRepository = matterRepository;
        this.tenantService = tenantService;
    }

    public Matter createMatter(
            UUID tenantId,
            String reference,
            String title,
            String description
    ) {
        if (matterRepository.findByTenantIdAndReference(tenantId, reference).isPresent()) {
            throw new ResourceConflictException("A matter with this reference already exists for this tenant");
        }

        Tenant tenant = tenantService.findById(tenantId);
        return matterRepository.save(new Matter(tenant, reference, title, description));
    }

    public Matter findMatter(UUID tenantId, UUID matterId) {
        return matterRepository.findByTenantIdAndId(tenantId, matterId)
                .orElseThrow(() -> new ResourceNotFoundException("Matter not found"));
    }

    public List<Matter> findAllMatters(UUID tenantId) {
        tenantService.findById(tenantId);
        return matterRepository.findAllByTenantId(tenantId);
    }

    public Matter changeStatus(UUID tenantId, UUID matterId, MatterStatus status) {
        Matter matter = findMatter(tenantId, matterId);
        matter.changeStatus(status);
        return matterRepository.save(matter);
    }
}
