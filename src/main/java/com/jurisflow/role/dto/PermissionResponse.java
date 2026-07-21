package com.jurisflow.role.dto;

import com.jurisflow.role.entity.Permission;

import java.util.UUID;

public record PermissionResponse(UUID id, String code, String description) {

    public static PermissionResponse from(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getCode(),
                permission.getDescription()
        );
    }
}
