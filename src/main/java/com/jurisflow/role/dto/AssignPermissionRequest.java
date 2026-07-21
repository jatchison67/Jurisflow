package com.jurisflow.role.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignPermissionRequest(

        @NotBlank(message = "Permission code is required")
        String permissionCode

) {
}
