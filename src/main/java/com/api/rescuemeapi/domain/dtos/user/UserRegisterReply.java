package com.api.rescuemeapi.domain.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserRegisterReply(
        UUID sagaId,
        Long userId,
        boolean success,
        String token,
        String refreshToken,
        String errorMessage
) {
}