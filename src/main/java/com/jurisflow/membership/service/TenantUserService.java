package com.jurisflow.membership.service;

import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TenantUserService {


    private final TenantUserRepository tenantUserRepository;


    public TenantUserService(
            TenantUserRepository tenantUserRepository
    ) {

        this.tenantUserRepository = tenantUserRepository;

    }


    public TenantUser assignUserToTenant(
            Tenant tenant,
            User user
    ) {

        TenantUser membership =
                new TenantUser(
                        tenant,
                        user
                );


        return tenantUserRepository.save(
                membership
        );

    }


    public TenantUser findMembership(
            UUID tenantId,
            UUID userId
    ) {

        return tenantUserRepository
                .findByTenantIdAndUserId(
                        tenantId,
                        userId
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "User is not a member of this tenant"
                        )
                );

    }

}