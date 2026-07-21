package com.jurisflow.role.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.role.entity.Permission;
import com.jurisflow.role.repository.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(String code, String description) {

        if (permissionRepository.findByCode(code).isPresent()) {
            throw new ResourceConflictException("A permission with this code already exists");
        }

        return permissionRepository.save(new Permission(code, description));
    }

    public Permission findByCode(String code) {

        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }
}
