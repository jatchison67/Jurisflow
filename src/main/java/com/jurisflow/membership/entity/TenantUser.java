package com.jurisflow.membership.entity;

import com.jurisflow.common.entity.BaseEntity;
import com.jurisflow.tenant.entity.Tenant;
import com.jurisflow.user.entity.User;
import com.jurisflow.role.entity.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tenant_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_user",
                        columnNames = {
                                "tenant_id",
                                "user_id"
                        }
                )
        }
)
public class TenantUser extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tenant_id",
            nullable = false
    )
    private Tenant tenant;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    @Column(nullable = false)
    private boolean active = true;


    @ManyToMany
    @JoinTable(
            name = "tenant_user_roles",
            joinColumns = @JoinColumn(name = "tenant_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    protected TenantUser() {
    }


    public TenantUser(
            Tenant tenant,
            User user
    ) {

        this.tenant = tenant;
        this.user = user;

    }


    public Tenant getTenant() {
        return tenant;
    }


    public User getUser() {
        return user;
    }


    public boolean isActive() {
        return active;
    }


    public void deactivate() {

        this.active = false;

    }


    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }


    public void addRole(Role role) {
        roles.add(role);
    }

}
