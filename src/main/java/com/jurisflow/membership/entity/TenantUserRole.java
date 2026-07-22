package com.jurisflow.membership.entity;

import com.jurisflow.common.entity.BaseEntity;
import com.jurisflow.role.entity.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "tenant_user_roles",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"tenant_user_id","role_id"}
        )
)
public class TenantUserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tenant_user_id", nullable=false)
    private TenantUser tenantUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    protected TenantUserRole(){}

    public TenantUserRole(TenantUser tenantUser, Role role){
        this.tenantUser = tenantUser;
        this.role = role;
    }

    public TenantUser getTenantUser(){
        return tenantUser;
    }

    public Role getRole(){
        return role;
    }
}