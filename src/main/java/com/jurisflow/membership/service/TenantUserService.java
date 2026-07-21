package com.jurisflow.membership.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.membership.entity.TenantUser;
import com.jurisflow.membership.repository.TenantUserRepository;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.tenant.service.TenantService;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class TenantUserService {


    private final TenantUserRepository tenantUserRepository;
    private final TenantService tenantService;
    private final UserService userService;


    public TenantUserService(
            TenantUserRepository tenantUserRepository,
            TenantService tenantService,
            UserService userService
    ) {

        this.tenantUserRepository = tenantUserRepository;
        this.tenantService = tenantService;
        this.userService = userService;

    }


    public TenantUser assignUserToTenant(
            UUID tenantId,
            UUID userId
    ) {

        if (tenantUserRepository.findByTenantIdAndUserId(tenantId, userId).isPresent()) {
            throw new ResourceConflictException(
                    "User is already a member of this tenant"
            );
        }

        Tenant tenant = tenantService.findById(tenantId);
        User user = userService.findById(userId);

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
                        () -> new ResourceNotFoundException(
                                "User is not a member of this tenant"
                        )
                );

    }

    public List<TenantUser> findAllByTenantId(UUID tenantId) {

        tenantService.findById(tenantId);

        return tenantUserRepository.findAllByTenantId(tenantId);

    }

}
