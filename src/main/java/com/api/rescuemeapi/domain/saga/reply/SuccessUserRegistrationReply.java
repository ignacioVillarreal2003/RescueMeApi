package com.api.rescuemeapi.domain.saga.reply;

import java.util.UUID;

public record SuccessUserRegistrationReply(
        UUID sagaId,
        String token,
        String refreshToken
) {
}