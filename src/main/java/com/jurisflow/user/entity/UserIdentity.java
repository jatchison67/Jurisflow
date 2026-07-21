package com.jurisflow.user.entity;

import com.jurisflow.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "user_identities",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_identity_provider_user",
                        columnNames = {"provider", "provider_user_id"}
                )
        }
)
public class UserIdentity extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    @Column(nullable = false)
    private String provider;


    @Column(nullable = false)
    private String providerUserId;


    protected UserIdentity() {
    }


    public UserIdentity(
            User user,
            String provider,
            String providerUserId
    ) {

        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;

    }


    public User getUser() {
        return user;
    }


    public String getProvider() {
        return provider;
    }


    public String getProviderUserId() {
        return providerUserId;
    }

}
