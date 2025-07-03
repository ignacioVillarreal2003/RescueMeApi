package com.api.rescuemeapi.api.controllers;

import com.api.rescuemeapi.domain.dtos.user.*;
import com.api.rescuemeapi.application.services.UserService;
import com.api.rescuemeapi.infrastructure.persistence.SagaStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SagaStore sagaStore;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.get(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InitiateRegisterUserResponse> initiateRegistration(@Valid @RequestBody InitiateRegisterUserRequest request) {
        InitiateRegisterUserResponse response = userService.initiateRegistration(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("registration/status/{sagaId}")
    public ResponseEntity<?> getRegistrationStatus(@PathVariable UUID sagaId) {
        Optional<UserRegisterSagaResponse> registerUserSagaResponse = sagaStore.getRegisterUserSagaCache(sagaId);

        if (registerUserSagaResponse.isPresent()) {
            UserRegisterSagaResponse response = registerUserSagaResponse.get();
            if (response.isSuccess()) {
                return ResponseEntity.ok(response.getUser());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.getErrorMessage());
            }
        }
        else {
            return ResponseEntity.status(202)
                    .body(Map.of("status", "PENDING"));
        }
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.update(request);
        return ResponseEntity.ok(response);
    }
}
