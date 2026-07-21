package com.jurisflow.membership.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignTenantUserRequest(

        @NotNull(message = "User ID is required")
        UUID userId

) {
}
