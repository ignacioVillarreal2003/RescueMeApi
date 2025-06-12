package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.api.producers.RegisterUserSagaPublisher;
import com.api.rescuemeapi.application.helpers.UserHelper;
import com.api.rescuemeapi.domain.dtos.user.*;
import com.api.rescuemeapi.application.mappers.UserResponseMapper;
import com.api.rescuemeapi.domain.enums.Role;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.infrastructure.persistence.SagaStore;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final UserResponseMapper userResponseMapper;
    private final RegisterUserSagaPublisher registrationPublisher;
    private final SagaStore sagaStore;

    public UserResponse get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return userResponseMapper.apply(user);
    }

    public InitiateRegisterUserResponse initiateRegistration(@Valid InitiateRegisterUserRequest request) {
        UUID sagaId = UUID.randomUUID();

        sagaStore.putInitiateRegisterUser(sagaId, request);

        registrationPublisher.publishRegisterUserCommand(
                RegisterUserCommand.builder()
                        .sagaId(sagaId)
                        .email(request.email())
                        .password(request.password())
                        .roles(List.of(Role.RESCUEME_USER))
                        .build()
        );

        return InitiateRegisterUserResponse.builder()
                .sagaId(sagaId)
                .build();
    }

    public UserResponse completeRegistration(InitiateRegisterUserRequest request, RegisterUserReply message) {
        Long userId = message.userId();

        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        User createdUser = userRepository.save(
                User.builder()
                        .id(userId)
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .phone(request.phone())
                        .address(request.address())
                        .build());

        return userResponseMapper.apply(createdUser);
    }

    public UserResponse update(UpdateUserRequest request) {
        User user = userHelper.getCurrentUser()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Authenticated user not found"));

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.address() != null) user.setAddress(request.address());
        if (request.phone() != null) user.setPhone(request.phone());

        User updated = userRepository.save(user);

        return userResponseMapper.apply(updated);
    }
}
