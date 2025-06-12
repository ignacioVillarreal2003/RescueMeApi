package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.config.authentication.AuthUserProvider;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;
    private final AuthUserProvider authUserProvider;

    public Optional<User> getCurrentUser() {
        Long id = authUserProvider.getUserId();
        return userRepository.findById(id);
    }
}
