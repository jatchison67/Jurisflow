package com.jurisflow.role.dto;

import com.jurisflow.role.entity.Role;

import java.util.Set;
import java.util.UUID;

public record RoleResponse(

        UUID id,
        UUID tenantId,
        String name,
        boolean systemRole,
        Set<String> permissions

) {

    public static RoleResponse from(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getTenant().getId(),
                role.getName(),
                role.isSystemRole(),
                role.getPermissions().stream().map(permission -> permission.getCode()).collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
    }
}
