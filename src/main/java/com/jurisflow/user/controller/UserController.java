package com.jurisflow.user.controller;

import com.jurisflow.user.dto.CreateUserRequest;
import com.jurisflow.user.dto.UserResponse;
import com.jurisflow.user.entity.User;
import com.jurisflow.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    public UserController(
            UserService userService
    ) {

        this.userService = userService;

    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {


        User user =
                userService.createUser(
                        request.email(),
                        request.firstName(),
                        request.lastName()
                );


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                UserResponse.from(user)
        );

    }

}
