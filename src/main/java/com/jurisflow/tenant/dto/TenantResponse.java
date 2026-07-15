package com.jurisflow.tenant.dto;

import com.jurisflow.tenant.entity.Tenant;

import java.util.UUID;

public record TenantResponse(

        UUID id,
        String name,
        String slug,
        boolean active

) {

    public static TenantResponse from(
            Tenant tenant
    ) {

        return new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getSlug(),
                tenant.isActive()
        );

    }

}