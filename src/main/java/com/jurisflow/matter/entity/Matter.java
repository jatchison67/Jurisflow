package com.jurisflow.matter.entity;

import com.jurisflow.common.entity.BaseEntity;
import com.jurisflow.tenant.entity.Tenant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "matters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_matter_tenant_reference",
                        columnNames = {"tenant_id", "reference"}
                )
        }
)
public class Matter extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatterStatus status = MatterStatus.OPEN;

    protected Matter() {
    }

    public Matter(Tenant tenant, String reference, String title, String description) {
        this.tenant = tenant;
        this.reference = reference;
        this.title = title;
        this.description = description;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public String getReference() {
        return reference;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MatterStatus getStatus() {
        return status;
    }

    public void changeStatus(MatterStatus status) {
        this.status = status;
    }
}
