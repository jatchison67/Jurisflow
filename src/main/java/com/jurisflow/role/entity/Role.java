package com.jurisflow.role.entity;

import com.jurisflow.common.entity.BaseEntity;
import com.jurisflow.tenant.entity.Tenant;
import jakarta.persistence.*;

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

}