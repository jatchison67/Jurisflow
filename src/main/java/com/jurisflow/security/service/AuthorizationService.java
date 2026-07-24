package com.jurisflow.security.service;

import java.util.Set;
import java.util.UUID;

public interface AuthorizationService {

    boolean hasRoleName(
            UUID tenantId,
            UUID userId,
            String roleName);

    boolean hasPermission(
            UUID tenantId,
            UUID userId,
            String permissionCode);

    Set<String> getRoleNames(
            UUID tenantId,
            UUID userId);

    Set<String> getPermissionCodes(
            UUID tenantId,
            UUID userId);
}