package com.jurisflow.matter.dto;

import com.jurisflow.matter.entity.MatterStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateMatterStatusRequest(

        @NotNull(message = "Matter status is required")
        MatterStatus status

) {
}
