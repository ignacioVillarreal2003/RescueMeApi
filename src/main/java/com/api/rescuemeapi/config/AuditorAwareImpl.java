package com.api.rescuemeapi.config;

import com.api.rescuemeapi.config.authentication.AuthenticationUserProvider;
import com.api.rescuemeapi.config.authentication.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditorAwareImpl  implements AuditorAware<String> {

    private final AuthenticationUserProvider authenticationUserProvider;

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        UserPrincipal user = authenticationUserProvider.getUser();
        if (user != null) {
            return Optional.of(user.getEmail());
        }
        return Optional.of("system");
    }
}
