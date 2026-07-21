package com.jurisflow.role.controller;

import com.jurisflow.role.dto.CreatePermissionRequest;
import com.jurisflow.role.dto.PermissionResponse;
import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody CreatePermissionRequest request
    ) {
        Permission permission = permissionService.createPermission(request.code(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionResponse.from(permission));
    }
}
