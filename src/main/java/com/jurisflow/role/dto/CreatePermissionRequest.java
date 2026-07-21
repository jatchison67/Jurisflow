package com.jurisflow.role.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(

        @NotBlank(message = "Permission code is required")
        String code,

        @NotBlank(message = "Permission description is required")
        String description

) {
}
