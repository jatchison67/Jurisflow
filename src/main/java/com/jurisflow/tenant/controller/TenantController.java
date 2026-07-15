package com.jurisflow.tenant.controller;

import com.jurisflow.tenant.dto.CreateTenantRequest;
import com.jurisflow.tenant.dto.TenantResponse;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {


    private final TenantService tenantService;


    public TenantController(
            TenantService tenantService
    ) {

        this.tenantService = tenantService;

    }


    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {

        Tenant tenant =
                tenantService.createTenant(
                        request.name(),
                        request.slug()
                );


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TenantResponse.from(tenant));

    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> getTenants() {

        List<TenantResponse> response =
                tenantService.findAll()
                        .stream()
                        .map(TenantResponse::from)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenant(
            @PathVariable UUID id
    ) {

        Tenant tenant =
                tenantService.findById(id);

        return ResponseEntity.ok(
                TenantResponse.from(tenant)
        );

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<TenantResponse> getTenantBySlug(
            @PathVariable String slug
    ) {

        Tenant tenant =
                tenantService.findBySlug(slug);

        return ResponseEntity.ok(
                TenantResponse.from(tenant)
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(
            @PathVariable UUID id
    ) {

        tenantService.deleteTenant(id);

        return ResponseEntity.noContent().build();

    }
}