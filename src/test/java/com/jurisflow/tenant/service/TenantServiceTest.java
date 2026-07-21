package com.jurisflow.tenant.service;

import com.jurisflow.common.event.TenantCreatedEvent;
import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TenantService tenantService;

    @Test
    void createsTenantAndPublishesCreatedEvent() {
        when(tenantRepository.findByName("Atchison Law Group")).thenReturn(Optional.empty());
        when(tenantRepository.findBySlug("atchison-law")).thenReturn(Optional.empty());
        when(tenantRepository.save(org.mockito.ArgumentMatchers.any(Tenant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tenant result = tenantService.createTenant("Atchison Law Group", "atchison-law");

        ArgumentCaptor<TenantCreatedEvent> event = ArgumentCaptor.forClass(TenantCreatedEvent.class);
        ArgumentCaptor<Tenant> savedTenant = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepository).save(savedTenant.capture());
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(result).isSameAs(savedTenant.getValue());
        assertThat(event.getValue().getTenantName()).isEqualTo("Atchison Law Group");
    }

    @Test
    void rejectsDuplicateSlugWithoutSaving() {
        when(tenantRepository.findByName("Atchison Law Group")).thenReturn(Optional.empty());
        when(tenantRepository.findBySlug("atchison-law"))
                .thenReturn(Optional.of(new Tenant("Existing Firm", "atchison-law")));

        assertThatThrownBy(() -> tenantService.createTenant("Atchison Law Group", "atchison-law"))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("A tenant with this slug already exists");

        verify(tenantRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deactivatesExistingTenantInsteadOfDeletingIt() {
        UUID id = UUID.randomUUID();
        Tenant tenant = new Tenant("Atchison Law Group", "atchison-law");
        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));

        tenantService.deleteTenant(id);

        assertThat(tenant.isActive()).isFalse();
        verify(tenantRepository).save(tenant);
    }

    @Test
    void reportsMissingTenant() {
        UUID id = UUID.randomUUID();
        when(tenantRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tenantService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tenant not found");
    }
}
