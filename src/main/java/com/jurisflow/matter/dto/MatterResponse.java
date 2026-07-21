package com.jurisflow.matter.dto;

import com.jurisflow.matter.entity.Matter;
import com.jurisflow.matter.entity.MatterStatus;

import java.util.UUID;

public record MatterResponse(

        UUID id,
        UUID tenantId,
        String reference,
        String title,
        String description,
        MatterStatus status

) {

    public static MatterResponse from(Matter matter) {
        return new MatterResponse(
                matter.getId(),
                matter.getTenant().getId(),
                matter.getReference(),
                matter.getTitle(),
                matter.getDescription(),
                matter.getStatus()
        );
    }
}
