package com.jurisflow.matter.controller;

import com.jurisflow.matter.dto.CreateMatterRequest;
import com.jurisflow.matter.dto.MatterResponse;
import com.jurisflow.matter.dto.UpdateMatterStatusRequest;
import com.jurisflow.matter.entity.Matter;
import com.jurisflow.matter.service.MatterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/matters")
public class MatterController {

    private final MatterService matterService;

    public MatterController(MatterService matterService) {
        this.matterService = matterService;
    }

    @PostMapping
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'matter:manage')")
    public ResponseEntity<MatterResponse> createMatter(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateMatterRequest request
    ) {
        Matter matter = matterService.createMatter(
                tenantId,
                request.reference(),
                request.title(),
                request.description()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(MatterResponse.from(matter));
    }

    @GetMapping
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'matter:read')")
    public ResponseEntity<List<MatterResponse>> getMatters(@PathVariable UUID tenantId) {
        List<MatterResponse> response = matterService.findAllMatters(tenantId)
                .stream()
                .map(MatterResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matterId}")
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'matter:read')")
    public ResponseEntity<MatterResponse> getMatter(
            @PathVariable UUID tenantId,
            @PathVariable UUID matterId
    ) {
        return ResponseEntity.ok(MatterResponse.from(matterService.findMatter(tenantId, matterId)));
    }

    @PatchMapping("/{matterId}/status")
    @PreAuthorize("@tenantPermissionAuthorization.hasPermission(#tenantId, 'matter:manage')")
    public ResponseEntity<MatterResponse> changeMatterStatus(
            @PathVariable UUID tenantId,
            @PathVariable UUID matterId,
            @Valid @RequestBody UpdateMatterStatusRequest request
    ) {
        Matter matter = matterService.changeStatus(tenantId, matterId, request.status());
        return ResponseEntity.ok(MatterResponse.from(matter));
    }
}
