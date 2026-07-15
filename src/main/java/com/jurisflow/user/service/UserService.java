package com.jurisflow.user.service;

import com.jurisflow.common.exception.ResourceConflictException;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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

}
