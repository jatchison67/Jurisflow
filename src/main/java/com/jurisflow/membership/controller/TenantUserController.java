package com.jurisflow.membership.controller;

import com.jurisflow.membership.dto.AssignTenantUserRequest;
import com.jurisflow.membership.dto.TenantUserResponse;
import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.service.TenantUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/users")
public class TenantUserController {

    private final TenantUserService tenantUserService;

    public TenantUserController(TenantUserService tenantUserService) {
        this.tenantUserService = tenantUserService;
    }

    @PostMapping
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'tenant:manage')")
    public ResponseEntity<TenantUserResponse> assignUser(
            @PathVariable UUID tenantId,
            @Valid @RequestBody AssignTenantUserRequest request
    ) {

        TenantUser membership = tenantUserService.assignUserToTenant(
                tenantId,
                request.userId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TenantUserResponse.from(membership));
    }

    @GetMapping
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'tenant:manage')")
    public ResponseEntity<List<TenantUserResponse>> getTenantUsers(
            @PathVariable UUID tenantId
    ) {

        List<TenantUserResponse> response = tenantUserService.findAllByTenantId(tenantId)
                .stream()
                .map(TenantUserResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }
}
