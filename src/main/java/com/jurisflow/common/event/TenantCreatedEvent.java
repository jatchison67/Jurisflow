package com.jurisflow.common.event;

import java.util.UUID;

public class TenantCreatedEvent extends DomainEvent {


    private final UUID tenantId;
    private final String tenantName;


    public TenantCreatedEvent(
            UUID tenantId,
            String tenantName
    ) {

        super();

        this.tenantId = tenantId;
        this.tenantName = tenantName;

    }


    public UUID getTenantId() {
        return tenantId;
    }


    public String getTenantName() {
        return tenantName;
    }

}