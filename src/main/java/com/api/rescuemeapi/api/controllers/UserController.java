package com.api.rescuemeapi.api.controllers;

import com.api.rescuemeapi.application.services.UserRegisterSagaService;
import com.api.rescuemeapi.domain.dtos.user.*;
import com.api.rescuemeapi.application.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRegisterSagaService userRegisterSagaService;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<InitiateRegisterUserResponse> initiateRegistration(@Valid @RequestBody InitiateRegisterUserRequest request) {
        InitiateRegisterUserResponse response = userService.initiateRegistration(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("registration/status/{sagaId}")
    public ResponseEntity<UserRegisterSagaResponse> getRegistrationStatus(@PathVariable UUID sagaId) {
        UserRegisterSagaResponse response = userRegisterSagaService.getUserRegisterSagaResponse(sagaId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }
}
