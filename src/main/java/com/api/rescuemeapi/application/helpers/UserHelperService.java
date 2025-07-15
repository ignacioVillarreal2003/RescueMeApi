package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.application.exceptions.UserNotFoundException;
import com.api.rescuemeapi.config.authentication.AuthenticationUserProvider;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.infrastructure.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHelperService {

    private final UserRepository userRepository;
    private final AuthenticationUserProvider authenticationUserProvider;

    public User getCurrentUser() {
        String email = authenticationUserProvider.getUser().getEmail();
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
