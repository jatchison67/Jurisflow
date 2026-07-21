package com.jurisflow.role.controller;

import com.jurisflow.role.dto.AssignPermissionRequest;
import com.jurisflow.role.dto.CreateRoleRequest;
import com.jurisflow.role.dto.RoleResponse;
import com.jurisflow.role.entity.Role;
import com.jurisflow.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'tenant:manage')")
    public ResponseEntity<RoleResponse> createRole(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateRoleRequest request
    ) {
        Role role = roleService.createRole(tenantId, request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleResponse.from(role));
    }

    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'tenant:manage')")
    public ResponseEntity<RoleResponse> addPermission(
            @PathVariable UUID tenantId,
            @PathVariable UUID roleId,
            @Valid @RequestBody AssignPermissionRequest request
    ) {
        Role role = roleService.addPermission(tenantId, roleId, request.permissionCode());
        return ResponseEntity.ok(RoleResponse.from(role));
    }

    @PostMapping("/{roleId}/users/{userId}")
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'tenant:manage')")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable UUID tenantId,
            @PathVariable UUID roleId,
            @PathVariable UUID userId
    ) {
        roleService.assignRoleToUser(tenantId, userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
