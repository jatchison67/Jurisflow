package com.jurisflow.common.event;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class TenantCreatedEventListener {


    @EventListener
    public void handleTenantCreated(
            TenantCreatedEvent event
    ) {

        System.out.println(
                "EVENT RECEIVED: Tenant created - "
                        + event.getTenantName()
                        + " "
                        + event.getTenantId()
        );

    }

}