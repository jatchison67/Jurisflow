package com.jurisflow.membership.dto;

import com.jurisflow.membership.entity.TenantUser;

import java.util.UUID;

public record TenantUserResponse(

        UUID id,
        UUID tenantId,
        UUID userId,
        boolean active

) {

    public static TenantUserResponse from(TenantUser membership) {

        return new TenantUserResponse(
                membership.getId(),
                membership.getTenant().getId(),
                membership.getUser().getId(),
                membership.isActive()
        );

    }
}
