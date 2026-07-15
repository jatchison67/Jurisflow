package com.jurisflow.tenant.entity;

import com.jurisflow.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity {


    @Column(nullable = false, unique = true)
    private String name;


    @Column(nullable = false, unique = true)
    private String slug;


    @Column(nullable = false)
    private boolean active = true;


    protected Tenant() {
    }


    public Tenant(
            String name,
            String slug
    ) {

        this.name = name;
        this.slug = slug;

    }


    public String getName() {
        return name;
    }


    public String getSlug() {
        return slug;
    }


    public boolean isActive() {
        return active;
    }

    public void deactivate() {

        this.active = false;

    }

}