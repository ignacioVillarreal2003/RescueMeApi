package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.api.producers.UserRegisterSagaPublisher;
import com.api.rescuemeapi.application.exceptions.UserNotFoundException;
import com.api.rescuemeapi.application.helpers.UserHelperService;
import com.api.rescuemeapi.domain.dtos.user.*;
import com.api.rescuemeapi.application.mappers.UserResponseMapper;
import com.api.rescuemeapi.domain.constants.Role;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.domain.models.UserRegisterSaga;
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
    private final UserHelperService userHelper;
    private final UserResponseMapper userResponseMapper;
    private final UserRegisterSagaPublisher userRegisterSagaPublisher;
    private final UserRegisterSagaService userRegisterSagaService;

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userResponseMapper.apply(user);
    }

    public InitiateRegisterUserResponse initiateRegistration(@Valid InitiateRegisterUserRequest request) {
        UUID sagaId = UUID.randomUUID();

        userRegisterSagaService.startSaga(sagaId, request.firstName(), request.lastName(), request.phone(), request.email());

        userRegisterSagaPublisher.publishUserRegisterCommand(
                UserRegisterCommand.builder()
                        .sagaId(sagaId)
                        .email(request.email())
                        .password(request.password())
                        .roles(List.of(Role.ROLE_RESCUE_ME_USER.toString()))
                        .build()
        );

        return InitiateRegisterUserResponse.builder()
                .sagaId(sagaId)
                .build();
    }

    public UserResponse completeRegistration(UserRegisterReply message) {
        String email = message.email();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        UserRegisterSaga userRegisterSaga = userRegisterSagaService.getUserRegisterSaga(message.sagaId());

        User createdUser = userRepository.save(
                User.builder()
                        .email(email)
                        .firstName(userRegisterSaga.getFirstName())
                        .lastName(userRegisterSaga.getLastName())
                        .phone(userRegisterSaga.getPhone())
                        .address(userRegisterSaga.getAddress())
                        .build());

        return userResponseMapper.apply(createdUser);
    }

    public UserResponse updateUser(UpdateUserRequest request) {
        User user = userHelper.getCurrentUser();

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.address() != null) user.setAddress(request.address());
        if (request.phone() != null) user.setPhone(request.phone());

        User updated = userRepository.save(user);

        return userResponseMapper.apply(updated);
    }
}
