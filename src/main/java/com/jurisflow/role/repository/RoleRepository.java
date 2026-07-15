package com.jurisflow.role.repository;

import com.jurisflow.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository
        extends JpaRepository<Role, UUID> {

}