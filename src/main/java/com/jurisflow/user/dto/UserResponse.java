package com.jurisflow.user.dto;

import com.jurisflow.user.entity.User;

import java.util.UUID;

public record UserResponse(

        UUID id,
        String email,
        String firstName,
        String lastName,
        boolean active

) {


    public static UserResponse from(
            User user
    ) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive()
        );

    }

}