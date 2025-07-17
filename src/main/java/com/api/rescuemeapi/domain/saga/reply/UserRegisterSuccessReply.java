package com.api.rescuemeapi.domain.saga.reply;

import java.util.UUID;

public record UserRegisterSuccessReply(
        UUID sagaId,
        String email,
        String token,
        String refreshToken
) {
}