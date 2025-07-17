package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.application.saga.services.UserRegistrationSagaStateService;
import com.api.rescuemeapi.infrastructure.messaging.publisher.UserRegistrationPublisher;
import com.api.rescuemeapi.application.exceptions.UserNotFoundException;
import com.api.rescuemeapi.application.helpers.UserHelperService;
import com.api.rescuemeapi.domain.dtos.user.*;
import com.api.rescuemeapi.application.mappers.UserResponseMapper;
import com.api.rescuemeapi.domain.constants.Role;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.domain.saga.command.UserRegisterInitialCommand;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserHelperService userHelper;
    private final UserResponseMapper userResponseMapper;
    private final UserRegistrationPublisher publisher;
    private final UserRegistrationSagaStateService sagaStateService;

    @Transactional
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return userResponseMapper.apply(user);
    }

    @Transactional
    public InitiateRegisterUserResponse initiateRegistration(@Valid InitiateRegisterUserRequest request) {
        String email = request.email();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        UUID sagaId = UUID.randomUUID();
        sagaStateService.startSaga(sagaId,
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.address());
        publisher.publishUserRegisterInitialCommand(
                UserRegisterInitialCommand.builder()
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

    @Transactional
    public UserResponse completeRegistration(String email,
                                             String firstName,
                                             String lastName,
                                             String phone,
                                             String address) {
        User createdUser = userRepository.save(
                User.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .phone(phone)
                        .address(address)
                        .build());
        return userResponseMapper.apply(createdUser);
    }

    @Transactional
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
