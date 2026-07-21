package com.jurisflow.matter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMatterRequest(

        @NotBlank(message = "Matter reference is required")
        @Size(max = 100, message = "Matter reference must not exceed 100 characters")
        String reference,

        @NotBlank(message = "Matter title is required")
        @Size(max = 255, message = "Matter title must not exceed 255 characters")
        String title,

        @Size(max = 4000, message = "Matter description must not exceed 4000 characters")
        String description

) {
}
