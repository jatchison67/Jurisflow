package com.jurisflow.user.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.common.exception.ResourceNotFoundException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {


    private final UserRepository userRepository;


    public UserService(
            UserRepository userRepository
    ) {

        this.userRepository = userRepository;

    }


    public User createUser(
            String email,
            String firstName,
            String lastName
    ) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceConflictException(
                    "A user with this email already exists"
            );
        }

        User user =
                new User(
                        email,
                        firstName,
                        lastName
                );


        return userRepository.save(
                user
        );

    }

    public User findById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

    }

}
