package com.jurisflow.matter.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.matter.entity.Matter;
import com.jurisflow.matter.entity.MatterStatus;
import com.jurisflow.matter.repository.MatterRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatterServiceTest {

    @Mock
    private MatterRepository matterRepository;

    @Mock
    private TenantService tenantService;

    @InjectMocks
    private MatterService matterService;

    @Test
    void createsMatterForAnExistingTenant() {
        UUID tenantId = UUID.randomUUID();
        Tenant tenant = new Tenant("Atchison Law Group", "atchison-law");
        when(matterRepository.findByTenantIdAndReference(tenantId, "ALG-001")).thenReturn(Optional.empty());
        when(tenantService.findById(tenantId)).thenReturn(tenant);
        when(matterRepository.save(org.mockito.ArgumentMatchers.any(Matter.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Matter matter = matterService.createMatter(tenantId, "ALG-001", "Smith divorce", "Initial consultation");

        assertThat(matter.getTenant()).isSameAs(tenant);
        assertThat(matter.getStatus()).isEqualTo(MatterStatus.OPEN);
        verify(matterRepository).save(matter);
    }

    @Test
    void rejectsDuplicateMatterReferenceWithinATenant() {
        UUID tenantId = UUID.randomUUID();
        when(matterRepository.findByTenantIdAndReference(tenantId, "ALG-001"))
                .thenReturn(Optional.of(org.mockito.Mockito.mock(Matter.class)));

        assertThatThrownBy(() -> matterService.createMatter(tenantId, "ALG-001", "Smith divorce", null))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("A matter with this reference already exists for this tenant");

        verify(tenantService, never()).findById(tenantId);
    }

    @Test
    void preventsMatterLookupAcrossTenants() {
        when(matterRepository.findByTenantIdAndId(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> matterService.findMatter(UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Matter not found");
    }

    @Test
    void changesMatterStatus() {
        UUID tenantId = UUID.randomUUID();
        UUID matterId = UUID.randomUUID();
        Matter matter = new Matter(new Tenant("Atchison Law Group", "atchison-law"), "ALG-001", "Smith divorce", null);
        when(matterRepository.findByTenantIdAndId(tenantId, matterId)).thenReturn(Optional.of(matter));
        when(matterRepository.save(matter)).thenReturn(matter);

        Matter result = matterService.changeStatus(tenantId, matterId, MatterStatus.CLOSED);

        assertThat(result.getStatus()).isEqualTo(MatterStatus.CLOSED);
        verify(matterRepository).save(matter);
    }
}
