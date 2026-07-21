package com.jurisflow.role.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(

        @NotBlank(message = "Role name is required")
        String name

) {
}
