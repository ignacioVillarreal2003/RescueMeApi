package com.api.rescuemeapi.domain.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterUserReply(

        @NotNull(message = "Saga id is required")
        UUID sagaId,

        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Success is required")
        boolean success,

        String token,
        String refreshToken,
        String errorMessage
) {
}