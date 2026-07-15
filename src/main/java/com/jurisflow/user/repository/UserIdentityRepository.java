package com.jurisflow.user.repository;

import com.jurisflow.user.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserIdentityRepository
        extends JpaRepository<UserIdentity, UUID> {


    Optional<UserIdentity> findByProviderAndProviderUserId(
            String provider,
            String providerUserId
    );

}