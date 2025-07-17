package com.api.rescuemeapi.domain.saga.reply;

import java.util.UUID;

public record UserRegisterFailureReply(
        UUID sagaId,
        Integer status,
        String message
) {
}