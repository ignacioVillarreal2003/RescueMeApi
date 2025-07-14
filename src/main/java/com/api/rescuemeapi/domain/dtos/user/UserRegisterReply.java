package com.api.rescuemeapi.domain.dtos.user;

import java.util.UUID;

public record UserRegisterReply(
        UUID sagaId,
        boolean success,
        String email,
        String token,
        String refreshToken,
        String errorMessage
) {
}