package com.jurisflow.role.entity;

import com.jurisflow.common.entity.BaseEntity;
import com.jurisflow.tenant.entity.Tenant;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private boolean systemRole;


    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();


    protected Role() {
    }


    public Role(
            Tenant tenant,
            String name,
            boolean systemRole
    ) {

        this.tenant = tenant;
        this.name = name;
        this.systemRole = systemRole;

    }


    public Tenant getTenant() {
        return tenant;
    }


    public String getName() {
        return name;
    }


    public boolean isSystemRole() {
        return systemRole;
    }


    public Set<Permission> getPermissions() {
        return Set.copyOf(permissions);
    }


    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

}
